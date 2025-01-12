package ru.ikm.ikm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ikm.ikm.entity.Order;
import ru.ikm.ikm.repository.OrderRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Order> getAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return new PagedModel<>(orders);
    }

    @GetMapping("/{id}")
    public Order getOne(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Order> getMany(@RequestParam List<Integer> ids) {
        return orderRepository.findAllById(ids);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PatchMapping("/{id}")
    public Order patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(order).readValue(patchNode);

        return orderRepository.save(order);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            objectMapper.readerForUpdating(order).readValue(patchNode);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream()
                .map(Order::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Order delete(@PathVariable Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return order;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        orderRepository.deleteAllById(ids);
    }
}
