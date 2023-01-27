/*
README
controller for item service. This is a simple controller which performs CRUD operations
Circuit breaker is injected to it's service class

*/


package capillary.controller;

import capillary.service.ItemService;
import capillary.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService service;

    @PostMapping
    public Item create(@RequestBody Item item) {
        return service.create(item);
    }

    @GetMapping
    public List<Item> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable Integer id, @RequestBody Item item) {
        return service.update(id, item);
    }

}
