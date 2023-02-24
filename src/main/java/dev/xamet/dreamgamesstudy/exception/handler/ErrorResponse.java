package dev.xamet.dreamgamesstudy.exception.handler;

/*
 * Simple Error Response Body with a message and a status code
 */
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
