package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Inventory;
import com.java.alfaizunaproject.model.Item;
import com.java.alfaizunaproject.model.Order;
import com.java.alfaizunaproject.repository.InventoryRepository;
import com.java.alfaizunaproject.repository.ItemRepository;
import com.java.alfaizunaproject.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Order createOrder(Order order) throws Exception {
        // Validate inventory
        Item item = itemRepository.findById(order.getItem().getId())
                .orElseThrow(() -> new Exception("Item not found"));
        Inventory inventory = inventoryRepository.findByItemId(item.getId())
                .orElseThrow(() -> new Exception("Inventory not found"));

        if (inventory.getQty() < order.getQty()) {
            throw new Exception("Insufficient quantity in inventory");
        }

        // Create order
        order.setTotalPrice(order.getQty() * item.getPrice());
        Order createdOrder = orderRepository.save(order);

        // Update inventory
        inventory.setQty(inventory.getQty() - order.getQty());
        inventoryRepository.save(inventory);

        return createdOrder;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).getContent();
    }

    @Transactional
    public Order updateOrder(Long id, Order newOrder) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setQty(newOrder.getQty());
            existingOrder.setTotalPrice(newOrder.getTotalPrice());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
