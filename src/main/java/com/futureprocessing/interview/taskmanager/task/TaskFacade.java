package com.futureprocessing.interview.taskmanager.task;

import com.futureprocessing.interview.taskmanager.task.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TaskFacade {

    TaskRepository taskRepository;
    int taskPageSize;

    public TaskDto addTask(TaskDto taskDto) {
        return taskRepository.saveAndFlush(Task.of(taskDto))
                             .dto();
    }

    public Optional<TaskDto> updateTask(TaskDto taskDto) {
        return taskRepository.findById(taskDto.getId())
                             .map(existingTask -> taskRepository.saveAndFlush(Task.of(taskDto)))
                             .map(Task::dto);
    }

    public Optional<TaskDto> getTaskById(long id) {
        return taskRepository.findById(id)
                             .map(Task::dto);
    }

    public Page<TaskDto> getAllTasks(Integer page, Integer size) {
        return taskRepository.findAll(pageableFrom(page, size))
                             .map(Task::dto);
    }


    private Pageable pageableFrom(Integer page, Integer size) {
        int pageSize =  size != null && size < taskPageSize
                ? size
                : taskPageSize;
        return PageRequest.of(page != null ? page : 0, pageSize, Sort.by(ASC, "id"));
    }
}
