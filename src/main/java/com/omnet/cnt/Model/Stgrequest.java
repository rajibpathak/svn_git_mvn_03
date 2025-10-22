package com.omnet.cnt.Model;

public class Stgrequest {

    private String sbiNo;
    private Rowdata data;

    // Getters and Setters
    public String getSbiNo() {
        return sbiNo;
    }

    public void setSbiNo(String sbiNo) {
        this.sbiNo = sbiNo;
    }

    public Rowdata getData() {
        return data;
    }

    public void setData(Rowdata data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SaveRequest{" +
                "sbiNo='" + sbiNo + '\'' +
                ", data=" + data +
                '}';
    }
}