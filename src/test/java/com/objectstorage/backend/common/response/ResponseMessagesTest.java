package com.objectstorage.backend.common.response;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ResponseMessagesTest {

    @Test
    void created_shouldReturnExpectedMessage() {
        assertEquals(
                "User created successfully.",
                ResponseMessages.Resource.created("User")
        );
    }

    @Test
    void updated_shouldReturnExpectedMessage() {
        assertEquals(
                "User updated successfully.",
                ResponseMessages.Resource.updated("User")
        );
    }

    @Test
    void deleted_shouldReturnExpectedMessage() {
        assertEquals(
                "User deleted successfully.",
                ResponseMessages.Resource.deleted("User")
        );
    }

    @Test
    void retrieved_shouldReturnExpectedMessage() {
        assertEquals(
                "User retrieved successfully.",
                ResponseMessages.Resource.retrieved("User")
        );
    }



}
