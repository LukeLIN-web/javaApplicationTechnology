package com.miaoshaproject.service.model;

public class UserModel {
    private Integer id;
    private String name;

    public void setAge(Integer age) {
        this.age = age;
    }

    private Integer gender;
    private Integer age;
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    private String encrptPassword;

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public Integer getAge() {
        return age;
    }

    public String getTelphone() {
        return telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }
}
