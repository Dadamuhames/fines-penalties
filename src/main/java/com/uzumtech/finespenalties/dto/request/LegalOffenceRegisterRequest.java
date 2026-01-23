package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record LegalOffenceRegisterRequest(@NotNull Long codeArticleId,
                                          @NotBlank(message = "offenderPinfl required") @Size(max = 14, min = 14) String offenderPinfl,
                                          @NotBlank(message = "offenderFullName required") String offenderFullName,
                                          @NotBlank(message = "offenseLocation required") String offenseLocation,
                                          @NotBlank(message = "description required") String description,
                                          @NotNull OffsetDateTime offenseDateTime, String offenderExplanation) {}
