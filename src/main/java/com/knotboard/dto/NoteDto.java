package com.knotboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private Long id;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Board id is required")
    private Long boardId;

    private LocalDateTime createdAt;

    private String createdByUsername;

}
