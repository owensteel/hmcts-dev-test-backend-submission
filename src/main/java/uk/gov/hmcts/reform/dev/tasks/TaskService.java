package uk.gov.hmcts.reform.dev.tasks;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskUpdateRequest;

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

    public Page<Task> getTasksByCaseId(Long caseId, Pageable pageable) {
        return repo.findByCaseId(caseId, pageable);
    }

    public Page<Task> getTasksByCaseIdAndStatus(Long caseId, TaskStatus status, Pageable pageable) {
        return repo.findByCaseIdAndStatus(caseId, status, pageable);
    }

    public Task createTaskForCase(Long caseId, Task newTask) {
        newTask.setCaseId(caseId);
        return create(newTask);
    }

    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repo.deleteById(id);
    }

    @Transactional
    public Task updateTask(Long taskId, TaskUpdateRequest updateRequest) {
        if (!repo.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }

        Task task = repo.getReferenceById(taskId);

        if (updateRequest.getTitle() != null) {
            task.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            task.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getStatus() != null) {
            task.setStatus(updateRequest.getStatus());
        }
        if (updateRequest.getDueDateTime() != null) {
            task.setDueDateTime(updateRequest.getDueDateTime());
        }

        return repo.save(task);
    }
}
