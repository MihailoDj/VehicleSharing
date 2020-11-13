package com.example.mpos_projekat;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class Vozilo {

    private String registracija;
    private String tip;
    private int brojPutnika;
    private double lat;
    private double lng;
    private double cenaPoMinutu;
    private boolean rezervisan;
    private String marka;
    private String model;

    public Vozilo() {

    }

    public Vozilo(String tip, String model, String marka, int brojPutnika, double lat, double lng,
                  double cenaPoMinutu, boolean rezervisan, String registracija) {
        this.tip = tip;
        this.brojPutnika = brojPutnika;
        this.lat = lat;
        this.lng = lng;
        this.cenaPoMinutu = cenaPoMinutu;
        this.rezervisan = rezervisan;
        this.registracija = registracija;
        this.marka = marka;
        this.model = model;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }



    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getBrojPutnika() {
        return brojPutnika;
    }

    public void setBrojPutnika(int brojPutnika) {
        this.brojPutnika = brojPutnika;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getCenaPoMinutu() {
        return cenaPoMinutu;
    }

    public void setCenaPoMinutu(double cenaPoMinutu) {
        this.cenaPoMinutu = cenaPoMinutu;
    }

    public boolean isRezervisan() {
        return rezervisan;
    }

    public void setRezervisan(boolean rezervisan) {
        this.rezervisan = rezervisan;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tip: " + this.tip + "\n" +
                "Marka: " + this.marka + "\n" +
                "Model: " + this.model + "\n" +
                "Registracija: " + this.registracija + "\n";
    }
}
