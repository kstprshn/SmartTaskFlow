package ru.java.teamProject.SmartTaskFlow.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommentUpdateDto {

    @NotEmpty
    @Size(min = 25, max = 500)
    String newContent;
}
