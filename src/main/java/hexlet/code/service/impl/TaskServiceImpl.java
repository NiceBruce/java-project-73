package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final LabelService labelService;

    @Override
    public Task createTask(TaskDto dto) {
        Task task = fromDto(dto);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskDto dto) {

        Task task = fromDto(dto);
        task.setAuthor(userService.getCurrentUserById(id));
        task.setId(id);

        return taskRepository.save(task);
    }

    @Override
    public Task getCurrentTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Set<Label> getLabels(Set<Long> labelIds) {
        return Optional.ofNullable(labelIds)
                .orElse(Set.of())
                .stream()
                .map(labelService::getCurrentLabelById)
                .collect(Collectors.toSet());
    }

    public Task fromDto(TaskDto dto) {

        TaskStatus taskStatus = taskStatusService.getCurrentTaskStatusById(dto.getTaskStatusId());
        User author = userService.getCurrentUserByEmail();
        User executor = userService.getCurrentUserById(dto.getExecutorId());
        Set<Label> labels = getLabels(dto.getLabelIds());

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
