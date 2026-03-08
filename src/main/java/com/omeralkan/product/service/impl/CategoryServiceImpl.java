package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // SONARQUBE ÇÖZÜMÜ 1: Sabit mesajı en tepeye tanımladık
    private static final String CATEGORY_NOT_FOUND_MSG = "Kategori bulunamadı. ID: ";

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());

        CategoryEntity savedEntity = categoryRepository.save(entity);
        return mapToResponse(savedEntity);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND_MSG + id)); // Sabit değişkeni kullandık
        return mapToResponse(entity);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAllByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList(); // SONARQUBE ÇÖZÜMÜ 2: Yeni nesil toList() kullandık
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND_MSG + id)); // Sabit değişkeni kullandık

        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());

        CategoryEntity updatedEntity = categoryRepository.save(entity);
        return mapToResponse(updatedEntity);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND_MSG + id)); // Sabit değişkeni kullandık

        entity.setIsActive(false);
        categoryRepository.save(entity);
    }

    private CategoryResponseDto mapToResponse(CategoryEntity entity) {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setDescription(entity.getDescription());
        responseDto.setIsActive(entity.getIsActive());
        return responseDto;
    }
}