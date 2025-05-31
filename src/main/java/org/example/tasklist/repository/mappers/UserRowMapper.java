package org.example.tasklist.repository.mappers;

import lombok.SneakyThrows;
import org.example.tasklist.domain.task.Status;
import org.example.tasklist.domain.task.Task;
import org.example.tasklist.domain.user.Role;
import org.example.tasklist.domain.user.User;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {


@SneakyThrows
public static User mapRow(ResultSet rs) {
    User user = null;
    Set<Role> roles = new HashSet<>();
    List<Task> tasks = new java.util.ArrayList<>();
    Long userId = null;

    while (rs.next()) {
        if (user == null) {
            user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("user_name"));
            user.setUsername(rs.getString("user_username"));
            user.setPassword(rs.getString("user_password"));
            userId = user.getId();
        }


        String roleStr = rs.getString("user_role_role");
        if (roleStr != null) {
            roles.add(Role.valueOf(roleStr));
        }

        Long taskId = rs.getLong("task_id");
        if (taskId != 0) { // чтобы не добавлять если task_id = null
            Task task = new Task();
            task.setId(taskId);
            task.setDescription(rs.getString("task_description"));
            task.setExpirationDate(rs.getTimestamp("task_expiration_date").toLocalDateTime());
            task.setStatus(Status.valueOf(rs.getString("task_status")));
            tasks.add(task);
        }
    }

    if (user != null) {
        user.setRoles(roles);
        user.setTasks(tasks);
    }

    return user;
}
}
