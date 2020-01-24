package com.futureprocessing.interview.taskmanager.task.integration

import com.futureprocessing.interview.taskmanager.base.BaseIntegrationSpec
import com.futureprocessing.interview.taskmanager.task.TaskFacade
import com.futureprocessing.interview.taskmanager.task.dto.TaskDto
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

class UpdateTaskIntegrationSpec extends BaseIntegrationSpec implements UserAuthorization, TaskSamples {

    @Autowired
    TaskFacade taskFacade

    def 'Should update task when received PUT request with existing task id and valid task in body'() {
        given: 'There is existing task'
            TaskDto taskDto = taskFacade.addTask(aTask())

        and: 'task update data'
            String taskTitleUpdate = randomAlphanumeric(10)
            String taskDescriptionUpdate = randomAlphanumeric(10)

        expect: 'update task request is received'
            mockMvc.perform(authorized(PUT, """/task/$taskDto.id""").contentType(APPLICATION_JSON)
                                                                    .content("""{ "title" : "$taskTitleUpdate", "description" : "$taskDescriptionUpdate"}"""))
                   .andExpect(status().isNoContent())

        and: 'task is updated'
            TaskDto updatedTask = taskFacade.getTaskById(taskDto.getId()).get()
            updatedTask
            updatedTask.title == taskTitleUpdate
            updatedTask.description == taskDescriptionUpdate
    }

    def 'Should return not found status when received valid update task request for non-existent task id'() {
        expect:
            mockMvc.perform(authorized(PUT, "/task/12345").contentType(APPLICATION_JSON)
                                                          .content("""{ "title" : "TaskTitle", "description" : "TaskDesc"}"""))
                   .andExpect(status().isNotFound())
    }

    def 'Should return method not allowed status when received update task request without id in path'() {
        expect:
            mockMvc.perform(authorized(PUT, "/task").contentType(APPLICATION_JSON)
                                                          .content("""{ "title" : "TaskTitle", "description" : "TaskDesc"}"""))
                   .andExpect(status().isMethodNotAllowed())
    }

    def 'Should return bad request when received invalid update task request'() {
        given: 'There is existing task'
            TaskDto taskDto = taskFacade.addTask(aTask())
        expect:
            mockMvc.perform(authorized(PUT, """/task/$taskDto.id""").contentType(APPLICATION_JSON)
                                                                    .content('{}'))
                   .andExpect(status().isBadRequest())
    }
}
