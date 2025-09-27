package uk.gov.hmcts.reform.dev.tasks;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.PENDING;

    @NotNull
    @Column(name = "due_at", nullable = false)
    private OffsetDateTime dueDateTime;

    @NotNull
    @Column(name = "case_id", nullable = false)
    private Long caseId;

    public Task() {
    }

    public Task(String title, String description, TaskStatus status, OffsetDateTime dueDateTime, Long caseId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDateTime = dueDateTime;
        this.caseId = caseId;
    }

    public Long setId() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String v) {
        title = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus v) {
        status = v;
    }

    public OffsetDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(OffsetDateTime v) {
        dueDateTime = v;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long v) {
        caseId = v;
    }
}
