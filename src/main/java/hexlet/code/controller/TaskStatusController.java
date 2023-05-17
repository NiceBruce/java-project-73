package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.POST_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + POST_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String POST_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;

    @Operation(hidden = true)
    @GetMapping
    public List<TaskStatus> getAll() {
        return taskStatusRepository.findAll()
                .stream()
                .toList();
    }

    @Operation(hidden = true)
    @PostMapping
    @ResponseStatus(CREATED)
    public TaskStatus createTaskStatus(@RequestBody @Valid final TaskStatusDto dto) {
        return taskStatusService.createTaskStatus(dto);
    }

    @Operation(hidden = true)
    @GetMapping(ID)
    public TaskStatus getTaskStatusById(@PathVariable final Long id) {
        return taskStatusRepository.findById(id).get();
    }

    @Operation(hidden = true)
    @DeleteMapping(ID)
    public void deleteTaskStatus(@PathVariable long id) {
        this.taskStatusRepository.deleteById(id);
    }

    @Operation(hidden = true)
    @PutMapping(ID)
    public TaskStatus updateTaskStatus(@PathVariable long id, @RequestBody @Valid final TaskStatusDto dto) {
        return taskStatusService.updateTaskStatus(id, dto);
    }
}
