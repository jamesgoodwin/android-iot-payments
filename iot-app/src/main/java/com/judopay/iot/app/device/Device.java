package com.judopay.iot.app.device;

public class Device {

    private String id;
    private String description;
    private boolean authorized;

    public Device(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAuthorized() {
        return authorized;
    }
}
