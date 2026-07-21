package org.example.expense_tracker_bx.controller;


import org.example.expense_tracker_bx.dto.request.CategoryRequestDTO;
import org.example.expense_tracker_bx.dto.response.CategoryResponseDTO;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {
        return new ResponseEntity<>(categoryService.createCategory(request, authenticatedUser.getId()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAvailableCategories(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(categoryService.getAvailableCategories(authenticatedUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(categoryService.getCategoryById(id, authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request, authenticatedUser.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        categoryService.deleteCategory(id, authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }
}