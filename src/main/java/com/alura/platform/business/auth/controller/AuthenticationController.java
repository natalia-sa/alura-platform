package com.alura.platform.business.auth.controller;

import com.alura.platform.business.auth.dto.AuthenticationDto;
import com.alura.platform.business.auth.dto.LoginResponseDto;
import com.alura.platform.business.security.TokenService;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login")
    @Operation(summary = "Authenticate user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserNameEmailRoleDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while authenticating") })
    public ResponseEntity login(
            @RequestBody
            @Valid
            AuthenticationDto authDto
    ) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            LoginResponseDto loginResponseDto = new LoginResponseDto(token);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
