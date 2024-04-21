package dto.taskInProject.view;

public record TaskInProjectViewDto(String idTaskInProject,
                                   String task,
                                   String project,
                                   String dateOfAddition) {
}
