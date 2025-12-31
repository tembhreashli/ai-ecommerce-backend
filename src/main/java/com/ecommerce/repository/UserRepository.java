package com.ecommerce.repository;

import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom query methods for User management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
