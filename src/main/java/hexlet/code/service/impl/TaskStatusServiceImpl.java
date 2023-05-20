package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    @Override
    public TaskStatus createTaskStatus(final TaskStatusDto dto) {
        final TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(dto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(long id, TaskStatusDto dto) {
        final TaskStatus taskStatusToUpdate = taskStatusRepository.findById(id).get();
        taskStatusToUpdate.setName(dto.getName());
        return taskStatusRepository.save(taskStatusToUpdate);
    }

    @Override
    public TaskStatus getCurrentTaskStatus(long id) {
        return taskStatusRepository.findById(id).get();
    }
}
