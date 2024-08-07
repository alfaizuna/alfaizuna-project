package com.java.alfaizunaproject.repository;

import com.java.alfaizunaproject.model.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
}
