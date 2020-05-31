package com.example.car_e;

import androidx.annotation.NonNull;

public class RequestId {
    String requestId;
    public <T extends RequestId>T withId(@NonNull final String id){
        this.requestId = id;
        return (T) this;
    }
}
