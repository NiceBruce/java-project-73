package hexlet.code;

import hexlet.code.config.SpringConfigForIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testInit() {
        assertThat(true).isEqualTo(true);
    }

//    @Test
//    void testRootPage() throws Exception {
//
////        var response = utils.perform(get(POST_CONTROLLER_PATH), TEST_USERNAME)
////                .andExpect(status().isOk())
////                .andReturn()
////                .getResponse();
//
//        // Выполняем запрос и получаем ответ
//        MockHttpServletResponse response = mockMvc
//                // Выполняем GET запрос по указанному адресу
//                .perform(get("/"))
//                // Получаем результат MvcResult
//                .andReturn()
//                // Получаем ответ MockHttpServletResponse из класса MvcResult
//                .getResponse();
//
//        // Проверяем статус ответа
//        assertThat(response.getStatus()).isEqualTo(200);
//        // Проверяем, что ответ содержит определенный текст
//        assertThat(response.getContentAsString()).contains("Привет от Хекслета!");
//    }
}
