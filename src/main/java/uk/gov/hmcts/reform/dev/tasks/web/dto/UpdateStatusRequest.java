package uk.gov.hmcts.reform.dev.tasks.web.dto;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record UpdateStatusRequest(@NotNull TaskStatus status) {
}
