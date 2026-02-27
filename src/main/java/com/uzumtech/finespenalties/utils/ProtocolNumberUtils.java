package com.uzumtech.finespenalties.utils;

import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ProtocolNumberUtils {

    public String generateProtocolNumber() {
        var today = LocalDate.now();

        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String sequence = String.valueOf(Instant.now().toEpochMilli());

        return "FP-" + datePart + "-" + sequence;
    }

}
