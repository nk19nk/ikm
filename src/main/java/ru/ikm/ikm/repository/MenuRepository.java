package ru.ikm.ikm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ikm.ikm.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}