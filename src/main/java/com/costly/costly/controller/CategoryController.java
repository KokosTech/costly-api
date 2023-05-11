package com.costly.costly.controller;

import com.costly.costly.service.CategoryService;
import com.costly.costly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories/")
public class CategoryController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}/")
    public java.util.List<com.costly.costly.response.Category> getCategories(@PathVariable String id) {
        System.out.println("USER ID = " + id);
        return categoryService.getCategories(Long.parseLong(id));
    }

    @PostMapping("/")
    public com.costly.costly.response.Category createCategory(@RequestBody com.costly.costly.request.post.Category newCategory) {
        System.out.println("NEW CATEGORY = " + newCategory);
        return categoryService.createCategory(newCategory);
    }

    @PutMapping("/")
    public com.costly.costly.response.Category updateCategory(@RequestBody com.costly.costly.request.put.Category oldCategory) {
        System.out.println("OLD CATEGORY = " + oldCategory);
        return categoryService.updateCategory(oldCategory);
    }

    @DeleteMapping("/{id}/")
    public void deleteCategory(@PathVariable Long id) {
        System.out.println("DELETE CATEGORY = " + id);
        categoryService.deleteCategory(id);
    }
}
