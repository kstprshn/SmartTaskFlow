package ru.java.teamProject.SmartTaskFlow.dto.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter @Getter
@Accessors(chain = true)
public class BoardPreviewDto {

    @NotNull
    @Size(min = 3, max = 30)
    private String name;
}
