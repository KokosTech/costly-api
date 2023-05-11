package com.costly.costly.repository;

import com.costly.costly.response.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends org.springframework.data.jpa.repository.JpaRepository<com.costly.costly.model.Category, Long> {
    List<Category> findCategoriesByOwnerId(Long id);
}