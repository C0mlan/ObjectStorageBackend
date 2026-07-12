package com.objectstorage.backend.common.response;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ResponseMessagesTest {

    @Test
    void created_shouldReturnExpectedMessage() {
        assertEquals(
                "User created successfully.",
                ResponseMessages.created("User")
        );
    }

    @Test
    void updated_shouldReturnExpectedMessage() {
        assertEquals(
                "User updated successfully.",
                ResponseMessages.updated("User")
        );
    }

    @Test
    void deleted_shouldReturnExpectedMessage() {
        assertEquals(
                "User deleted successfully.",
                ResponseMessages.deleted("User")
        );
    }

    @Test
    void retrieved_shouldReturnExpectedMessage() {
        assertEquals(
                "User retrieved successfully.",
                ResponseMessages.retrieved("User")
        );
    }



}
