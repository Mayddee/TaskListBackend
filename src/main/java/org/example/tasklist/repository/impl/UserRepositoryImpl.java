package org.example.tasklist.repository.impl;
import lombok.RequiredArgsConstructor;
import org.example.tasklist.domain.exception.ResourceMappingException;
import org.example.tasklist.domain.user.Role;
import org.example.tasklist.domain.user.User;
import org.example.tasklist.repository.DataSourceConfig;
import org.example.tasklist.repository.UserRepository;
import org.example.tasklist.repository.mappers.UserRowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            LEFT JOIN users_tasks ut on u.id = ut.user_id
            LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.id = ?""";

    private final String FIND_BY_USERNAME = """
            Select u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            LEFT JOIN users_tasks ut on u.id = ut.user_id
            LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.username = ?
            """;

    private final String  UPDATE = """
            UPDATE users SET name = ?, username = ?, password = ? WHERE id = ?
            """;

    private final String CREATE = """
            INSERT INTO users (name, username, password) VALUES (?, ?, ?)""";

    private final String DELETE = """
            DELETE FROM users WHERE id = ?
            """;

    private final String INSERT_USER_ROLE = """
INSERT INTO user_roles (user_id, role) VALUES (?, ?)
""";
    private final String IS_TASK_OWNER = """
            SELECT exists(
                SELECT 1
                FROM users_tasks
                WHERE user_id = ? AND task_id = ?
            )
            """;
    private final DataSource dataSource;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_BY_ID, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while finding user by id");
        }
    }



    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_USERNAME,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());

            statement.executeUpdate();

        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();

            try(ResultSet rs  = statement.getGeneratedKeys()){
                rs.next();
                user.setId(rs.getLong(1));
            }
        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while creating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);

            statement.setString(2, role.name());
            statement.executeUpdate();

        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while inserting user role");
        }
    }

    @Override
    public boolean isTaskOwner(Long taskId, Long userId) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);

            statement.setLong(2, taskId);
            try(ResultSet rs = statement.executeQuery()){
                rs.next();
                return rs.getBoolean(1);
            }

        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while checking if user is task owner");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();

        }
        catch (SQLException throwables){
            throw new ResourceMappingException("Exception while dleting user.");
        }
    }

}
