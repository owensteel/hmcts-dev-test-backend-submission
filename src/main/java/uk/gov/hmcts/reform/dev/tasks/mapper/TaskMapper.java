package uk.gov.hmcts.reform.dev.tasks.mapper;

import uk.gov.hmcts.reform.dev.tasks.Task;
import uk.gov.hmcts.reform.dev.tasks.web.dto.TaskResponse;

public final class TaskMapper {
    private TaskMapper() {
    }

    public static TaskResponse toResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getDueDateTime());
    }
}
