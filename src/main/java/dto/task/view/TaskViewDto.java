package dto.task.view;


public record TaskViewDto(String idTask,
                                String name,
                                String description,
                                String dateStart,
                                String deadline,
                                String status,
                                String priority) {
}
