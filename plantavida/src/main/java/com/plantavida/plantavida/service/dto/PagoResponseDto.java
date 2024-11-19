package com.plantavida.plantavida.service.dto;

public class PagoResponseDto {
    private String status;
    private String transactionId;
    private String message;

    public PagoResponseDto(String failure, Object o, String s) {

    }

    public PagoResponseDto() {

    }

    // Getters y setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

