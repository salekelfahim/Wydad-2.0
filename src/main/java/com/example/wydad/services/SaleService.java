package com.example.wydad.services;

import com.example.wydad.entities.Sale;
import java.util.List;
import java.util.Optional;

public interface SaleService {
    List<Sale> getAllSales();
    Optional<Sale> getSaleById(Integer id);
    Sale saveSale(Sale sale);
    void deleteSale(Integer id);
}