package com.costly.costly.service;

import com.costly.costly.repository.CategoryRepository;
import com.costly.costly.repository.UserRepository;
import com.costly.costly.response.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // GET CATEGORIES
    public List<Category> getCategories(Long id) {
        try {
            return categoryRepository.findCategoriesByOwnerId(id);
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                return null;
            }
            throw e;
        }
    }

    // POST CATEGORY
    public Category createCategory(com.costly.costly.request.post.Category newCategory) {
        // check if userId exists - parse long from string
        Long userId = newCategory.getUserId();
        if (!userRepository.existsById(userId)) return null;

        // create new category
        com.costly.costly.model.Category category = new com.costly.costly.model.Category();
        category.setName(newCategory.getName());
        category.setColor(newCategory.getColor());
        category.setIcon(newCategory.getIcon());
        category.setOwner(
                userRepository.findById(userId).orElse(null)
        );
        // save category
        categoryRepository.save(category);

        // return category
        return Stream.of(category)
                .map(c -> {
                    Category newCategoryResponse = new Category();
                    newCategoryResponse.setId(c.getId());
                    newCategoryResponse.setName(c.getName());
                    newCategoryResponse.setColor(c.getColor());
                    newCategoryResponse.setIcon(c.getIcon());
                    return newCategoryResponse;
                })
                .findFirst()
                .orElse(null);
    }

    // PUT CATEGORY
    public Category updateCategory(com.costly.costly.request.put.Category updatedCategory) {
        // check if category exists
        Long categoryId = updatedCategory.getId();
        if (!categoryRepository.existsById(categoryId)) return null;

        // update category
        com.costly.costly.model.Category category = categoryRepository.findById(categoryId).orElse(null);
        assert category != null;
        category.setName(updatedCategory.getName());
        category.setColor(updatedCategory.getColor());
        category.setIcon(updatedCategory.getIcon());
        // save category
        categoryRepository.save(category);

        // return category
        return Stream.of(category)
                .map(c -> {
                    Category newCategoryResponse = new Category();
                    newCategoryResponse.setId(c.getId());
                    newCategoryResponse.setName(c.getName());
                    newCategoryResponse.setColor(c.getColor());
                    newCategoryResponse.setIcon(c.getIcon());
                    return newCategoryResponse;
                })
                .findFirst()
                .orElse(null);
    }

    // DELETE CATEGORY
    public void deleteCategory(Long id) {
        // change deletedAt to current time
        com.costly.costly.model.Category category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        category.setDeletedAt(new java.util.Date());
        // save category
        categoryRepository.save(category);
    }
}
