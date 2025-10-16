package dev.matheuslf.desafio.inscritos.infra.controllers;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.usecases.CreateTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.controllers.dto.CreateTaskReqDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
    }

    @PostMapping
    public ResponseEntity<TaskOutput> createTask(@Valid @RequestBody CreateTaskReqDTO request) {
        CreateTaskInput useCaseInput = new CreateTaskInput(
                request.title(),
                request.description(),
                request.status(),
                request.priority(),
                request.dueDate(),
                request.projectId()
        );
        TaskOutput task = createTaskUseCase.execute(useCaseInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskOutput>> listTasks(
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long projectId) {
        // Implementation will be added once the ListTasksUseCase is available
        return ResponseEntity.ok(List.of());
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskOutput> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatusEnum status) {
        // Implementation will be added once the UpdateTaskStatusUseCase is available
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        // Implementation will be added once the DeleteTaskUseCase is available
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskOutput> getTask(@PathVariable Long taskId) {
        // Implementation will be added once the GetTaskUseCase is available
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
