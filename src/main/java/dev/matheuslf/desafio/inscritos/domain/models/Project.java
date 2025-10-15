package dev.matheuslf.desafio.inscritos.domain.models;

import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;

import java.time.LocalDate;

public record Project(String name, String description, LocalDate startDate, LocalDate endDate) {
    public Project(String name, String description, LocalDate startDate) {
        this(name, description, startDate, null);
    }

    public Project {
        if (!isNameValid(name)) {
            throw new NotValidException("Name must be between 3 and 100 characters.");
        }
        if (!isStartDateValid(startDate)) {
            throw new NotValidException("Start date must be a valid date.");
        }
        if (!isEndDateValid(startDate, endDate)) {
            throw new NotValidException("End date must be after start date.");
        }

    }

    private Boolean isNameValid(String name) {
        return name != null && name.trim().length() >= 3 && name.length() <= 100;
    }

    private Boolean isStartDateValid(LocalDate startDate) {
        // There is not a rule that says the start date must be after today
        return startDate != null;
    }

    private Boolean isEndDateValid(LocalDate startDate, LocalDate endDate) {
        return endDate == null || !endDate.isBefore(startDate);
    }
}
