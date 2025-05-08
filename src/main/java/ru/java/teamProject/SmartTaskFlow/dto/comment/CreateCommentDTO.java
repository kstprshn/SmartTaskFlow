package ru.java.teamProject.SmartTaskFlow.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {

    private Long authorId;

    @NotEmpty
    @Size(min = 25, max = 500)
    private String content;
}
