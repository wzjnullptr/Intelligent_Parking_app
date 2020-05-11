package com.example.administrator.intelligentparkingapp.entity;

import java.io.Serializable;

public class Plimit implements Serializable {
    private Integer lid;
    private Integer llimit;

    public Plimit() {
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getLlimit() {
        return llimit;
    }

    public void setLlimit(Integer llimit) {
        this.llimit = llimit;
    }
}
