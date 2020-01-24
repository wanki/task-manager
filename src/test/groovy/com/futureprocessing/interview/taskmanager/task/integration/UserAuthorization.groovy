package com.futureprocessing.interview.taskmanager.task.integration

import com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryUser
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryUser.BRUCE_ALMIGHTY
import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.util.Base64Utils.encodeToString

trait UserAuthorization {

    MockHttpServletRequestBuilder unauthorized(HttpMethod httpMethod, String urlTemplate) {
        return new MockHttpServletRequestBuilder(httpMethod, urlTemplate, new Object[0])
    }

    MockHttpServletRequestBuilder authorizedBy(HttpMethod httpMethod, String urlTemplate, InMemoryUser inMemoryUser) {
        return unauthorized(httpMethod, urlTemplate).header(AUTHORIZATION, authorizationHeader(inMemoryUser))
    }

    MockHttpServletRequestBuilder authorized(HttpMethod httpMethod, String urlTemplate) {
        return authorizedBy(httpMethod, urlTemplate, BRUCE_ALMIGHTY)
    }

    private String authorizationHeader(InMemoryUser inMemoryUser) {
        return "Basic " + encodeToString("""$inMemoryUser.username:$inMemoryUser.password""".getBytes())
    }
}