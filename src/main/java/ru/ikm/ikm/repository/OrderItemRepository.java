package ru.ikm.ikm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ikm.ikm.entity.OrderItem;
import ru.ikm.ikm.entity.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}