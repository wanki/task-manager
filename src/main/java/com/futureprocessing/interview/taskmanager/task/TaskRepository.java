package com.futureprocessing.interview.taskmanager.task;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

interface TaskRepository extends Repository<Task, Long> {

    Task saveAndFlush(Task task);

    Optional<Task> findById(Long id);

    Page<Task> findAll(Pageable pageable);
}

class TaskInMemoryRepository implements TaskRepository {
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    @Override
    public Task saveAndFlush(Task task) {
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return ofNullable(taskMap.get(id));
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        PagedListHolder<Task> pagedStore = new PagedListHolder<>(new ArrayList<>(taskMap.values()));
        pagedStore.setPageSize(pageable.getPageSize());
        pagedStore.setPage(pageable.getPageNumber());
        return new PageImpl<>(pagedStore.getPageList(), pageable, pagedStore.getNrOfElements());
    }
}
