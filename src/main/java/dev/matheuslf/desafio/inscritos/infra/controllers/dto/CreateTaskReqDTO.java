package dev.matheuslf.desafio.inscritos.infra.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTaskReqDTO(
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
        String title,

        String description,

        TaskStatusEnum status,

        TaskPriorityEnum priority,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("dueDate")
        LocalDate dueDate,

        @NotNull(message = "Project ID is required")
        Long projectId
) {
}
