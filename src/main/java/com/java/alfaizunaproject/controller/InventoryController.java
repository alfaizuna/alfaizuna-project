package com.java.alfaizunaproject.controller;

import com.java.alfaizunaproject.exception.ResourceNotFoundException;
import com.java.alfaizunaproject.model.Inventory;
import com.java.alfaizunaproject.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> createOrUpdateInventory(@RequestBody Inventory inventory) {
        try {
            Inventory createdOrUpdatedInventory = inventoryService.createOrUpdateInventory(inventory);
            return ResponseEntity.ok(createdOrUpdatedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventory(id);
        return inventory != null ? ResponseEntity.ok(inventory) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<Inventory> inventories = inventoryService.getAllInventories(page, size);
        return ResponseEntity.ok(inventories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestParam Integer qty, @RequestParam Character type) {
        try {
            Inventory updatedInventory = inventoryService.updateInventory(id, qty, type);
            return ResponseEntity.ok(updatedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
