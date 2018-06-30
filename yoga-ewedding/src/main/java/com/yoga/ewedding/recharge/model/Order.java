package com.yoga.ewedding.recharge.model;

import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;

import javax.persistence.*;
import java.util.Date;

public class Order extends Recharge {
    private String username;
    private String fullname;
    private String phone;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}