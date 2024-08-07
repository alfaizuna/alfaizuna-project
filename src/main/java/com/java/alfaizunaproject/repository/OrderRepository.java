package com.java.alfaizunaproject.repository;

import com.java.alfaizunaproject.dto.ItemStatisticsReportDTO;
import com.java.alfaizunaproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.item.id AS itemId, COUNT(o) AS totalOrders, SUM(o.qty) AS totalQty, SUM(o.totalPrice) AS totalPrice " +
            "FROM Order o " +
            "GROUP BY o.item.id")
    List<ItemStatisticsReportDTO> getItemStatistic();
}
