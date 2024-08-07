package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Item;
import com.java.alfaizunaproject.repository.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable).getContent();
    }

    public Item updateItem(Long id, Item item) {
        return itemRepository.findById(id).map(existingItem -> {
            existingItem.setName(item.getName());
            existingItem.setPrice(item.getPrice());
            return itemRepository.save(existingItem);
        }).orElse(null);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
