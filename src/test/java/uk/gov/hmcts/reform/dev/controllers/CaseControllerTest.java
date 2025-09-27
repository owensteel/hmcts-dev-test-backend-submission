package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.TaskService;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskResponse;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CaseControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CaseController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExampleCase_shouldReturnHardcodedCase() {
        ResponseEntity<ExampleCase> response = controller.getExampleCase();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        ExampleCase body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1);
        assertThat(body.getCaseNumber()).isEqualTo("ABC12345");
    }

    @Test
    void getTasks_shouldReturnFilteredTasks() {
        Task task1 = new Task("t1", "d1", TaskStatus.PENDING, OffsetDateTime.now(), 42L);
        task1.setId(101L);
        Page<Task> page = new PageImpl<>(List.of(task1));

        when(taskService.getTasksByCaseIdAndStatus(
                eq(42L),
                eq(TaskStatus.PENDING),
                any(PageRequest.class))).thenReturn(page);

        Page<Task> result = controller.getTasks(
                42L,
                0,
                5,
                "dueDateTime",
                "asc",
                TaskStatus.PENDING.toString());

        // Assert the controller forwarded to the correct service method
        verify(taskService).getTasksByCaseIdAndStatus(
                eq(42L),
                eq(TaskStatus.PENDING),
                any(PageRequest.class));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(101L);
    }

    @Test
    void getTasks_shouldFallbackToAllTasks_whenInvalidStatus() {
        Task task1 = new Task("t1", "d1", TaskStatus.PENDING, OffsetDateTime.now(), 99L);
        task1.setId(202L);
        Page<Task> page = new PageImpl<>(List.of(task1));

        when(taskService.getTasksByCaseId(eq(42L), any(PageRequest.class))).thenReturn(page);

        Page<Task> result = controller.getTasks(42L, 0, 5, "dueDateTime", "asc", "INVALID_STATUS");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(202L);
    }

    @Test
    void createTask_shouldReturnCreatedTaskResponse() {
        Task newTask = new Task("new", "desc", TaskStatus.IN_PROGRESS, OffsetDateTime.now(), 55L);
        newTask.setId(303L);

        when(taskService.createTaskForCase(eq(55L), any(Task.class))).thenReturn(newTask);

        ResponseEntity<TaskResponse> response = controller.createTask(55L, newTask);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/api/tasks/303");
        assertThat(response.getBody().id()).isEqualTo(303L);
    }

    @Test
    void getTasks_shouldPassCorrectSortAscending() {
        Task task = new Task("t", "d", TaskStatus.PENDING, OffsetDateTime.now(), 42L);
        task.setId(1L);
        Page<Task> page = new PageImpl<>(List.of(task));

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);

        when(taskService.getTasksByCaseIdAndStatus(eq(10L), eq(TaskStatus.PENDING), any(PageRequest.class)))
                .thenReturn(page);

        controller.getTasks(10L, 0, 5, "title", "asc", "PENDING");

        verify(taskService).getTasksByCaseIdAndStatus(eq(10L), eq(TaskStatus.PENDING), captor.capture());

        PageRequest request = captor.getValue();
        assertThat(request.getPageNumber()).isEqualTo(0);
        assertThat(request.getPageSize()).isEqualTo(5);
        assertThat(request.getSort().toString()).isEqualTo("title: ASC");
    }

    @Test
    void getTasks_shouldPassCorrectSortDescending() {
        Task task = new Task("t", "d", TaskStatus.PENDING, OffsetDateTime.now(), 42L);
        task.setId(2L);
        Page<Task> page = new PageImpl<>(List.of(task));

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);

        when(taskService.getTasksByCaseIdAndStatus(eq(20L), eq(TaskStatus.DONE), any(PageRequest.class)))
                .thenReturn(page);

        controller.getTasks(20L, 1, 10, "dueDateTime", "desc", "DONE");

        verify(taskService).getTasksByCaseIdAndStatus(eq(20L), eq(TaskStatus.DONE), captor.capture());

        PageRequest request = captor.getValue();
        assertThat(request.getPageNumber()).isEqualTo(1);
        assertThat(request.getPageSize()).isEqualTo(10);
        assertThat(request.getSort().toString()).isEqualTo("dueDateTime: DESC");
    }
}
