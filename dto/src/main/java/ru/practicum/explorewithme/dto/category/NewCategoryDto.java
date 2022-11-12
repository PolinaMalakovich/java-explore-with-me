package ru.practicum.explorewithme.dto.category;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class NewCategoryDto {
    @NotBlank
    String name;
}
