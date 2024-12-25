package ru.java.teamProject.SmartTaskFlow.dto.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter @Getter
@Accessors(chain = true)
public class CommentResponse {
    Long id;
    String content;
    String authorUsername;
    Long taskId;
    LocalDateTime createdAt;
}
