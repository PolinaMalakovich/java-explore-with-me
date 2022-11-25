package ru.practicum.explorewithme.dto.category;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class CategoryDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
}
