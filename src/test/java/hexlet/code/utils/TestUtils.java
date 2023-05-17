package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.controller.LabelController;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.POST_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_2 = "email2@email.com";

    private final UserDto testRegistrationDto = new UserDto(
            "fname",
            "lname",
            TEST_USERNAME,
            "pwd"
    );

    public UserDto getTestRegistrationDto() {
        return testRegistrationDto;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JWTHelper jwtHelper;

    public void tearDown() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationDto);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    // some methods for TaskStatus Entity
    public TaskStatusDto buildTaskStatus(final String nameTaskStatus) {
        return new TaskStatusDto(nameTaskStatus);
    }

    public TaskStatus createTaskStatus(String name) throws Exception {

        final var request = post(TaskStatusController.POST_CONTROLLER_PATH)
                .content(asJson(buildTaskStatus(name)))
                .contentType(APPLICATION_JSON);

        perform(request, TEST_USERNAME);

        return taskStatusRepository.findByName(name).get();
    }

    public List<TaskStatus> createTaskStatuses() throws Exception {

        for (String word: "New InWork OnTest".split(" ")) {
            createTaskStatus(word);
        }

        return taskStatusRepository.findAll();
    }


    // some methods for Label Entity

    public LabelDto buildLabel(final String nameLabel) {
        return new LabelDto(nameLabel);
    }

    public Label createLabel(String name) throws Exception {

        final var request = post(LabelController.POST_CONTROLLER_PATH)
                .content(asJson(buildLabel(name)))
                .contentType(APPLICATION_JSON);

        perform(request, TEST_USERNAME);

        return labelRepository.findByName(name).get();
    }

    public List<Label> createLabels() throws Exception {
        for (String word: "Error Bug Fix".split(" ")) {
            createLabel(word.toUpperCase());
        }

        return labelRepository.findAll();
    }

    // some methods for Task Entity

    public TaskDto buildTask(String name) {
        return new TaskDto(
                name,
                "some descptn",
                userRepository.findByEmail(TEST_USERNAME).get().getId(),
                getRandomNum(taskStatusRepository.count()),
                Set.of(getRandomNum(labelRepository.count())));
    }

    public Task createTask(String name) throws Exception {

        final var request = post(POST_CONTROLLER_PATH)
                .content(asJson(buildTask(name)))
                .contentType(APPLICATION_JSON);

        perform(request, TEST_USERNAME);

        return taskRepository.findByName(name).get();
    }

    public List<Task> createTasks() throws Exception {

        for (String word: "Task1 Task2 Task3".split(" ")) {
            createTask(word.toUpperCase());
        }

        return taskRepository.findAll();
    }

    public long getRandomNum(long max) {

        return (long) (Math.random() * ((max - 1) + 1)) + 1;
    }
}
