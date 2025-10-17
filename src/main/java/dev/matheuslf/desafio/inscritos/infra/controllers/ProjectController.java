package dev.matheuslf.desafio.inscritos.infra.controllers;

import dev.matheuslf.desafio.inscritos.application.usecases.CreateProjectUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.ListProjectsUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateProjectInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.infra.controllers.dto.CreateProjectReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/projects",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Projects", description = "Endpoints for managing projects")
@RequiredArgsConstructor
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;

    @Operation(
        summary = "Create a new project",
        description = "Creates a new project with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Project created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectOutput.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input"
        )
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjectOutput> createProject(
            @Valid @RequestBody CreateProjectReqDTO request) {
        CreateProjectInput useCaseInput = new CreateProjectInput(
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate()
        );
        ProjectOutput project = createProjectUseCase.execute(useCaseInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @Operation(
        summary = "List all projects",
        description = "Retrieves a list of all projects in the system"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved list of projects",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ProjectOutput.class))
        )
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectOutput>> listProjects() {
        List<ProjectOutput> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(projects);
    }

}
