package com.futureprocessing.interview.taskmanager.infrastructure.api;

import com.futureprocessing.interview.taskmanager.task.TaskFacade;
import com.futureprocessing.interview.taskmanager.task.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpHeaders.LINK;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class TaskController {

    private static String LINK_HEADER_VALUE = "<%s>; rel=\"next\"";

    TaskFacade taskFacade;

    @PostMapping
    ResponseEntity addTask(@RequestBody @NotNull @Valid TaskDto taskDto) {
        TaskDto addedTask = taskFacade.addTask(taskDto);
        return created(locationUriOf(addedTask)).build();
    }

    @PutMapping("/{taskId}")
    ResponseEntity updateTask(@PathVariable("taskId") Long taskId, @RequestBody @NotNull @Valid TaskDto taskDto) {
        TaskDto taskWithId = new TaskDto(taskId, taskDto.getTitle(), taskDto.getDescription());
        return taskFacade.updateTask(taskWithId).isPresent()
                ? noContent().build()
                : notFound().build();
    }

    @GetMapping("/{taskId}")
    ResponseEntity<TaskDto> getTask(@PathVariable("taskId") Long taskId) {
        return ResponseEntity.of(taskFacade.getTaskById(taskId));
    }

    @GetMapping
    ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
        Page<TaskDto> tasksPage = taskFacade.getAllTasks(page, size);
        return nextPageLinkHeader(tasksPage)
                .map(linkHeader -> ok().header(LINK, linkHeader)
                                       .body(tasksPage.getContent()))
                .orElse(ok(tasksPage.getContent()));
    }

    private URI locationUriOf(TaskDto taskDto) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                                          .path("/" + taskDto.getId())
                                          .build()
                                          .toUri();
    }

    private Optional<String> nextPageLinkHeader(Page<?> currentPage) {
        return Optional.ofNullable(currentPage)
                       .map(page -> page.hasNext() ? page.nextPageable() : null)
                       .map(nextPageable -> currentRequestUriBuilder()
                               .replaceQueryParam("page", nextPageable.getPageNumber())
                               .replaceQueryParam("size", nextPageable.getPageSize())
                               .toUriString())
                       .map(url -> format(LINK_HEADER_VALUE, url));
    }

    private UriComponentsBuilder currentRequestUriBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequest();
    }
}
