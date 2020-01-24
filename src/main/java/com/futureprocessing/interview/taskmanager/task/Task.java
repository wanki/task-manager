package com.futureprocessing.interview.taskmanager.task;

import com.futureprocessing.interview.taskmanager.task.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
class Task {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    String description;

    static Task of(TaskDto dto) {
        return new Task(dto.getId(), dto.getTitle(), dto.getDescription());
    }

    TaskDto dto() {
        return new TaskDto(id, title, description);
    }
}
