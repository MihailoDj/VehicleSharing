package com.example.mpos_projekat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class PodesavanjaNalogaActivity extends AppCompatActivity {

    EditText etIme, etPrezime, etLozinka, etEmail, etJMBG, etBrVozackeDozvole;
    Button buttonIzmeniNalog, buttonObrisiNalog;
    String ulogovanKorisnik, key;
    Korisnik k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanja_naloga);

        ulogovanKorisnik = getIntent().getStringExtra("ulogovanKorisnik");

        etIme = findViewById(R.id.etIme);
        etPrezime = findViewById(R.id.etPrezime);
        etEmail = findViewById(R.id.etEmail);
        etLozinka = findViewById(R.id.etLozinka);
        etJMBG = findViewById(R.id.etJMBG);
        etBrVozackeDozvole = findViewById(R.id.etBrVozackeDozvole);
        buttonIzmeniNalog = findViewById(R.id.buttonIzmeniNalog);
        buttonObrisiNalog = findViewById(R.id.buttonObrisiNalog);



        new FirebaseDatabaseHelperKorisnici().vratiKorisnike(new FirebaseDatabaseHelperKorisnici.DataStatus() {
            @Override
            public void DataIsLoaded(List<Korisnik> korisnici, List<String> keys) {
                for (int i = 0; i < korisnici.size(); i++) {
                    if (korisnici.get(i).getEmail().equals(ulogovanKorisnik)) {

                        etIme.setText(korisnici.get(i).getIme());
                        etPrezime.setText(korisnici.get(i).getPrezime());
                        etEmail.setText(korisnici.get(i).getEmail());
                        etJMBG.setText(korisnici.get(i).getJmbg());
                        etBrVozackeDozvole.setText(korisnici.get(i).getBrVozackeDozvole());

                        try {
                            etLozinka.setText(Security.decrypt(korisnici.get(i).getLozinka()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        key = keys.get(i);

                    }
                }
            }
            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        buttonIzmeniNalog.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                k = new Korisnik();

                k.setIme(etIme.getText().toString());
                k.setPrezime(etPrezime.getText().toString());
                k.setEmail(etEmail.getText().toString());
                k.setJmbg(etJMBG.getText().toString());
                k.setBrVozackeDozvole(etBrVozackeDozvole.getText().toString());

                try {
                    k.setLozinka(Security.encrypt(etLozinka.getText().toString()));
                } catch (Exception e) {

                }

                new FirebaseDatabaseHelperKorisnici().izmeniKorisnika(key, k, new FirebaseDatabaseHelperKorisnici.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Korisnik> korisnici, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                        Log.d("Izmenjen korisnik: ", k.toString());
                        Log.d("Redosled u bazi: ", key);
                        Toast.makeText(PodesavanjaNalogaActivity.this, "Uspesno izmenjeni podaci.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
        buttonObrisiNalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelperKorisnici().obrisiKorisnika(key, new FirebaseDatabaseHelperKorisnici.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Korisnik> korisnici, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        startActivity(new Intent(PodesavanjaNalogaActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        });
    }
}
