package com.uzumtech.finespenalties.utils;

import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ProtocolNumberUtils {
    private final LegalOffenseRepository legalOffenseRepository;

    @Transactional(readOnly = true)
    public String generateProtocolNumber() {
        var today = LocalDate.now();

        int countTodayOffenses = legalOffenseRepository.countByCreatedAt(today);

        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String sequence = String.format("%05d", countTodayOffenses + 1);

        return "FP-" + datePart + "-" + sequence;
    }

}
