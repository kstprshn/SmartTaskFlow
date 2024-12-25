package ru.java.teamProject.SmartTaskFlow.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class BoardDTO {
    private Long id;
    private String name;
    private Boolean archived;
    private List<Long> members;
    private String description;
}