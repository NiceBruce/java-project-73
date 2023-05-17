package hexlet.code.controller;


import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    private final UserService userService;
    private final UserRepository userRepository;


    @Operation(summary = "Get list of all users")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = User.class))
    ))
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll()
                .stream()
                .toList();
    }


    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @PostMapping
    @ResponseStatus(CREATED)
    public User createUser(@RequestBody @Valid final UserDto dto) {
        return userService.createNewUser(dto);
    }

    @Operation(summary = "Get specific user by his id")
    @ApiResponses(value = {
            // Указываем, что операция вернет ответ с кодом 200 в случае успешного выполнения
            @ApiResponse(responseCode = "200", description = "User found"),
            // И ответ с кодом 404 в случе, если пользователь не найден
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @GetMapping(ID)
    public User getUserById(@PathVariable final Long id) {
        return userRepository.findById(id).get();
    }



    @Operation(summary = "Delete user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Attempting to perform an operation "
                    + "for which the user does not have rights"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }



    @Operation(summary = "Update user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(@PathVariable long id, @RequestBody @Valid final UserDto dto) {
        return userService.updateUser(id, dto);
    }
}
