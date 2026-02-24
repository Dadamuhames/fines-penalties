package com.uzumtech.finespenalties.dto.response;

public record GcpResponse(String name, String surname, String address, String phoneNumber, String email,
                          String personalIdentificationNumber,
                          Integer age) {}
