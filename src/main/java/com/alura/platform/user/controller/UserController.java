package com.alura.platform.user.controller;

import com.alura.platform.user.dto.UserNameEmailRoleDto;
import com.alura.platform.user.entity.User;
import com.alura.platform.user.service.UserService;
import com.alura.platform.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "")
    @Operation(summary = "Save new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "500", description = "Something went wrong while creating user",
                    content = @Content) })
    public ResponseEntity save(
            @RequestBody
            @Valid
            UserDto userDto) {
        try {
            User user = userService.save(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/by/username")
    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "500", description = "Something went wrong while searching user",
                    content = @Content) })
    public ResponseEntity findByUsername(
            @RequestParam
            String username
            ) {
        try {
            UserNameEmailRoleDto userDto = userService.findByUsername(username);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
