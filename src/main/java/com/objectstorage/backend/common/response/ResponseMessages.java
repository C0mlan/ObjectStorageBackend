package com.objectstorage.backend.common.response;


public final class ResponseMessages {

    /**
     * Centralised factory for standard success response messages used across the application.
     */

    private ResponseMessages() {
    }

    public static final class Resource {

        private Resource() {
        }

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

    public static final class Auth {

        private Auth() {
        }

        public static String loginSuccessful() {
            return "Login successful.";
        }

        public static String logoutSuccessful() {
            return "Logout successful.";
        }

        public static String tokenRefreshed() {
            return "Token refreshed successfully.";
        }

        public static String passwordChanged() {
            return "Password changed successfully.";
        }

        public static String passwordReset() {
            return "Password reset successfully.";
        }
    }
}