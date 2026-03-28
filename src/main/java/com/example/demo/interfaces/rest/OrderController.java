package com.example.demo.interfaces.rest;

import com.example.demo.infrastructure.order.OrderJpaEntity;
import com.example.demo.infrastructure.order.OrderJpaRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderJpaRepository orderRepository;

    public OrderController(OrderJpaRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public PageResponse<OrderResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OrderJpaEntity> orderPage = orderRepository.findAll(PageRequest.of(page, size));
        List<OrderResponse> content = orderPage.getContent().stream()
                .map(e -> new OrderResponse(e.getId(), e.getCustomerId(), e.getAmount()))
                .collect(Collectors.toList());
        return new PageResponse<>(
                content,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                page,
                size,
                orderPage.hasNext()
        );
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable UUID id) {
        OrderJpaEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return new OrderResponse(entity.getId(), entity.getCustomerId(), entity.getAmount());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderJpaEntity entity = new OrderJpaEntity(UUID.randomUUID(), request.customerId(), request.amount());
        orderRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponse(entity.getId(), entity.getCustomerId(), entity.getAmount()));
    }

    @PutMapping("/{id}")
    public OrderResponse updateOrder(@PathVariable UUID id, @Valid @RequestBody OrderRequest request) {
        OrderJpaEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        entity.setCustomerId(request.customerId());
        entity.setAmount(request.amount());
        orderRepository.save(entity);
        return new OrderResponse(entity.getId(), entity.getCustomerId(), entity.getAmount());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found: " + id);
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleNotFound(IllegalArgumentException ex) {
        ApiError body = new ApiError("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
