package dev.matheuslf.desafio.inscritos.infra.repositories.jparepository;

import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByName(String projectName);
}
