package com.funcorp.springbootcontent.integrationtests

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootContentApplicationTests {
    @Autowired
    private val mockMvc: MockMvc? = null

    companion object {
        private val log = LoggerFactory.getLogger(SpringBootContentApplicationTests::class.java)
    }

    @Test
    fun play_get() {
        val result = mockMvc!!.perform(MockMvcRequestBuilders.get("/play/13"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
        log.info(result.toString())
        //                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }
}
