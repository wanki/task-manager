package com.futureprocessing.interview.taskmanager.base

import com.futureprocessing.interview.taskmanager.AppRunner
import com.futureprocessing.interview.taskmanager.infrastructure.config.Profiles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(classes = [AppRunner], webEnvironment = RANDOM_PORT)
@ActiveProfiles([Profiles.INTEGRATION_TEST])
@AutoConfigureMockMvc
@Testcontainers
abstract class BaseIntegrationSpec extends Specification {
    @Autowired
    WebApplicationContext webApplicationContext
    @Autowired
    MockMvc mockMvc
    @Autowired
    JdbcTemplate jdbcTemplate;

}




