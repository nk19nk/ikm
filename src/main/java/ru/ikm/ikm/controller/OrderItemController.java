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
import ru.ikm.ikm.entity.OrderItem;
import ru.ikm.ikm.entity.OrderItemId;
import ru.ikm.ikm.repository.OrderItemRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<OrderItem> getAll(Pageable pageable) {
        Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);
        return new PagedModel<>(orderItems);
    }

    @GetMapping("/{id}")
    public OrderItem getOne(@PathVariable OrderItemId id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        return orderItemOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<OrderItem> getMany(@RequestParam List<OrderItemId> ids) {
        return orderItemRepository.findAllById(ids);
    }

    @PostMapping
    public OrderItem create(@RequestBody OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @PatchMapping("/{id}")
    public OrderItem patch(@PathVariable OrderItemId id, @RequestBody JsonNode patchNode) throws IOException {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(orderItem).readValue(patchNode);

        return orderItemRepository.save(orderItem);
    }

    @PatchMapping
    public List<OrderItemId> patchMany(@RequestParam List<OrderItemId> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<OrderItem> orderItems = orderItemRepository.findAllById(ids);

        for (OrderItem orderItem : orderItems) {
            objectMapper.readerForUpdating(orderItem).readValue(patchNode);
        }

        List<OrderItem> resultOrderItems = orderItemRepository.saveAll(orderItems);
        return resultOrderItems.stream()
                .map(OrderItem::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public OrderItem delete(@PathVariable OrderItemId id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        if (orderItem != null) {
            orderItemRepository.delete(orderItem);
        }
        return orderItem;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<OrderItemId> ids) {
        orderItemRepository.deleteAllById(ids);
    }
}
