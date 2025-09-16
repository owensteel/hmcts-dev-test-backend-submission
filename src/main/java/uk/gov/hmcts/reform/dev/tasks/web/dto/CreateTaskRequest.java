package uk.gov.hmcts.reform.dev.tasks.web.dto;

import java.time.OffsetDateTime;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull TaskStatus status,
        @NotNull @FutureOrPresent OffsetDateTime dueDateTime) {
}
