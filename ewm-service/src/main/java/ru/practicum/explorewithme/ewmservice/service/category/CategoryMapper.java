package ru.practicum.explorewithme.ewmservice.service.category;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.ewmservice.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(final Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(final CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static Category toCategory(final NewCategoryDto newCategoryDto) {
        return new Category(null, newCategoryDto.getName());
    }

    public static List<CategoryDto> toCategoryDtoList(final List<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}
