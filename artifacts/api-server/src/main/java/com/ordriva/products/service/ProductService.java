package com.ordriva.products.service;

import com.ordriva.common.api.ConflictException;
import com.ordriva.common.api.ResourceNotFoundException;
import com.ordriva.products.api.ProductDtos.ProductRequest;
import com.ordriva.products.api.ProductDtos.ProductResponse;
import com.ordriva.products.domain.Product;
import com.ordriva.products.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> list(String search, Boolean active, Pageable pageable) {
        return repository.search(normalize(search), active, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        repository.findBySkuIgnoreCase(request.sku()).ifPresent(existing -> {
            throw new ConflictException("A product with SKU " + request.sku() + " already exists");
        });
        return toResponse(repository.save(new Product(
                request.sku(), request.name(), request.description(), request.price(), request.active()
        )));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = find(id);
        repository.findBySkuIgnoreCase(request.sku()).filter(existing -> !existing.getId().equals(id)).ifPresent(existing -> {
            throw new ConflictException("A product with SKU " + request.sku() + " already exists");
        });
        product.update(request.sku(), request.name(), request.description(), request.price(), request.active());
        return toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product " + id + " was not found");
        }
        repository.deleteById(id);
    }

    private Product find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " was not found"));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getSku(), p.getName(), p.getDescription(), p.getPrice(), p.isActive(), p.getCreatedAt(), p.getUpdatedAt());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}