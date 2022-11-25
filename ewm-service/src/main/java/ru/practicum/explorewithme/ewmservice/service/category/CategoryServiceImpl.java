package ru.practicum.explorewithme.ewmservice.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.model.Category;
import ru.practicum.explorewithme.ewmservice.repository.CategoryRepository;

import java.util.List;

import static ru.practicum.explorewithme.ewmservice.service.category.CategoryMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(final int from, final int size) {
        final List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).toList();

        return toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategory(final long categoryId) {
        final Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category", categoryId));

        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(final CategoryDto categoryDto) {
        final Category category = categoryRepository.findById(categoryDto.getId())
            .orElseThrow(IllegalArgumentException::new);
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        log.info("Category " + category.getId() + " updated successfully.");

        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto addCategory(final NewCategoryDto newCategoryDto) {
        final Category category = toCategory(newCategoryDto);
        final Category newCategory = categoryRepository.save(category);
        log.info("New category created successfully.");

        return toCategoryDto(newCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(final long categoryId) {
        categoryRepository.deleteById(categoryId);
        log.info("Category " + categoryId + " deleted successfully.");
    }
}
