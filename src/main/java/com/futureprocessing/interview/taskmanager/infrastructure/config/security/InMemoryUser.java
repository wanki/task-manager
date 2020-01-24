package com.futureprocessing.interview.taskmanager.infrastructure.config.security;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryRole.EDIT_TASK;
import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryRole.VIEW_TASK;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum InMemoryUser {

    TASK_VIEWER("viewer", "viewerPassword", List.of(VIEW_TASK)),
    TASK_EDITOR("editor", "editorPassword", List.of(EDIT_TASK)),
    BRUCE_ALMIGHTY("balmighty", "secret", List.of(InMemoryRole.values()));

    String username;
    String password;
    List<InMemoryRole> roles;

    public User toUser() {
        List<GrantedAuthority> authorities = roles.stream()
                                                  .map(Enum::name)
                                                  .map(SimpleGrantedAuthority::new)
                                                  .collect(toList());
        return new User(username, "{noop}"+password, authorities);
    }
}
