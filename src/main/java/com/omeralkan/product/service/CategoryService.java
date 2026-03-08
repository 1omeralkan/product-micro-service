package com.omeralkan.product.service;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto requestDto);
    CategoryResponseDto getCategoryById(Long id);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto);
    void deleteCategory(Long id);
}