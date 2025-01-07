package com.example.wydad.services.impl;

import com.example.wydad.entities.Sale;
import com.example.wydad.repositories.SaleRepository;
import com.example.wydad.services.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Optional<Sale> getSaleById(Integer id) {
        return saleRepository.findById(id);
    }

    @Override
    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public void deleteSale(Integer id) {
        saleRepository.deleteById(id);
    }
}