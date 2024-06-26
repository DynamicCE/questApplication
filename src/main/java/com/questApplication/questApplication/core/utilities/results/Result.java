package com.questApplication.questApplication.core.utilities.results;

public class Result {
    private final boolean success;
    private final String message;

    public Result(boolean success) {
        this(success, null);
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
