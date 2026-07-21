package org.example.expense_tracker_bx.repository;


import org.example.expense_tracker_bx.entity.Category;
import org.example.expense_tracker_bx.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Fetches BOTH global system defaults AND the specific user's custom categories.
     * This is the primary method used to populate the category dropdowns in React.
     */
    @Query("SELECT c FROM Category c WHERE c.isDefault = true OR c.user.id = :userId")
    List<Category> findAllAvailableForUser(@Param("userId") Long userId);

    /**
     * Same as above, but filtered by CategoryType (INCOME vs EXPENSE).
     * When a user selects "INCOME" in the UI form, use this to hide all EXPENSE categories!
     */
    @Query("SELECT c FROM Category c WHERE (c.isDefault = true OR c.user.id = :userId) AND c.type = :type")
    List<Category> findAllAvailableForUserByType(@Param("userId") Long userId, @Param("type") CategoryType type);

    /**
     * Securely finds a category by ID, ensuring it is either a global default
     * OR owned by the requesting user. Prevents users from using someone else's private category ID.
     */
    @Query("SELECT c FROM Category c WHERE c.id = :id AND (c.isDefault = true OR c.user.id = :userId)")
    Optional<Category> findAvailableByIdAndUser(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Used when deleting or modifying a user-created custom category to verify ownership.
     *
     * Generates SQL: SELECT * FROM categories WHERE category_id = ?1 AND user_id = ?2 AND is_default = false;
     */
    Optional<Category> findByIdAndUserIdAndIsDefaultFalse(Long id, Long userId);
}