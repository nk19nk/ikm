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
import ru.ikm.ikm.entity.Menu;
import ru.ikm.ikm.repository.MenuRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu") // Добавляем базовый путь
public class MenuController {

    private final MenuRepository menuRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public String menuPage() {
        return "menu.html"; // Возвращаем страницу HTML
    }

    @GetMapping
    public PagedModel<Menu> getAll(Pageable pageable) {
        Page<Menu> menus = menuRepository.findAll(pageable);
        return new PagedModel<>(menus);
    }

    @GetMapping("/{id}")
    public Menu getOne(@PathVariable Integer id) {
        Optional<Menu> menuOptional = menuRepository.findById(id);
        return menuOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Menu> getMany(@RequestParam List<Integer> ids) {
        return menuRepository.findAllById(ids);
    }

    @PostMapping
    public Menu create(@RequestBody Menu menu) {
        return menuRepository.save(menu);
    }

    @PatchMapping("/{id}")
    public Menu patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        Menu menu = menuRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(menu).readValue(patchNode);

        return menuRepository.save(menu);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Menu> menus = menuRepository.findAllById(ids);

        for (Menu menu : menus) {
            objectMapper.readerForUpdating(menu).readValue(patchNode);
        }

        List<Menu> resultMenus = menuRepository.saveAll(menus);
        return resultMenus.stream()
                .map(Menu::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Menu delete(@PathVariable Integer id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menuRepository.delete(menu);
        }
        return menu;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        menuRepository.deleteAllById(ids);
    }
}
