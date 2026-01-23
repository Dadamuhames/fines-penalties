package com.uzumtech.finespenalties.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class GcpService {
    private final RestClient gcpRestClient;


}
