package org.example.tasklist.repository;
import java.util.Optional;
import org.example.tasklist.domain.user.*;

public interface UserRepository {

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void update(User user);
    void create(User user);
    void insertUserRole(Long userId, Role role);
    boolean isTaskOwner(Long taskId, Long userId);
    void delete(Long id);
}
