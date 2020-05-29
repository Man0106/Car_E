package com.example.care_driver;

import androidx.annotation.NonNull;

public class CarId {
    String carId;
    public <T extends CarId>T withId(@NonNull final String id){
        this.carId = id;
        return (T) this;
    }
}
