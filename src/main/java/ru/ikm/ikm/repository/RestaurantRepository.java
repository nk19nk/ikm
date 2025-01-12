package ru.ikm.ikm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ikm.ikm.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}