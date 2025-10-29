package com.automotive.callcentre.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value="ADMIN")
public class AdminUser extends User{
    @Override
    public String getType() {
        return "ADMIN";
    }
}
