package com.agrotis.backendtest.handlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public abstract class CustomResponse {
    private Boolean success;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;

    public CustomResponse(Boolean success, LocalDateTime timestamp, int status) {
        this.success = success;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}