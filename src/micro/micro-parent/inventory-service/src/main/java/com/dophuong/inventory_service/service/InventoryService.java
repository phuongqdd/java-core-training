package com.dophuong.inventory_service.service;

import com.dophuong.inventory_service.entity.Inventory;
import com.dophuong.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    public boolean isInStock(List<String> skuCodes) {
        // Lấy danh sách Inventory có skuCode trong skuCodes
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);

        // true nếu số lượng inventory trả về bằng số SKU cần check (tức tất cả đều có trong kho)
        boolean allInStock = inventories.size() == skuCodes.size();

        log.info("Checking stock for SKUs {}: {}", skuCodes, allInStock);
        return allInStock;
    }
}