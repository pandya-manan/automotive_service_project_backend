package com.automotive.callcentre.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CALL_CENTRE_AGENT")
public class CallCenterAgent extends User{

    @Override
    public String getType() {
        return "CALL_CENTRE_AGENT";
    }
}
