package com.futureprocessing.interview.taskmanager.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TaskConfiguration {

    TaskFacade taskFacade(int taskPageSize) {
        return taskFacade(new TaskInMemoryRepository(), taskPageSize);
    }
    @Bean
    TaskFacade taskFacade(TaskRepository taskRepository, @Value("${task-page-size}") int taskPageSize) {
        return new TaskFacade(taskRepository, taskPageSize);
    }
}
