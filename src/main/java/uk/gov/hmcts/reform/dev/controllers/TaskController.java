package uk.gov.hmcts.reform.dev.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskService;
import uk.gov.hmcts.reform.dev.tasks.mapper.TaskMapper;
import uk.gov.hmcts.reform.dev.tasks.web.dto.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskResponse;
import uk.gov.hmcts.reform.dev.tasks.web.dto.UpdateStatusRequest;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Validated @RequestBody CreateTaskRequest req) {
        Task saved = service.create(
                new Task(req.title(), req.description(), req.status(), req.dueDateTime()));
        return ResponseEntity.created(URI.create(("/api/tasks/" + saved.getId()))).body(TaskMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public TaskResponse byId(@PathVariable long id) {
        return TaskMapper.toResponse(service.get(id));
    }

    @GetMapping
    public List<TaskResponse> all() {
        return service.all().stream().map(TaskMapper::toResponse).toList();
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable long id, @Validated @RequestBody UpdateStatusRequest body) {
        return TaskMapper.toResponse(service.updateStatus(id, body.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
