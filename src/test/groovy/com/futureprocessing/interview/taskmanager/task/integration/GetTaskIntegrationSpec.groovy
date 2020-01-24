package com.futureprocessing.interview.taskmanager.task.integration

import com.futureprocessing.interview.taskmanager.base.BaseIntegrationSpec
import com.futureprocessing.interview.taskmanager.task.TaskFacade
import com.futureprocessing.interview.taskmanager.task.dto.TaskDto
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

class GetTaskIntegrationSpec extends BaseIntegrationSpec implements UserAuthorization, TaskSamples {

    @Autowired
    TaskFacade taskFacade

    def 'Should return task when authorized GET request with existing task id received'() {
        given: 'There is existing task'
            TaskDto taskDto = taskFacade.addTask(aTask())

        expect: 'task is returned when request with existing id is received'
            mockMvc.perform(authorized(GET, """/task/$taskDto.id"""))
                   .andExpect(status().isOk())
                   .andExpect(content().json("""{
                                           "id": $taskDto.id,
                                           "title" : "$taskDto.title",
                                           "description" : "$taskDto.description"
                                      }"""))

    }

    def 'Should return not found status when authorized GET request with non-existent task id received'() {
        expect: 'task is returned when request with existing id is received'
            mockMvc.perform(authorized(GET, """/task/12345"""))
                   .andExpect(status().isNotFound())

    }

    def 'Should return all task in paginated model when received GET request'() {
        given: 'There is existing task'
            TaskDto taskDto = taskFacade.addTask(aTask())

        expect: 'task is returned when request with existing id is received'
            mockMvc.perform(authorized(GET, """/task/$taskDto.id"""))
                   .andExpect(status().isOk())
                   .andExpect(content().json("""{
                                           "id": $taskDto.id,
                                           "title" : "$taskDto.title",
                                           "description" : "$taskDto.description"
                                      }"""))

    }

}
