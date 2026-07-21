package org.example.expense_tracker_bx.repository;


import org.example.expense_tracker_bx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Used by Spring Security's UserDetailsService to load user authentication details
     * during JWT validation, and by AuthService during login.
     *
     * Generates SQL: SELECT * FROM users WHERE email = ?1;
     */
    Optional<User> findByEmail(String email);

    /**
     * Used by AuthService during user registration to check if an account with
     * this email already exists, preventing duplicate registration errors.
     *
     * Generates SQL: SELECT COUNT(*) > 0 FROM users WHERE email = ?1;
     */
    boolean existsByEmail(String email);
}