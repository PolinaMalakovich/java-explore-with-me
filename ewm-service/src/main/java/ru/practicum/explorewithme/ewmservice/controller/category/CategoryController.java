package ru.practicum.explorewithme.ewmservice.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.ewmservice.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                           @RequestParam(defaultValue = "10") @Positive final int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategory(@PathVariable @PositiveOrZero final long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}
