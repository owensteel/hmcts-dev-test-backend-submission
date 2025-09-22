package uk.gov.hmcts.reform.dev.tasks;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public Task create(Task t) {
        return repo.save(t);
    }

    public Task get(Long id) {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> all() {
        return repo.findAll();
    }

    public List<Task> getTasksForCase(Long caseId) {
        return repo.findByCaseId(caseId);
    }

    public Task createTaskForCase(Long caseId, Task newTask) {
        // TODO: check if case actually exists
        newTask.setCaseId(caseId);
        return create(newTask);
    }

    @Transactional
    public Task updateStatus(long id, TaskStatus status) {
        Task t = get(id);
        t.setStatus(status);
        return t; // JPA dirty checking persists
    }

    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repo.deleteById(id);
    }
}
