package dev.matheuslf.desafio.inscritos.application.usecases.dto;

import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;

public record UpdateTaskStatusInput(
    Long taskId,
    TaskStatusEnum status
) {}
