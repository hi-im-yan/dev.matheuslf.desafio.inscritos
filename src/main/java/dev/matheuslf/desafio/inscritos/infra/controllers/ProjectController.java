package dev.matheuslf.desafio.inscritos.infra.controllers;

import dev.matheuslf.desafio.inscritos.application.usecases.CreateProjectUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.ListProjectsUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateProjectInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.infra.controllers.dto.CreateProjectReqDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;

    @PostMapping
    public ResponseEntity<ProjectOutput> createProject(@Valid @RequestBody CreateProjectReqDTO request) {
        CreateProjectInput useCaseInput = new CreateProjectInput(
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate()
        );
        ProjectOutput project = createProjectUseCase.execute(useCaseInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectOutput>> listProjects() {
        List<ProjectOutput> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(projects);
    }



}
