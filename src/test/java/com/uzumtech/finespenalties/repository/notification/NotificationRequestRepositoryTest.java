package com.uzumtech.finespenalties.repository.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class NotificationRequestRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private NotificationRequestRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private NotificationRequestEntity entity;
    private final UUID testRequestId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        entity = NotificationRequestEntity.builder()
            .requestId(testRequestId)
            .notificationServiceId(101L)
            .notificationReceiver("user@example.com")
            .notificationText("Hello World")
            .notificationType(NotificationType.EMAIL)
            .requestStatus(NotificationRequestStatus.NEW)
            .build();
    }

    @Test
    void testFindByNotificationServiceId() {
        repository.save(entity);

        flushAndClear();

        Optional<NotificationRequestEntity> found = repository.findByNotificationServiceId(101L);

        assertTrue(found.isPresent());
        assertEquals(testRequestId, found.get().getRequestId());
    }

    @Test
    void testUpdateRequestStatus() {
        repository.save(entity);
        entityManager.flush();

        repository.updateRequestStatus(testRequestId, NotificationRequestStatus.DELIVERED);

        flushAndClear();

        NotificationRequestEntity updated = entityManager.find(NotificationRequestEntity.class, entity.getId());
        assertNotNull(updated);
        assertEquals(NotificationRequestStatus.DELIVERED, updated.getRequestStatus());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testMarkAsDelivered() {
        repository.save(entity);
        entityManager.flush();

        Long newServiceId = 202L;
        repository.markAsDelivered(testRequestId, newServiceId);

        flushAndClear();

        NotificationRequestEntity updated = entityManager.find(NotificationRequestEntity.class, entity.getId());
        assertNotNull(updated);
        assertEquals(NotificationRequestStatus.DELIVERED, updated.getRequestStatus());
        assertEquals(newServiceId, updated.getNotificationServiceId());
    }

    @Test
    void testIsAvailableForProcessing() {
        // NEW entity should be available
        repository.save(entity);

        flushAndClear();

        assertTrue(repository.isAvailableForProcessing(testRequestId));

        // DELIVERED entity should NOT be available
        repository.markAsDelivered(testRequestId, 1L);

        flushAndClear();

        assertFalse(repository.isAvailableForProcessing(testRequestId));
    }

    @Test
    void testIsAvailableForProcessing_False() {
        entity.setRequestStatus(NotificationRequestStatus.DELIVERED);
        repository.save(entity);
        entityManager.flush();

        boolean available = repository.isAvailableForProcessing(testRequestId);

        assertFalse(available);
    }


    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}