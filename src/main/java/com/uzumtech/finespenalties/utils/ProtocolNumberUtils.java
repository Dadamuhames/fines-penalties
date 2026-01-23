package com.uzumtech.finespenalties.utils;

import com.uzumtech.finespenalties.repository.LegalOffenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ProtocolNumberUtils {
    private final LegalOffenceRepository legalOffenceRepository;

    public String generateProtocolNumber() {
        var today = OffsetDateTime.now();

        int countTodayOffenses = legalOffenceRepository.countByCreatedAt(today);

        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String sequence = String.format("%05d", countTodayOffenses + 1);

        return "FP-" + datePart + "-" + sequence;
    }

}
