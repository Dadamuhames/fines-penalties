package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
class LegalOffenseRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private LegalOffenseRepository repository;


    @Autowired
    private EntityManager entityManager;

    private UserEntity user;
    private InspectorEntity inspector;
    private CodeArticleEntity article;


    private final String ARTICLE_TITLE = "Speeding";
    private final String NAME = "Jon Snow";
    private final String COURT_CASE_NUMBER = "CASE-2026-001";

    @BeforeEach
    void setUp() {
        // User

        String phone = String.format("99890%d", (int) (Math.random() * 10000000));

        user = UserEntity.builder()
            .fullName(NAME)
            .age(30)
            .email("john-" + UUID.randomUUID() + "@test.com")
            .phone(phone)
            .pinfl("12345678901234")
            .build();

        // Inspector
        inspector = InspectorEntity.builder()
            .fullName("Officer Nolan")
            .personnelNumber("ID-" + UUID.randomUUID())
            .pinfl("12345678901234")
            .password("secure_pass")
            .dateOfBirth(LocalDate.of(1985, 5, 20))
            .build();

        // Article
        article = CodeArticleEntity.builder()
            .reference("UZ-" + UUID.randomUUID())
            .title(ARTICLE_TITLE)
            .content("Speeding in restricted area")
            .build();

        entityManager.persist(user);
        entityManager.persist(inspector);
        entityManager.persist(article);
        entityManager.flush();
    }

    @Test
    void testFindByInspectorId_WithEntityGraph() {
        LegalOffenseEntity offense = createOffense();
        offense.setInspector(inspector);
        offense.setUser(user);
        repository.save(offense);

        flushAndClear();

        Page<LegalOffenseEntity> page = repository.findByInspectorId(inspector.getId(), PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        assertEquals(NAME, page.getContent().getFirst().getUser().getFullName());
    }

    @Test
    void testFindByIdAndStatus_WithFetchJoin() {
        LegalOffenseEntity offense = createOffense();
        offense.setUser(user);
        offense.setCodeArticle(article);
        offense.setStatus(OffenseStatus.NEW);
        repository.save(offense);

        flushAndClear();

        Optional<LegalOffenseEntity> found = repository.findByIdAndStatus(offense.getId(), OffenseStatus.NEW);

        assertTrue(found.isPresent());
        assertEquals(ARTICLE_TITLE, found.get().getCodeArticle().getTitle());
        assertEquals(NAME, found.get().getUser().getFullName());
    }

    @Test
    void testUpdateWithCourtData() {
        LegalOffenseEntity offense = createOffense();
        repository.save(offense);
        flushAndClear();

        repository.updateWithCourtData(offense.getId(), 500L, COURT_CASE_NUMBER);
        flushAndClear();

        LegalOffenseEntity updated = entityManager.find(LegalOffenseEntity.class, offense.getId());
        assertEquals(OffenseStatus.SENT_TO_COURT, updated.getStatus());
        assertEquals(COURT_CASE_NUMBER, updated.getCourtCaseNumber());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testCountByCreatedAt() {
        repository.save(createOffense());
        flushAndClear();

        int count = repository.countByCreatedAt(LocalDate.now());

        assertEquals(1, count);
    }

    @Test
    void testUpdateStatus_ShouldUpdateDbAndTimestamp() {
        LegalOffenseEntity offense = createOffense();
        offense.setStatus(OffenseStatus.NEW);
        repository.save(offense);

        flushAndClear();

        OffenseStatus newStatus = OffenseStatus.SENT_TO_COURT;
        repository.updateStatus(offense.getId(), newStatus);

        flushAndClear();

        LegalOffenseEntity updated = entityManager.find(LegalOffenseEntity.class, offense.getId());

        assertNotNull(updated);
        assertEquals(newStatus, updated.getStatus());
        assertNotNull(updated.getUpdatedAt());
    }

    private LegalOffenseEntity createOffense() {
        return LegalOffenseEntity.builder()
            .inspector(inspector)
            .user(user)
            .codeArticle(article)
            .offenseLocation("Tashkent, Uzbekistan")
            .description("Sample offense description")
            .status(OffenseStatus.NEW)
            .protocolNumber("FP-" + UUID.randomUUID())
            .offenseDateTime(OffsetDateTime.now())
            .build();
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}