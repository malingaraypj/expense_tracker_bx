package org.example.expense_tracker_bx.service.impl;


import org.example.expense_tracker_bx.dto.request.CategoryRequestDTO;
import org.example.expense_tracker_bx.dto.response.CategoryResponseDTO;
import org.example.expense_tracker_bx.entity.Category;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.repository.CategoryRepository;
import org.example.expense_tracker_bx.repository.TransactionRepository;
import org.example.expense_tracker_bx.repository.UserRepository;
import org.example.expense_tracker_bx.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponseDTO createCategory(CategoryRequestDTO request, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .icon(request.getIcon() != null ? request.getIcon() : "Folder")
                .color(request.getColor() != null ? request.getColor() : "#64748b")
                .isDefault(false) // User-created categories are NEVER system defaults
                .user(user)
                .build();

        return mapToDTO(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAvailableCategories(Long userId) {
        return categoryRepo.findAllAvailableForUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id, Long userId) {
        Category category = categoryRepo.findAvailableByIdAndUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
        return mapToDTO(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request, Long userId) {
        // Must use findByIdAndUserIdAndIsDefaultFalse to prevent modifying global defaults!
        Category category = categoryRepo.findByIdAndUserIdAndIsDefaultFalse(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or you cannot edit a system default category"));

        category.setName(request.getName());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());

        return mapToDTO(categoryRepo.save(category));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepo.findByIdAndUserIdAndIsDefaultFalse(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or you cannot delete a system default category"));

        // Check if any transactions are using this category before deleting
        if (transactionRepo.existsByCategoryId(id)) {
            throw new RuntimeException("Cannot delete category: It is currently used by existing transactions.");
        }

        categoryRepo.delete(category);
    }

    private CategoryResponseDTO mapToDTO(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .type(c.getType())
                .icon(c.getIcon())
                .color(c.getColor())
                .isDefault(c.getIsDefault())
                .build();
    }
}