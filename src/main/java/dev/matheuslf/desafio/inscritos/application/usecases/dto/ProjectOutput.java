package dev.matheuslf.desafio.inscritos.application.usecases.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ProjectOutput(
        Long id,
        String name,
        String description,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("startDate")
        LocalDate startDate,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("endDate")
        LocalDate endDate
) {
    public String formattedStartDate() {
        return startDate != null ? startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null;
    }

    public String formattedEndDate() {
        return endDate != null ? endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null;
    }
}
