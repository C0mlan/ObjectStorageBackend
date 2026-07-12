package com.objectstorage.backend.common.response;


public final class ResponseMessages {

    private ResponseMessages() {}

    public static String created(String resource) {
        return resource + " created successfully.";
    }

    public static String updated(String resource) {
        return resource + " updated successfully.";
    }

    public static String deleted(String resource) {
        return resource + " deleted successfully.";
    }

    public static String retrieved(String resource) {
        return resource + " retrieved successfully.";
    }
}
