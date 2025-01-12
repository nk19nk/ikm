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
import ru.ikm.ikm.entity.Restaurant;
import ru.ikm.ikm.repository.RestaurantRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Restaurant> getAll(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);
        return new PagedModel<>(restaurants);
    }

    @GetMapping("/{id}")
    public Restaurant getOne(@PathVariable Integer id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Restaurant> getMany(@RequestParam List<Integer> ids) {
        return restaurantRepository.findAllById(ids);
    }

    @PostMapping
    public Restaurant create(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PatchMapping("/{id}")
    public Restaurant patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(restaurant).readValue(patchNode);

        return restaurantRepository.save(restaurant);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Restaurant> restaurants = restaurantRepository.findAllById(ids);

        for (Restaurant restaurant : restaurants) {
            objectMapper.readerForUpdating(restaurant).readValue(patchNode);
        }

        List<Restaurant> resultRestaurants = restaurantRepository.saveAll(restaurants);
        return resultRestaurants.stream()
                .map(Restaurant::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Restaurant delete(@PathVariable Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
        }
        return restaurant;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        restaurantRepository.deleteAllById(ids);
    }
}
