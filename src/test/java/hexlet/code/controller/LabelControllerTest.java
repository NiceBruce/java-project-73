package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.utils.TestUtilsForApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.LabelController.ID;
import static hexlet.code.controller.LabelController.POST_CONTROLLER_PATH;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class LabelControllerTest {
    @Autowired
    private LabelRepository labelRepository;

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
    public void createLabel() throws Exception {

        MockHttpServletRequestBuilder request = post(LabelController.POST_CONTROLLER_PATH)
                .content(asJson(utils.buildLabel("testLabel")))
                .contentType(APPLICATION_JSON);

        utils.perform(request, TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, labelRepository.count());
    }

    @Test
    public void updateLabelById() throws Exception {

        long actualLabelId = utils.createLabel("errorTask").getId();

        var updateRequest = put(POST_CONTROLLER_PATH + ID, actualLabelId)
                .content(asJson(utils.buildLabel("fixedTask")))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        assertTrue(labelRepository.existsById(actualLabelId));
        assertNotNull(labelRepository.findByName("fixedTask").orElse(null));

    }


    @Test
    public void  getAllLabels() throws Exception {

        for (String word: "Error Bug Fix".split(" ")) {
            utils.createLabel(word.toUpperCase());
        }

        var response = utils.perform(get(POST_CONTROLLER_PATH), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(labels).hasSize(3);
    }
    @Test
    public void  getLabelById() throws Exception {

        Label actualLabel = utils.createLabel("testBUG");

        var response = utils.perform(get(POST_CONTROLLER_PATH + ID, actualLabel.getId()), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Label expectedLabel = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(actualLabel.getId(), expectedLabel.getId());
        assertEquals(actualLabel.getName(), expectedLabel.getName());
    }

    @Test
    public void deleteLabelById() throws Exception {

        long actualLabelsID = utils.createLabel("Error").getId();

        var deleteRequest = delete(POST_CONTROLLER_PATH + ID, actualLabelsID);

        utils.perform(deleteRequest, TEST_USERNAME).andExpect(status().isOk());

        assertFalse(labelRepository.existsById(actualLabelsID));
        assertNull(labelRepository.findByName("Error").orElse(null));
    }

}
