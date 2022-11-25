package ru.practicum.explorewithme.ewmservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.ewmservice.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                           @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@PathVariable @PositiveOrZero final long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@Valid @RequestBody final CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCategory(@Valid @RequestBody final NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public void deleteCategory(@PathVariable @PositiveOrZero final long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
