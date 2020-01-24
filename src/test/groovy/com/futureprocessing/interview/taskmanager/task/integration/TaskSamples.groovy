package com.futureprocessing.interview.taskmanager.task.integration


import com.futureprocessing.interview.taskmanager.task.dto.TaskDto

import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

trait TaskSamples {

    TaskDto aTask() {
        return new TaskDto(null, randomAlphanumeric(10), randomAlphanumeric(20))
    }
}