package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Inventory;
import com.java.alfaizunaproject.model.InventoryLog;
import com.java.alfaizunaproject.repository.InventoryRepository;
import com.java.alfaizunaproject.repository.InventoryLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryLogRepository inventoryLogRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testCreateOrUpdateInventoryCreate() throws Exception {
        Inventory inventory = new Inventory(1L, 10);
        when(inventoryRepository.findByItemId(anyLong())).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory createdInventory = inventoryService.createOrUpdateInventory(inventory);
        assertNotNull(createdInventory);
        assertEquals(1L, createdInventory.getItemId());
        assertEquals(10, createdInventory.getQty());
        verify(inventoryLogRepository, times(1)).save(any(InventoryLog.class));
    }

    @Test
    public void testCreateOrUpdateInventoryUpdate() throws Exception {
        Inventory existingInventory = new Inventory(1L, 10);
        when(inventoryRepository.findByItemId(anyLong())).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(existingInventory);

        Inventory inventory = new Inventory(1L, 5);
        Inventory updatedInventory = inventoryService.createOrUpdateInventory(inventory);
        assertNotNull(updatedInventory);
        assertEquals(1L, updatedInventory.getItemId());
        assertEquals(15, updatedInventory.getQty());
        verify(inventoryLogRepository, times(1)).save(any(InventoryLog.class));
    }

    @Test
    public void testGetInventory() {
        Inventory inventory = new Inventory(1L, 10);
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));

        Inventory foundInventory = inventoryService.getInventory(1L);
        assertNotNull(foundInventory);
        assertEquals(1L, foundInventory.getItemId());
        assertEquals(10, foundInventory.getQty());
    }

    @Test
    public void testGetAllInventories() {
        Inventory inventory = new Inventory(1L, 10);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Inventory> page = new PageImpl<>(Arrays.asList(inventory));

        when(inventoryRepository.findAll(pageable)).thenReturn(page);

        List<Inventory> inventories = inventoryService.getAllInventories(0, 10);
        assertNotNull(inventories);
        assertEquals(1, inventories.size());
        assertEquals(1L, inventories.get(0).getItemId());
        assertEquals(10, inventories.get(0).getQty());
    }

    @Test
    public void testUpdateInventoryTopUp() throws Exception {
        Inventory inventory = new Inventory(1L, 10);
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory updatedInventory = inventoryService.updateInventory(1L, 5, 'T');
        assertNotNull(updatedInventory);
        assertEquals(15, updatedInventory.getQty());
        verify(inventoryLogRepository, times(1)).save(any(InventoryLog.class));
    }

    @Test
    public void testUpdateInventoryWithdrawal() throws Exception {
        Inventory inventory = new Inventory(1L, 10);
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory updatedInventory = inventoryService.updateInventory(1L, 5, 'W');
        assertNotNull(updatedInventory);
        assertEquals(5, updatedInventory.getQty());
        verify(inventoryLogRepository, times(1)).save(any(InventoryLog.class));
    }

    @Test
    public void testDeleteInventory() {
        Inventory inventory = new Inventory(1L, 10);
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));

        inventoryService.deleteInventory(1L);
        verify(inventoryRepository, times(1)).deleteById(1L);
        verify(inventoryLogRepository, times(1)).save(any(InventoryLog.class));
    }
}
