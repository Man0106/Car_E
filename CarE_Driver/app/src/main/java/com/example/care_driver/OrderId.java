package com.example.care_driver;

import androidx.annotation.NonNull;

public class OrderId {
    String orderId;
    public <T extends OrderId>T withId(@NonNull final String id){
        this.orderId = id;
        return (T) this;
    }
}
