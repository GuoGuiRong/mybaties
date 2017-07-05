package com.ggr.pojo;

/**
 * Created by GuiRunning on 2017/7/5.
 */
public class PhoneNumber{

    private String countryCode;
    private String stateCode;
    private String number;
    public PhoneNumber(){
    }

    public PhoneNumber(String phone){
        String[] temp = phone.split("-");
        this.countryCode = temp[0];
        this.stateCode =  temp[1];
        this.number =  temp[2];
    }
    public PhoneNumber(String countryCode, String stateCode, String number) {
        this.countryCode = countryCode;
        this.stateCode = stateCode;
        this.number = number;
    }
    public String getAsString() {
        return countryCode + "-" + stateCode + "-" + number;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    public String getStateCode() {
        return stateCode;
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
