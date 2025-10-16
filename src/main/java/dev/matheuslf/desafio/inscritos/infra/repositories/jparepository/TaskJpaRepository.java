package dev.matheuslf.desafio.inscritos.infra.repositories.jparepository;

import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByProjectId(Long projectId);

    @Query("""
                SELECT t FROM TaskEntity t
                WHERE
                    (:status IS NULL OR t.status = :status)
                    AND (:priority IS NULL OR t.priority = :priority)
                    AND (:projectId IS NULL OR t.project.id = :projectId)
            """)
    List<TaskEntity> findByStatusAndPriorityAndProjectId(
            @Param("status") TaskStatusEnum status,
            @Param("priority") TaskPriorityEnum priority,
            @Param("projectId") Long projectId
    );
}
