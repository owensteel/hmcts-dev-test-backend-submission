package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;

import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;
import uk.gov.hmcts.reform.dev.tasks.TaskService;
import uk.gov.hmcts.reform.dev.tasks.web.dto.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskResponse;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskUpdateRequest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnCreatedTask() {
        CreateTaskRequest req = new CreateTaskRequest(
                "title",
                "desc",
                TaskStatus.PENDING,
                OffsetDateTime.now(),
                1L);
        Task savedTask = new Task();
        savedTask.setId(1L);

        when(service.create(any(Task.class))).thenReturn(savedTask);

        ResponseEntity<TaskResponse> response = controller.create(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.SC_CREATED));
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/api/tasks/1");
        assertThat(response.getBody().id()).isEqualTo(1L);

        verify(service).create(any(Task.class));
    }

    @Test
    void byId_shouldReturnTaskResponse() {
        Task task = new Task(
                "title",
                "desc",
                TaskStatus.PENDING,
                OffsetDateTime.now(),
                123L);
        task.setId(2L);
        when(service.get(2L)).thenReturn(task);

        TaskResponse result = controller.byId(2L);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.title()).isEqualTo("title");

        verify(service).get(2L);
    }

    @Test
    void all_shouldReturnListOfTaskResponses() {
        Task task1 = new Task(
                "t1",
                "d1",
                TaskStatus.PENDING,
                OffsetDateTime.now(),
                1L);
        task1.setId(10L);
        Task task2 = new Task(
                "t2",
                "d2",
                TaskStatus.DONE,
                OffsetDateTime.now(),
                1L);
        task2.setId(11L);

        when(service.all()).thenReturn(List.of(task1, task2));

        List<TaskResponse> results = controller.all();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).title()).isEqualTo("t1");
        verify(service).all();
    }

    @Test
    void delete_shouldCallServiceAndReturnNoContent() {
        ResponseEntity<Void> response = controller.delete(5L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.SC_NO_CONTENT));
        verify(service).delete(5L);
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() {

        String newTitle = "new title";

        TaskUpdateRequest updateReq = new TaskUpdateRequest();
        updateReq.setTitle(newTitle);
        Task updated = new Task(
                newTitle,
                "desc",
                TaskStatus.IN_PROGRESS,
                updateReq.getDueDateTime(),
                3L);
        updated.setId(7L);

        when(service.updateTask(eq(7L), any(TaskUpdateRequest.class))).thenReturn(updated);

        ResponseEntity<Task> response = controller.updateTask(7L, updateReq);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.SC_OK));
        assertThat(response.getBody()).isExactlyInstanceOf(Task.class);
        assertThat(response.getBody().getTitle()).isEqualTo("new title");
        verify(service).updateTask(7L, updateReq);
    }
}
