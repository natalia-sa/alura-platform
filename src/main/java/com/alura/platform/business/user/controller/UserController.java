package com.alura.platform.business.user.controller;

import com.alura.platform.business.basic.dto.ExceptionMessageDto;
import com.alura.platform.business.basic.dto.IdDto;
import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "")
    @Operation(summary = "Save new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of the saved entity",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating user") })
    public ResponseEntity save(
            @RequestBody
            @Valid
            UserDto userDto) {
        try {
            User user = userService.save(userDto);
            IdDto idDto = new IdDto(user.getId());
            return new ResponseEntity<>(idDto, HttpStatus.CREATED);
        } catch (Exception e) {
            ExceptionMessageDto message = new ExceptionMessageDto(e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/by/username")
    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserNameEmailRoleDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while searching user") })
    public ResponseEntity findByUsername(
            @RequestParam
            @NotBlank
            @Schema(example = "joao")
            String username
            ) {
        try {
            UserNameEmailRoleDto userDto = userService.findByUsername(username);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ExceptionMessageDto message = new ExceptionMessageDto(e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ExceptionMessageDto message = new ExceptionMessageDto(e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
