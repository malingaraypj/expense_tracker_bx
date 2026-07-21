package org.example.expense_tracker_bx.service;


import org.example.expense_tracker_bx.dto.request.CategoryRequestDTO;
import org.example.expense_tracker_bx.dto.response.CategoryResponseDTO;
import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO request, Long userId);
    List<CategoryResponseDTO> getAvailableCategories(Long userId);
    CategoryResponseDTO getCategoryById(Long id, Long userId);
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request, Long userId);
    void deleteCategory(Long id, Long userId);
}