package com.futureprocessing.interview.taskmanager.task.integration

import com.futureprocessing.interview.taskmanager.base.BaseIntegrationSpec
import org.springframework.test.web.servlet.MvcResult

import static org.springframework.http.HttpHeaders.LOCATION
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

class CreateTaskIntegrationSpec extends BaseIntegrationSpec implements UserAuthorization, TaskSamples {

    def 'Should create task when received POST request with valid task in body'() {
        given:
            String taskTitle = randomAlphanumeric(10)
            String taskDescription = randomAlphanumeric(10)

        expect: ''
            MvcResult result = mockMvc.perform(authorized(POST, '/task').contentType(APPLICATION_JSON)
                                                                        .content("""{ "title" : "$taskTitle", "description" : "$taskDescription"}"""))
                                      .andExpect(status().isCreated())
                                      .andReturn()

        and: 'location header is not empty'
            String locationHeader = result.response.getHeader(LOCATION)
            locationHeader != null

        and: 'created task details is available under URL returned in header '
            mockMvc.perform(authorized(GET, new URL(locationHeader).getPath()))
                   .andExpect(status().isOk())
                   .andExpect(content().json("""{
                                               "title" : "$taskTitle",
                                               "description" : "$taskDescription"
                                          }"""))
    }

    def 'Should return bad request when received invalid create task request'() {
        expect:
            mockMvc.perform(authorized(POST, """/task""").contentType(APPLICATION_JSON)
                                                         .content('{}'))
                   .andExpect(status().isBadRequest())
    }
}
