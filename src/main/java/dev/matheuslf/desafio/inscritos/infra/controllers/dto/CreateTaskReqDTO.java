package dev.matheuslf.desafio.inscritos.infra.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTaskReqDTO(
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
        @Schema(description = "Title of the task", example = "Implement new feature", required = true)
        String title,

        @Schema(description = "Description of the task", example = "Implement the new user registration flow")
        String description,

        @Schema(description = "Status of the task", example = "TODO")
        TaskStatusEnum status,

        @Schema(description = "Priority of the task", example = "HIGH")
        TaskPriorityEnum priority,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("dueDate")
        @Schema(
                type = "string",
                pattern = "dd-MM-yyyy",
                example = "31-01-2025",
                description = "Date in dd-MM-yyyy format (e.g., 31-01-2025)"
        )
        LocalDate dueDate,

        @NotNull(message = "Project ID is required")
        @Schema(description = "ID of the project this task belongs to", example = "1")
        Long projectId
) {
}