package com.java.alfaizunaproject.repository;

import com.java.alfaizunaproject.model.InventoryLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryLogRepositoryTest {

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    @Test
    public void testSaveInventoryLog() {
        InventoryLog log = new InventoryLog("{\"item_id\": 1, \"qty_before\": 10, \"qty_after\": 7, \"type\": \"W\"}");
        InventoryLog savedLog = inventoryLogRepository.save(log);
        assertNotNull(savedLog);
        assertNotNull(savedLog.getId());
        assertEquals("{\"item_id\": 1, \"qty_before\": 10, \"qty_after\": 7, \"type\": \"W\"}", savedLog.getAction());
    }
}
