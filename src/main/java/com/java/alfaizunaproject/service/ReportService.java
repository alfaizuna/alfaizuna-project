package com.java.alfaizunaproject.service;

import com.java.alfaizunaproject.dto.ItemStatisticsReportDTO;
import com.java.alfaizunaproject.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<ItemStatisticsReportDTO> getItemsReport() {
        return orderRepository.getItemStatistic();
    }
}
