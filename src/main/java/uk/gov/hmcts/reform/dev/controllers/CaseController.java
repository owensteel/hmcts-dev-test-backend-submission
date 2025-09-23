package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskService;
import uk.gov.hmcts.reform.dev.tasks.mapper.TaskMapper;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskResponse;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/cases")
public class CaseController {
    private final TaskService taskService;

    public CaseController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/get-example-case")
    public ResponseEntity<ExampleCase> getExampleCase() {
        return ok(
                new ExampleCase(
                        1,
                        "ABC12345",
                        "Case Title",
                        "Case Description",
                        "Case Status",
                        LocalDateTime.now()));
    }

    @GetMapping("/{caseId}/tasks")
    public Page<Task> getTasks(
            @PathVariable Long caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dueDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return taskService.getTasksByCaseId(caseId, PageRequest.of(page, size, sort));
    }

    @PostMapping("/{caseId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long caseId, @RequestBody Task newTask) {
        Task task = taskService.createTaskForCase(caseId, newTask);
        return ResponseEntity.created(URI.create(("/api/tasks/" + task.getId()))).body(TaskMapper.toResponse(task));
    }
}
