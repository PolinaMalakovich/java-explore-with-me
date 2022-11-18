package ru.practicum.explorewithme.ewmservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.ewmservice.service.category.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") final int from,
                                           @RequestParam(required = false, defaultValue = "10") final int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@PathVariable final long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@RequestBody final CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCategory(@RequestBody final NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public void deleteCategory(@PathVariable final long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
