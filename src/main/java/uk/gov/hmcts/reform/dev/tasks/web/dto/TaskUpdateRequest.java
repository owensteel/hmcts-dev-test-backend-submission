package uk.gov.hmcts.reform.dev.tasks.web.dto;

import java.time.OffsetDateTime;

import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public class TaskUpdateRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private OffsetDateTime dueDateTime;

    public TaskUpdateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public OffsetDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(OffsetDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
