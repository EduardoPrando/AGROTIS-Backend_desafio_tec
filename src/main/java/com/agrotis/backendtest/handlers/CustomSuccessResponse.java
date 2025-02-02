package com.agrotis.backendtest.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class CustomSuccessResponse<E> extends CustomResponse {

    private E body;

    private String message;

    public CustomSuccessResponse(Boolean success, LocalDateTime timestamp, int status, E body, String message) {
        super(success, timestamp, status);
        this.body = body;
    }

    public CustomSuccessResponse(Boolean success, LocalDateTime timestamp, int status, E body) {
        super(success, timestamp, status);
        this.body = body;
    }

    public CustomSuccessResponse(Boolean success, LocalDateTime timestamp, int status) {
        super(success, timestamp, status);
    }

    public E getBody() {
        return body;
    }

    public void setBody(E body) {
        this.body = body;
    }

    public static <E> ResponseEntity<CustomSuccessResponse<E>> created(E body, String message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CustomSuccessResponse<>(true, LocalDateTime.now(), 201, body, message));
    }

    public static <E> ResponseEntity<CustomSuccessResponse<E>> created(E body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CustomSuccessResponse<>(true, LocalDateTime.now(), 201, body));
    }

    public static <E> ResponseEntity<?> created() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CustomSuccessResponse<E>(true, LocalDateTime.now(), 201));
    }

    public static ResponseEntity<?> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public static <E> ResponseEntity<CustomSuccessResponse<E>> ok() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomSuccessResponse<>(true, LocalDateTime.now(), 200));
    }

    public static <E> ResponseEntity<CustomSuccessResponse<E>> ok(E body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomSuccessResponse<>(true, LocalDateTime.now(), 200, body));
    }
}