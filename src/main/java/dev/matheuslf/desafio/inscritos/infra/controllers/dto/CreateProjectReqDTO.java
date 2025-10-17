package dev.matheuslf.desafio.inscritos.infra.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request DTO for creating a new project")
public record CreateProjectReqDTO(

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Schema(
            description = "Name of the project",
            example = "E-commerce Platform",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String name,

    @Schema(
        description = "Detailed description of the project",
        example = "A complete e-commerce solution with payment integration",
        nullable = true
    )
    String description,

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Schema(
            type = "string",
            pattern = "dd-MM-yyyy",
            example = "31-01-2025",
            description = "Project start date in dd-MM-yyyy format"
    )
    LocalDate startDate,

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Schema(
            type = "string",
            pattern = "dd-MM-yyyy",
            example = "31-01-2026",
            description = "Project end date in dd-MM-yyyy format"
    )
    LocalDate endDate
) {
}
