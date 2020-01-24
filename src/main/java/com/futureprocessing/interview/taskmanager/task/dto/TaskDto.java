package com.futureprocessing.interview.taskmanager.task.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class TaskDto {
    Long id;
    @NotNull
    String title;
    @NotNull
    String description;
}
