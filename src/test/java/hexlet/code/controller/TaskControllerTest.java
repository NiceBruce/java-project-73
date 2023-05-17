package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.POST_CONTROLLER_PATH;

import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regDefaultUser();
        utils.createTaskStatuses();
        utils.createLabels();
        utils.createTasks();
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }


    @Test
    public void createTask() throws Exception {

        assertEquals(3, taskRepository.count());
        assertTrue(taskRepository.existsById(taskRepository.findByName("TASK3").get().getId()));
    }

    @Test
    public void updateTaskByID() throws Exception {

        Task actualTask = taskRepository.findById(utils.getRandomNum(taskRepository.count())).get();

        var updateRequest = put(POST_CONTROLLER_PATH + ID, actualTask.getId())
                .content(asJson(utils.buildTask("TaskOnFuture")))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        Task expectedTask = taskRepository.findByName("TaskOnFuture").get();

        assertEquals(actualTask.getId(), expectedTask.getId());
        assertNull(taskRepository.findByName(actualTask.getName()).orElse(null));
        assertNotNull(taskRepository.findByName(expectedTask.getName()).orElse(null));
    }

    @Test
    public void  getAllTasks() throws Exception {

        var response = utils.perform(get(POST_CONTROLLER_PATH), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(tasks).hasSize(3);
    }

    @Test
    public void getTaskById() throws Exception {

        Task actualTask = taskRepository.findById(utils.getRandomNum(taskRepository.count())).get();

        var response = utils.perform(get(POST_CONTROLLER_PATH + ID, actualTask.getId()), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Task expectedTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(actualTask.getId(), expectedTask.getId());
        assertEquals(actualTask.getName(), expectedTask.getName());
    }

    @Test
    public void deleteTaskById() throws Exception {

        Task actualTask = taskRepository.findById(utils.getRandomNum(taskRepository.count())).get();

        var deleteRequest = delete(POST_CONTROLLER_PATH + ID, actualTask.getId());

        utils.perform(deleteRequest, TEST_USERNAME).andExpect(status().isOk());

        assertFalse(taskRepository.existsById(actualTask.getId()));
        assertNull(taskRepository.findByName(actualTask.getName()).orElse(null));
    }
}
