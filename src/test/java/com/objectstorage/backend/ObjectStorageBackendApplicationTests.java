package com.objectstorage.backend;

import com.objectstorage.backend.config.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ObjectStorageBackendApplicationTests extends AbstractTest {


    @Test
    void contextLoads() {
    }
}