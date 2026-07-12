package com.objectstorage.backend.config;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.objectstorage.backend.modules.user.repository.UserRepository;

@Testcontainers
@ActiveProfiles("test")
public abstract class  AbstractTest {

    @Autowired
    private UserRepository userRepository;


    @ServiceConnection
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withReuse(true);



    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }




}