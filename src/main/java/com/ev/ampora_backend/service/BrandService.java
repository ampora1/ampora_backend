package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.BrandDTO;
import com.ev.ampora_backend.entity.Brand;
import com.ev.ampora_backend.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service

public class BrandService {
    private BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> getAllBrands() {

        return  brandRepository.findAll().stream()
                .map(
                        brand -> BrandDTO.builder().
                                brand_id(brand.getId())
                                .brand_name(brand.getName())
                                .build()
                ).toList();
    }

    public BrandDTO addBrand(BrandDTO brandDTO) {
        Brand brand = new Brand();
        brand.setName(brandDTO.getBrand_name());
        brandRepository.save(brand);
        return BrandDTO.builder().brand_id(brand.getId()).brand_name(brand.getName()).build();
    }
}
