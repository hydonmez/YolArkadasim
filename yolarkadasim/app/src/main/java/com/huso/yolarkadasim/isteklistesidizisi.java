package com.huso.yolarkadasim;

import java.io.Serializable;

public class isteklistesidizisi implements Serializable {
    private String isteklistesiid;
    private String isteklistesiadvesoyad;
    private String basvurankisi;
    private String basvurulanisilaniid;
    private String isteklistesisehir;
    private String Tarih;
    private String isinadi;

    //isteklistesindeki degerleri alarak isteklistesinde diziye aktarma islemleri gerceklestirilir
    public isteklistesidizisi(String isteklistesiid, String isteklistesiadvesoyad, String basvurankisi, String basvurulanisilaniid,String isteklistesisehir,String Tarih,String isinadi) {
        this.isteklistesiid = isteklistesiid;
        this.isteklistesiadvesoyad = isteklistesiadvesoyad;
        this.basvurankisi = basvurankisi;
        this.basvurulanisilaniid = basvurulanisilaniid;
        this.isteklistesisehir=isteklistesisehir;
        this.Tarih=Tarih;
        this.isinadi=isinadi;
    }

    public String getIsteklistesiid() {
        return isteklistesiid;
    }

    public void setIsteklistesiid(String isteklistesiid) {
        this.isteklistesiid = isteklistesiid;
    }

    public String getIsteklistesiadvesoyad() {
        return isteklistesiadvesoyad;
    }

    public void setIsteklistesiadvesoyad(String isteklistesiadvesoyad) {
        this.isteklistesiadvesoyad = isteklistesiadvesoyad;
    }

    public String getBasvurankisi() {
        return basvurankisi;
    }

    public void setBasvurankisi(String basvurankisi) {
        this.basvurankisi = basvurankisi;
    }

    public String getBasvurulanisilaniid() {
        return basvurulanisilaniid;
    }

    public void setBasvurulanisilaniid(String basvurulanisilaniid) {
        this.basvurulanisilaniid = basvurulanisilaniid;
    }

    public String getIsteklistesisehir(){
        return isteklistesisehir;
    }
    public void setIsteklistesisehir(String isteklistesisehir){
        this.isteklistesisehir=isteklistesisehir;
    }

    public String getTarih(){
        return Tarih;
    }
    public void setTarih(String Tarih){
        this.Tarih=Tarih;
    }

    public String getIsinadi(){
        return isinadi;
    }
    public void setIsinadi(String isinadi){
        this.isinadi=isinadi;
    }
}