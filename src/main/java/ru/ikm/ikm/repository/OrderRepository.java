package ru.ikm.ikm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ikm.ikm.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}