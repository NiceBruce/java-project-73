package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final UserRepository userRepository;;
    private final LabelService labelService;

    @Override
    public Task createTask(TaskDto dto) {
        Task task = fromDto(dto);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskDto dto) {

        Task taskFromDto = fromDto(dto);
        Task currentTask = taskRepository.findById(id).get();

        currentTask.setName(taskFromDto.getName());
        currentTask.setDescription(taskFromDto.getDescription());
        currentTask.setExecutor(taskFromDto.getExecutor());
        currentTask.setTaskStatus(taskFromDto.getTaskStatus());
        if (!taskFromDto.getLabels().equals(currentTask.getLabels())) {
            currentTask.removeLabel();
            currentTask.addLabels(taskFromDto.getLabels());
        }

        return taskRepository.save(currentTask);
    }

    public Task fromDto(TaskDto dto) {

        TaskStatus taskStatus = taskStatusService.getCurrentTaskStatus(dto.getTaskStatusId());
        User author = userService.getCurrentUser();
        User executor = (dto.getExecutorId() == null) ? null : userRepository.findById(dto.getExecutorId()).get();
        Set<Label> labels = Collections.emptySet();

        if (!dto.getLabelIds().isEmpty()) {
            labels = dto.getLabelIds().stream()
                    .map(labelService::getCurrentLabel)
                    .collect(Collectors.toSet());
        }

        return Task.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .author(author)
                .executor(executor)
                .taskStatus(taskStatus)
                .labels(labels)
                .build();
    }
}
