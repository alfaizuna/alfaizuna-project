package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Inventory;
import com.java.alfaizunaproject.model.InventoryLog;
import com.java.alfaizunaproject.repository.InventoryLogRepository;
import com.java.alfaizunaproject.repository.InventoryRepository;
import com.java.alfaizunaproject.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryLogRepository inventoryLogRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }

    @Transactional
    public Inventory createOrUpdateInventory(Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findByItemId(inventory.getItemId()).orElse(null);

        int qtyBefore = 0;
        int qtyAfter;
        String actionType;

        if (existingInventory != null) {
            qtyBefore = existingInventory.getQty();
            existingInventory.setQty(existingInventory.getQty() + inventory.getQty());
            qtyAfter = existingInventory.getQty();
            actionType = "UPDATE";
            inventory = inventoryRepository.save(existingInventory);
        } else {
            qtyAfter = inventory.getQty();
            actionType = "CREATE";
            inventory = inventoryRepository.save(inventory);
        }

        String action = String.format("{\"item_id\": %d, \"qty_before\": %d, \"qty_after\": %d, \"type\": \"%s\"}",
                inventory.getItemId(), qtyBefore, qtyAfter, actionType);
        InventoryLog log = new InventoryLog(action);
        inventoryLogRepository.save(log);

        return inventory;
    }

    public Inventory getInventory(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public List<Inventory> getAllInventories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return inventoryRepository.findAll(pageable).getContent();
    }

    @Transactional
    public Inventory updateInventory(Long id, Integer qty, Character type) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        int qtyBefore = inventory.getQty();
        if (type == 'T') {
            inventory.setQty(inventory.getQty() + qty);
        } else if (type == 'W') {
            if (inventory.getQty() < qty) {
                throw new Exception("Insufficient quantity in inventory");
            }
            inventory.setQty(inventory.getQty() - qty);
        } else {
            throw new Exception("Invalid type");
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);

        String action = String.format("{\"item_id\": %d, \"qty_before\": %d, \"qty_after\": %d, \"type\": \"%s\"}",
                updatedInventory.getItemId(), qtyBefore, updatedInventory.getQty(), type);
        InventoryLog log = new InventoryLog(action);
        inventoryLogRepository.save(log);

        return updatedInventory;
    }

    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        int qtyBefore = inventory.getQty();

        inventoryRepository.deleteById(id);

        String action = String.format("{\"item_id\": %d, \"qty_before\": %d, \"qty_after\": 0, \"type\": \"DELETE\"}",
                inventory.getItemId(), qtyBefore);
        InventoryLog log = new InventoryLog(action);
        inventoryLogRepository.save(log);
    }
}
