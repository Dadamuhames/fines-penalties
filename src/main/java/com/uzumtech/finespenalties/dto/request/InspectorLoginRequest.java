package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InspectorLoginRequest(@NotBlank(message = "personnelNumber request") String personnelNumber,
                                    @NotBlank(message = "password request") String password) {}
