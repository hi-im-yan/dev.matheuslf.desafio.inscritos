package dev.matheuslf.desafio.inscritos.infra.controllers;

import dev.matheuslf.desafio.inscritos.application.usecases.CreateTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.DeleteTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.FilterTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
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
    private final FilterTaskUseCase filterTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase, FilterTaskUseCase filterTaskUseCase, DeleteTaskUseCase deleteTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.filterTaskUseCase = filterTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
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
    public ResponseEntity<List<TaskOutput>> filterTasks(
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(required = false) TaskPriorityEnum priority,
            @RequestParam(required = false) Long projectId) {
        
        List<TaskOutput> tasks = filterTaskUseCase.execute(status, priority, projectId);
        return ResponseEntity.ok(tasks);
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
        deleteTaskUseCase.execute(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
