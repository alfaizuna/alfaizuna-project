package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.model.Inventory;
import com.java.alfaizunaproject.model.Item;
import com.java.alfaizunaproject.model.Order;
import com.java.alfaizunaproject.repository.InventoryRepository;
import com.java.alfaizunaproject.repository.ItemRepository;
import com.java.alfaizunaproject.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder() throws Exception {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Inventory inventory = new Inventory(1L, 10);
        Order order = new Order(item, 5, 50.0);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(inventoryRepository.findByItemId(anyLong())).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Order createdOrder = orderService.createOrder(order);
        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getItem().getId());
        assertEquals(5, createdOrder.getQty());
        assertEquals(50.0, createdOrder.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(inventoryRepository, times(1)).findByItemId(anyLong());
    }

    @Test
    public void testCreateOrderInsufficientInventory() {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Inventory inventory = new Inventory(1L, 3);
        Order order = new Order(item, 5, 50.0);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(inventoryRepository.findByItemId(anyLong())).thenReturn(Optional.of(inventory));

        Exception exception = assertThrows(Exception.class, () -> {
            orderService.createOrder(order);
        });

        assertEquals("Insufficient quantity in inventory", exception.getMessage());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testGetOrder() {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Order order = new Order(item, 5, 50.0);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrder(1L);
        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getItem().getId());
        assertEquals(5, foundOrder.getQty());
        assertEquals(50.0, foundOrder.getTotalPrice());
    }

    @Test
    public void testGetAllOrders() {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Order order = new Order(item, 5, 50.0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = new PageImpl<>(Arrays.asList(order));

        when(orderRepository.findAll(pageable)).thenReturn(page);

        List<Order> orders = orderService.getAllOrders(0, 10);
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getItem().getId());
        assertEquals(5, orders.get(0).getQty());
        assertEquals(50.0, orders.get(0).getTotalPrice());
    }

    @Test
    public void testUpdateOrder() {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Order existingOrder = new Order(item, 5, 50.0);
        Order newOrder = new Order(item, 10, 100.0);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder);

        Order updatedOrder = orderService.updateOrder(1L, newOrder);
        assertNotNull(updatedOrder);
        assertEquals(10, updatedOrder.getQty());
        assertEquals(100.0, updatedOrder.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testDeleteOrder() {
        Item item = new Item("Test Item", 10.0);
        item.setId(1L);
        Order order = new Order(item, 5, 50.0);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
}
