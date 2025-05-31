package org.example.tasklist.repository.impl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.tasklist.domain.exception.ResourceMappingException;
import org.example.tasklist.domain.task.Task;
import org.example.tasklist.repository.DataSourceConfig;
import org.example.tasklist.repository.TaskRepository;
import org.example.tasklist.repository.mappers.TaskRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            Select t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM tasks t WHERE id = ?
            """;

    private final String FIND_ALL_BY_USER_ID = """
        SELECT t.id AS task_id,
               t.title AS task_title,
               t.description AS task_description,
               t.expiration_date AS task_expiration_date,
               t.status AS task_status
        FROM tasks t
        JOIN users_tasks ut ON t.id = ut.task_id
        WHERE ut.user_id = ?
        """;

    private final String ASSIGN = """
            INSERT INTO users_tasks (task_id, user_id) VALUES (?, ?)
            """;

    private final String CREATE = """
            INSERT INTO tasks (title, description, expiration_date, status) VALUES (?, ?, ?, ?)
            """;

    private final String DELETE = """
            DELETE FROM tasks WHERE id = ?
            """;

    private final String UPDATE = """
            UPDATE tasks SET title = ?, description = ?, expiration_date = ?, status = ? WHERE id = ?
            """;

    @Override
    public Optional<Task> findById(Long id) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try(ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(TaskRowMapper.mapRow(rs));
            }

        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while finding user by id.");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            try(ResultSet rs = statement.executeQuery()){
                return TaskRowMapper.mapRows(rs);
            }

        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while finding tasks by user id.");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
//
            statement.executeUpdate();

        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while assigning to task.");
        }
    }

    @Override
    public void update(Task task) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());
            if(task.getDescription() == null ){
                statement.setNull(2, java.sql.Types.VARCHAR);
            }else {
                statement.setString(2, task.getDescription());
            }
            if(task.getExpirationDate() == null ) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());

            statement.executeUpdate();

        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while updating to task.");
        }
    }

    @Override
    public void create(Task task) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS ); //it generates key
            statement.setString(1, task.getTitle());
            if(task.getDescription() == null ){
                statement.setNull(2, java.sql.Types.VARCHAR);
            }else {
                statement.setString(2, task.getDescription());
            }
            if(task.getExpirationDate() == null ) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());

            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()){
                rs.next();
                task.setId(rs.getLong(1));
            }

        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while creating to task.");
        }

    }

    @Override
    public void delete(Long id) {
        try{
            Connection conn = dataSourceConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS ); //it generates key
            statement.setLong(1, id);
            statement.executeUpdate();


        } catch (Exception throwables) {
            throw new ResourceMappingException("Error while deleting to task.");
        }


    }

}
