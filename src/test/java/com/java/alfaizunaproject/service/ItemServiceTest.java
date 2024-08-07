package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Item;
import com.java.alfaizunaproject.repository.ItemRepository;
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
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testCreateItem() {
        Item item = new Item("Test Item", 100.0);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item createdItem = itemService.createItem(item);
        assertNotNull(createdItem);
        assertEquals("Test Item", createdItem.getName());
        assertEquals(100.0, createdItem.getPrice());
    }

    @Test
    public void testGetItem() {
        Item item = new Item("Test Item", 100.0);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItem(1L);
        assertNotNull(foundItem);
        assertEquals("Test Item", foundItem.getName());
        assertEquals(100.0, foundItem.getPrice());
    }

    @Test
    public void testGetAllItems() {
        Item item = new Item("Test Item", 100.0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(Arrays.asList(item));

        when(itemRepository.findAll(pageable)).thenReturn(page);

        List<Item> items = itemService.getAllItems(0, 10);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Test Item", items.get(0).getName());
        assertEquals(100.0, items.get(0).getPrice());
    }

    @Test
    public void testUpdateItem() {
        Item item = new Item("Test Item", 100.0);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item updatedItem = itemService.updateItem(1L, item);
        assertNotNull(updatedItem);
        assertEquals("Test Item", updatedItem.getName());
        assertEquals(100.0, updatedItem.getPrice());
    }

    @Test
    public void testDeleteItem() {
        Item item = new Item("Test Item", 100.0);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        itemService.deleteItem(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }
}
