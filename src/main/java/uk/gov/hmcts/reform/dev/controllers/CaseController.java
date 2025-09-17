package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskService;

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
    public List<Task> getTasksForCase(@PathVariable Long caseId) {
        return taskService.getTasksForCase(caseId);
    }
}
