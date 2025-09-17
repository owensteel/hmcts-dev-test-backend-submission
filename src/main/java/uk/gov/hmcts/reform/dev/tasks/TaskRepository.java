package uk.gov.hmcts.reform.dev.tasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCaseId(Long caseId);
}