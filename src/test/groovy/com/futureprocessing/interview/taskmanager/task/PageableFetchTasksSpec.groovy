package com.futureprocessing.interview.taskmanager.task

import com.futureprocessing.interview.taskmanager.task.dto.TaskDto
import org.springframework.data.domain.Page
import spock.lang.Specification

import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

class PageableFetchTasksSpec extends Specification {

    static int pageSize = 5

    TaskFacade taskFacade = new TaskConfiguration().taskFacade(pageSize)

    def 'Should return all tasks when there are less items than page size' () {
        given: 'there are tasks in repository'
            int leastId = 1
            List<TaskDto> taskDtos = [aTask(2), aTask(3), aTask(leastId), aTask(4)]
            taskDtos.forEach({taskDto -> taskFacade.addTask(taskDto)})

        when: 'module is requested to fetch all tasks'
            Page<TaskDto> firstPage = taskFacade.getAllTasks(0, pageSize)

        then: 'all tasks are returned'
            firstPage.content.containsAll(taskDtos)
            firstPage.content.size() == taskDtos.size()

        and: 'returned tasks are sorted by id in ascending direction'
            firstPage.content.first().id == leastId

        and: 'there is no next page to fetch'
            !firstPage.hasNext()
    }

    def 'Should return only first page of tasks when there are more items than page size' () {
        given: 'there are tasks in repository'
            List<TaskDto> taskDtos = [aTask(1), aTask(2), aTask(3), aTask(4), aTask(5), aTask(pageSize+1)]
            taskDtos.forEach({taskDto -> taskFacade.addTask(taskDto)})

        when: 'module is requested to fetch all tasks'
            Page<TaskDto> firstPage = taskFacade.getAllTasks(0, pageSize)

        then: 'all tasks are returned'
            firstPage.content.size() == pageSize

        and: 'page does not contain item from next pages'
            firstPage.content.stream().mapToLong({task -> task.getId()}).max().orElse(0) != (pageSize + 1)

        and: 'there is next page to fetch'
            firstPage.hasNext()

        and: 'next page contains remaining items'
            Page<TaskDto> secondPage = taskFacade.getAllTasks(firstPage.nextPageable().getPageNumber(), firstPage.nextPageable().getPageSize());
            secondPage.content.size() == 1
            secondPage.content.first() == taskDtos.last()

        and: 'second page is last one'
            !secondPage.hasNext()
    }

    def 'Should return first page of default size when no pageable param is set' () {
        given: 'there are tasks in repository'
            List<TaskDto> taskDtos = [aTask(1), aTask(2), aTask(3), aTask(4), aTask(5), aTask(6)]
            taskDtos.forEach({taskDto -> taskFacade.addTask(taskDto)})

        when: 'module is requested to fetch all tasks'
            Page<TaskDto> firstPage = taskFacade.getAllTasks(null, null)

        then: 'first page of default size is returned'
            firstPage.content.size() == pageSize
            firstPage.getNumber() == 0
            firstPage.getTotalElements() == 6
    }

    def 'Should return page of default size when requested page size is bigger than default' () {
        given: 'there are tasks in repository'
            List<TaskDto> taskDtos = [aTask(1), aTask(2), aTask(3), aTask(4), aTask(5), aTask(6)]
            taskDtos.forEach({taskDto -> taskFacade.addTask(taskDto)})

        when: 'module is requested to fetch all tasks'
            Page<TaskDto> firstPage = taskFacade.getAllTasks(0, pageSize + 5)

        then: 'first page of default size is returned'
            firstPage.content.size() == pageSize
            firstPage.getNumber() == 0
            firstPage.getTotalElements() == 6
    }

    private TaskDto aTask(int id) {
        return new TaskDto(id, randomAlphanumeric(10), randomAlphanumeric(20))
    }
}
