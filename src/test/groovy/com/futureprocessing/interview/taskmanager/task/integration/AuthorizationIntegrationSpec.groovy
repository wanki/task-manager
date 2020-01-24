package com.futureprocessing.interview.taskmanager.task.integration

import com.futureprocessing.interview.taskmanager.base.BaseIntegrationSpec
import spock.lang.Unroll

import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryUser.TASK_EDITOR
import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryUser.TASK_VIEWER
import static org.springframework.http.HttpMethod.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthorizationIntegrationSpec extends BaseIntegrationSpec implements UserAuthorization {

    @Unroll
    def 'Should return status unauthorized when received request containing no authorization header'() {
        expect:
            mockMvc.perform(unauthorized(httpMethod, uri))
                   .andExpect(status().isUnauthorized())

        where:
            httpMethod | uri
            GET        | '/task/123'
            GET        | '/task'
            PUT        | '/task/123'
            POST       | '/task'
    }

    @Unroll
    def 'Should return status forbidden when received request containing credentials of user with unsufficient role set'() {
        expect:
            mockMvc.perform(authorizedBy(httpMethod, uri, user))
                   .andExpect(status().isForbidden())

        where:
            httpMethod | uri         | user
            GET        | '/task/123' | TASK_EDITOR
            GET        | '/task'     | TASK_EDITOR
            PUT        | '/task/123' | TASK_VIEWER
            POST       | '/task'     | TASK_VIEWER
    }
}
