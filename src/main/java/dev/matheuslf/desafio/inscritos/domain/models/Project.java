package dev.matheuslf.desafio.inscritos.domain.models;

import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Project {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public Project(String name, String description, LocalDate startDate) {
        if (!isNameValid(name)) {
            throw new NotValidException("Name must be between 3 and 100 characters.");
        }
        if (!isStartDateValid(startDate)) {
            throw new NotValidException("Start date must be a valid date.");
        }

        this.name = name;
        this.description = description;
        this.startDate = startDate;
    }

    private Boolean isNameValid(String name) {
        return name != null && name.trim().length() >= 3 && name.length() <= 100;
    }

    private Boolean isStartDateValid(LocalDate startDate) {
        // There is not a rule that says the start date must be after today
        return startDate != null;
    }

    private Boolean isEndDateValid(LocalDate endDate) {
        if (endDate == null) {
            return true;
        }
        return this.startDate != null && endDate.isBefore(this.startDate);
    }

}
