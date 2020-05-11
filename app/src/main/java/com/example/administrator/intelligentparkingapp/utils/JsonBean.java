package com.example.administrator.intelligentparkingapp.utils;

public class JsonBean {
    public String msg;
    public Integer status;
    public Integer uid;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "JsonBean{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", uid=" + uid +
                '}';
    }
}