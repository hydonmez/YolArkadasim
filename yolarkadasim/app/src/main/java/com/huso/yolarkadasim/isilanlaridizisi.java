package com.huso.yolarkadasim;

import java.io.Serializable;

public class isilanlaridizisi implements Serializable {
    //is ilanindan gonderilen degerleri bir dize seklinde tutuyor

    private String isilaniid;
    private String isinadi;
    private String varisisinadi;
    private String isilaniverenkullanici;
    private String isilaninintarihi;

    public isilanlaridizisi(String isilaniid, String isinadi, String varisisinadi,String isilaniverenkullanici,String isilaninintarihi) {
        this.isilaniid = isilaniid;
        this.isinadi = isinadi;
        this.varisisinadi=varisisinadi;
        this.isilaniverenkullanici=isilaniverenkullanici;
        this.isilaninintarihi=isilaninintarihi;
    }

    public String getIsilaniid() {
        return isilaniid;
    }

    public void setIsilaniid(String isilaniid) {
        this.isilaniid = isilaniid;
    }

    public String getIsinadi() {
        return isinadi;
    }

    public void setIsinadi(String isinadi) {
        this.isinadi = isinadi;
    }

    public String getvarisIsinadi() {
        return varisisinadi;
    }

    public void setvarisIsinadi(String isinadi) {
        this.isinadi = varisisinadi;
    }

    public String getIsilaniverenkullanici(){
        return isilaniverenkullanici;
    }
    public void setIsilaniverenkullanici(String isilaniverenkullanici){
        this.isilaniverenkullanici=isilaniverenkullanici;
    }

    public  String getIsilaninintarihi(){
        return isilaninintarihi;
    }
    public void setIsilaninintarihi(String isilaninintarihi){
        this.isilaninintarihi=isilaninintarihi;
    }
}
