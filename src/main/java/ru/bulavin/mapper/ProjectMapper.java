package ru.bulavin.mapper;

import ru.bulavin.dto.project.controller.ProjectControllerDto;
import ru.bulavin.dto.project.view.ProjectViewDto;
import ru.bulavin.entity.Project;

public class ProjectMapper {
    public Project map(ProjectControllerDto obj) {
        return Project.builder()
                .idProject(obj.idProject())
                .name(obj.name())
                .description(obj.description())
                .idUser(obj.idUser())
                .build();
    }
    public ProjectControllerDto map(Project obj) {
        return ProjectControllerDto.builder()
                .idProject(obj.getIdProject())
                .name(obj.getName())
                .description(obj.getDescription())
                .idUser(obj.getIdUser())
                .build();
    }
    public ProjectViewDto mapController(ProjectControllerDto obj) {
        return ProjectViewDto.builder()
                .idProject(obj.idProject().toString())
                .name(obj.name())
                .description(obj.description()==null?"-":obj.description())
                .idUser(obj.idUser().toString())
                .build();
    }
}
