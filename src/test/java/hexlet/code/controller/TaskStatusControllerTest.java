package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtilsForApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.TaskStatusController.ID;
import static hexlet.code.controller.TaskStatusController.POST_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtilsForApp.asJson;
import static hexlet.code.utils.TestUtilsForApp.TEST_USERNAME;
import static hexlet.code.utils.TestUtilsForApp.fromJson;
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

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtilsForApp utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regDefaultUser();
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void  getAllTaskStatuses() throws Exception {

        utils.createTaskStatuses();
        var response = utils.perform(get(POST_CONTROLLER_PATH), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Task> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(taskStatuses).hasSize(3);
    }

    @Test
    public void createTaskStatus() throws Exception {

       utils.createTaskStatuses();
        assertEquals(3, taskStatusRepository.count());
    }

    @Test
    public void updateTaskStatusById() throws Exception {

        long actualTaskStatusId = utils.createTaskStatus("testGoodWork").getId();

        var updateRequest = put(POST_CONTROLLER_PATH + ID, actualTaskStatusId)
                .content(asJson(utils.buildTaskStatus("testBadWork")))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        assertTrue(taskStatusRepository.existsById(actualTaskStatusId));
        assertNotNull(taskStatusRepository.findByName("testBadWork").orElse(null));

    }

    @Test
    public void deleteTaskStatusById() throws Exception {

        long actualTaskStatusId = utils.createTaskStatus("testGoodWork").getId();

        var updateRequest = delete(POST_CONTROLLER_PATH + ID, actualTaskStatusId);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assertFalse(taskStatusRepository.existsById(actualTaskStatusId));
        assertNull(taskStatusRepository.findByName("testGoodWork").orElse(null));
    }


    @Test
    public void  getTaskStatusById() throws Exception {

        TaskStatus actualTaskStatus = utils.createTaskStatus("newTaskStatus");

        var response = utils.perform(get(POST_CONTROLLER_PATH + ID, actualTaskStatus.getId()), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(actualTaskStatus.getId(), taskStatus.getId());
        assertEquals(actualTaskStatus.getName(), taskStatus.getName());
    }



}
