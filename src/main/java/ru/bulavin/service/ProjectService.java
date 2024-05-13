package ru.bulavin.service;

import ru.bulavin.dto.project.controller.ProjectControllerDto;
import ru.bulavin.entity.Project;
import ru.bulavin.mapper.ProjectMapper;
import ru.bulavin.repository.ProjectDao;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    private final ProjectDao projectDao;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectDao projectDao, ProjectMapper projectMapper) {
        this.projectDao = projectDao;
        this.projectMapper = projectMapper;
    }
    public void insertProject(ProjectControllerDto projectControllerDto) {
        Project project = projectMapper.map(projectControllerDto);
        projectDao.insert(project);
    }
    public List<ProjectControllerDto> selectByUserId(Long id) {
        return projectDao.selectAllByUserId(id).stream().map(projectMapper::map).collect(Collectors.toList());
    }
    public ProjectControllerDto selectById(Long id) {
        return projectMapper.map(projectDao.selectById(id));
    }
    public boolean existsByName(String name, Long idUser) {
        return projectDao.selectAllByUserId(idUser).stream().anyMatch(project -> project.getName().equals(name));
    }
    public void delete(Long id) {
        projectDao.delete(id);
    }
    public void update(ProjectControllerDto project) {
        projectDao.update(projectMapper.map(project));
    }
}
