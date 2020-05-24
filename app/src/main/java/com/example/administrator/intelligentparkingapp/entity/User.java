package com.example.administrator.intelligentparkingapp.entity;

import java.io.Serializable;

public class User implements Serializable{
private String uname;
private String upwd;
private Integer uid;
private Plimit plimit;
private String ucall;

public Plimit getPlimit() {
        return plimit;
        }

public void setPlimit(Plimit plimit) {
        this.plimit = plimit;
        }

public Double getUmoney() {
        return umoney;
        }

public void setUmoney(Double umoney) {
        this.umoney = umoney;
        }
private double umoney;
public User(){}

public User(Integer uid,String uname,String upwd,String ucall,Double umoney) {
        this.umoney=umoney;
        this.uname = uname;
        this.upwd = upwd;
        this.uid = uid;
        this.ucall = ucall;
        }

public User(Integer uid,String upwd,String uname) {
        this.uname = uname;
        this.uid = uid;
        this.upwd = upwd;
        }

public String getUname() {
        return uname;
        }

public void setUname(String uname) {
        this.uname = uname;
        }

public String getUpwd() {
        return upwd;
        }

public void setUpwd(String upwd) {
        this.upwd = upwd;
        }

public String getUcall() {
        return ucall;
        }

public void setUcall(String ucall) {
        this.ucall = ucall;
        }

public Integer getUid() {
        return uid;
        }

public void setUid(Integer uid) {
        this.uid = uid;
        }

        @Override
        public String toString() {
                return "User{" +
                        "id=" + uid+
                        ", pwd='" + upwd + '\'' +
                        ", name='" + uname + '\'' +
                        ", money='" + umoney + '\'' +
                        '}';
        }
        }
