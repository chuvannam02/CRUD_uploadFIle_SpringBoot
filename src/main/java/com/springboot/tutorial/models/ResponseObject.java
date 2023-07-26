package com.springboot.tutorial.models;

// POJO = Plain object Java Object Để convert sang dạng JSON thì mobile, web mới có thể đọc được
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

    //default Constructor
    public ResponseObject() {}

    // parameterized Constructor
    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getter and setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
