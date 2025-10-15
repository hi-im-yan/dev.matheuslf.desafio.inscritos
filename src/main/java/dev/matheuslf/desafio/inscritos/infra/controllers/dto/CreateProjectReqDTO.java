package dev.matheuslf.desafio.inscritos.infra.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateProjectReqDTO(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,

    String description,

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate,

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endDate
) {
    public Project toDomain() {
        return new Project(
            name,
            description,
            startDate,
            endDate
        );
    }
}
