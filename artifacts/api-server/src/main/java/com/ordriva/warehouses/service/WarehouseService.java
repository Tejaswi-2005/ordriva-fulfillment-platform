package com.ordriva.warehouses.service;

import com.ordriva.common.api.ConflictException;
import com.ordriva.common.api.ResourceNotFoundException;
import com.ordriva.warehouses.api.WarehouseDtos.WarehouseRequest;
import com.ordriva.warehouses.api.WarehouseDtos.WarehouseResponse;
import com.ordriva.warehouses.domain.Warehouse;
import com.ordriva.warehouses.repository.WarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseService {
    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<WarehouseResponse> list(String search, Boolean active, Pageable pageable) {
        return repository.search(normalize(search), active, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public WarehouseResponse get(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public WarehouseResponse create(WarehouseRequest request) {
        return toResponse(repository.save(new Warehouse(
                request.name(), request.code(), request.city(), request.state(), request.country(), request.active()
        )));
    }

    @Transactional
    public WarehouseResponse update(Long id, WarehouseRequest request) {
        Warehouse warehouse = find(id);
        warehouse.update(request.name(), request.code(), request.city(), request.state(), request.country(), request.active());
        return toResponse(warehouse);
    }

    private Warehouse find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse " + id + " was not found"));
    }

    private WarehouseResponse toResponse(Warehouse w) {
        return new WarehouseResponse(w.getId(), w.getName(), w.getCode(), w.getCity(), w.getState(), w.getCountry(), w.isActive());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}