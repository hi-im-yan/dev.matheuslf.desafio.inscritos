package dev.matheuslf.desafio.inscritos.infra.repositories.jparepository;

import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findByProjectId(Long projectId);
    
    Optional<TaskEntity> findByIdAndProjectId(Long id, Long projectId);

}
