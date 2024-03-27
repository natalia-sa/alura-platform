package com.alura.platform.business.registration.controller;

import com.alura.platform.business.basic.IdDto;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.registration.service.RegistrationService;
import com.alura.platform.exception.ActionDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "")
    @Operation(summary = "Save new registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of the saved entity",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating registration") })
    public ResponseEntity save(
            @RequestBody
            @Valid
            RegistrationUserIdCourseIdDto registrationDto) {
        try {
            Registration registration = registrationService.save(registrationDto);
            IdDto idDto = new IdDto(registration.getId());
            return new ResponseEntity<>(idDto, HttpStatus.CREATED);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
