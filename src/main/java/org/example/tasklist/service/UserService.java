package org.example.tasklist.service;

import org.example.tasklist.domain.user.User;

public interface UserService {

    User getById(Long id);
    User getByUsername(String username);
    User update(User user);
    User create(User user);
    boolean isTaskOwner(Long taskId, Long userId);
    void delete(Long id);
}
