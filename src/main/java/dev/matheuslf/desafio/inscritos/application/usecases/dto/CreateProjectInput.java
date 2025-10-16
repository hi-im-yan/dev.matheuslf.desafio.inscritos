package dev.matheuslf.desafio.inscritos.application.usecases.dto;

import java.time.LocalDate;

public record CreateProjectInput(String name, String description, LocalDate startDate, LocalDate endDate) {
}
