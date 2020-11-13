package com.example.mpos_projekat;

import androidx.annotation.NonNull;

public class Korisnik {

    private String lozinka;
    private String ime;
    private String prezime;
    private String jmbg;
    private String brVozackeDozvole;
    private String email;

    public Korisnik () {

    }

    public Korisnik(String email, String lozinka, String ime, String prezime, String jmbg, String brVozackeDozvole) {
        this.email = email;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.brVozackeDozvole = brVozackeDozvole;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getBrVozackeDozvole() {
        return brVozackeDozvole;
    }

    public void setBrVozackeDozvole(String brVozackeDozvole) {
        this.brVozackeDozvole = brVozackeDozvole;
    }



    @Override
    public String toString() {
        return "Korisnik{" +
                "email='" + email + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", brVozackeDozvole='" + brVozackeDozvole + '\'' +
                '}';
    }
}
