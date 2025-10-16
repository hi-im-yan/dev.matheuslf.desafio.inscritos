package dev.matheuslf.desafio.inscritos.application.usecases.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;

import java.time.LocalDate;

public record CreateTaskInput(
    String title,
    String description,
    TaskStatusEnum status,
    TaskPriorityEnum priority,
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("dueDate")
    LocalDate dueDate,
    
    Long projectId
) {}
