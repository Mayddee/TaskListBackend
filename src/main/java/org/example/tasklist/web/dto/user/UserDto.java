package org.example.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.tasklist.domain.task.Task;
import org.example.tasklist.domain.user.Role;
import org.example.tasklist.web.dto.validation.OnCreate;
import org.example.tasklist.web.dto.validation.OnUpdate;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    @NotNull(message = " User id can not be null", groups = OnUpdate.class)
    private long id;

    @NotNull(message = " User name can not be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name length can not be smaller than 255 symbols.", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = " Username can not be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length can not be smaller than 255 symbols.", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password can not be null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation can not be null", groups = {OnCreate.class, OnUpdate.class})
    private String passwordConfirmation;

}
