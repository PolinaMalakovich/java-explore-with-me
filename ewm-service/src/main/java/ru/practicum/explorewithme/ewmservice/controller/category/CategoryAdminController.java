package ru.practicum.explorewithme.ewmservice.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.ewmservice.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody final CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody final NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable @PositiveOrZero final long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
