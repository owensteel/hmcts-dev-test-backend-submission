package uk.gov.hmcts.reform.dev.tasks.web.dto;

import java.time.OffsetDateTime;

import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record TaskResponse(Long id, String title, String description, TaskStatus status, OffsetDateTime dueDateTime) {
}
