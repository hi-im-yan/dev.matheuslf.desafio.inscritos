package dev.matheuslf.desafio.inscritos.infra.controllers;

import dev.matheuslf.desafio.inscritos.application.usecases.CreateTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.DeleteTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.FilterTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.UpdateTaskStatusUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.UpdateTaskStatusInput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.controllers.dto.CreateTaskReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "Task Management",
        description = "Endpoints for managing tasks including CRUD operations and status updates"
)
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final FilterTaskUseCase filterTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final UpdateTaskStatusUseCase updateTaskStatusUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase,
                          FilterTaskUseCase filterTaskUseCase,
                          DeleteTaskUseCase deleteTaskUseCase,
                          UpdateTaskStatusUseCase updateTaskStatusUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.filterTaskUseCase = filterTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.updateTaskStatusUseCase = updateTaskStatusUseCase;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TaskOutput.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"title\": \"Complete project documentation\",\n" +
                                            "  \"description\": \"Document all endpoints and features\",\n" +
                                            "  \"status\": \"TODO\",\n" +
                                            "  \"priority\": \"HIGH\",\n" +
                                            "  \"dueDate\": \"31-01-2025\",\n" +
                                            "  \"projectId\": 1\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get filtered tasks",
            description = "Retrieves tasks based on optional filters such as status, priority, or project ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved tasks",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskOutput.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content
            )
    })
    public ResponseEntity<List<TaskOutput>> filterTasks(
            @Parameter(description = "Filter tasks by status", example = "TODO")
            @RequestParam(required = false) TaskStatusEnum status,

            @Parameter(description = "Filter tasks by priority", example = "HIGH")
            @RequestParam(required = false) TaskPriorityEnum priority,

            @Parameter(description = "Filter tasks by project ID", example = "1")
            @RequestParam(required = false) Long projectId) {

        List<TaskOutput> tasks = filterTaskUseCase.execute(status, priority, projectId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping(
            value = "/{taskId}/status/{status}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Update task status",
            description = "Updates the status of an existing task"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task status updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TaskOutput.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status or task ID",
                    content = @Content
            )
    })
    public ResponseEntity<TaskOutput> updateTaskStatus(
            @Parameter(description = "ID of the task to update", required = true, example = "1")
            @PathVariable Long taskId,

            @Parameter(
                    description = "New status for the task",
                    required = true,
                    example = "DOING"
            )
            @PathVariable TaskStatusEnum status) {
        UpdateTaskStatusInput input = new UpdateTaskStatusInput(taskId, status);
        TaskOutput updatedTask = updateTaskStatusUseCase.execute(input);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping(
            value = "/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task with the specified ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true, example = "1")
            @PathVariable Long taskId) {
        deleteTaskUseCase.execute(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
