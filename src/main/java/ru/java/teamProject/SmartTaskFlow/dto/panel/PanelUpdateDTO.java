package ru.java.teamProject.SmartTaskFlow.dto.panel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class PanelUpdateDTO {

    @NotBlank
    private String name;

}
