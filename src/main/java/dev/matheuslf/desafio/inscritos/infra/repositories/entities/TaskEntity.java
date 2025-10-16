package dev.matheuslf.desafio.inscritos.infra.repositories.entities;

import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 150)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TaskStatusEnum status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TaskPriorityEnum priority;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

}
