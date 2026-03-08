package com.omeralkan.product.controller;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring Boot'a "Burası dışarıya açılan bir API gişesidir" diyoruz
@RequestMapping("/api/v1/categories") // Sektör standardı URL yapısı (Kaan abi bunu sever)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto requestDto) {
        // Kayıt başarılıysa 201 CREATED durum koduyla döneriz (Tam kurumsal hareket)
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        // Silme işlemi (Soft delete) başarılıysa 204 NO CONTENT döneriz
        return ResponseEntity.noContent().build();
    }
}