package com.uzumtech.finespenalties.controller.inspector;

import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.service.intr.InspectorLegalOffenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/inspector/offenses")
public class InspectorOffenseController {

    private final InspectorLegalOffenseService offenseService;


    @GetMapping
    public ResponseEntity<Page<InspectorLegalOffenseResponse>> list(@AuthenticationPrincipal InspectorEntity inspector, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") @Max(50) Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<InspectorLegalOffenseResponse> offenses = offenseService.findAllForInspector(inspector, pageable);

        return ResponseEntity.ok(offenses);
    }


    @PostMapping
    public ResponseEntity<InspectorLegalOffenseResponse> create(@Valid @RequestBody LegalOffenceRegisterRequest request, @AuthenticationPrincipal InspectorEntity inspector) {

        InspectorLegalOffenseResponse response = offenseService.registerLegalOffense(inspector, request);

        return ResponseEntity.ok(response);
    }
}
