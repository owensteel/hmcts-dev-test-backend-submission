package uk.gov.hmcts.reform.dev.tasks.web.dto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

public record UpdateStatusRequest(@NotNull TaskStatus status) {
}
