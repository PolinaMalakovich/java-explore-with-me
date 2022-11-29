package ru.practicum.explorewithme.dto.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor = @__({@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)}))
public class NewCategoryDto {
    @NotBlank
    String name;
}
