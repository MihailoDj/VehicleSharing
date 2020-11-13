package com.example.mpos_projekat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText etIme, etPrezime, etEmail, etLozinka,etBrVozackeDozvole, etPotvrdiLozinku, etJMBG;
    Button buttonRegistracija;
    ProgressDialog mProgress;
    boolean emailRegistrovan = false, JMBGRegistrovan = false, brVozackeDozvoleRegistrovan = false, korisnikPostojiUBazi = false;
    String ime, prezime, lozinka, potvrdiLozinku, email, brVozackeDozvole, JMBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etIme = findViewById(R.id.etIme);
        etPrezime = findViewById(R.id.etPrezime);
        etEmail = findViewById(R.id.etEmail);
        etLozinka = findViewById(R.id.etLozinka);
        etPotvrdiLozinku = findViewById(R.id.etPotvrdiLozinku);
        etBrVozackeDozvole = findViewById(R.id.etBrVozackeDozvole);
        etJMBG = findViewById(R.id.etJMBG);
        buttonRegistracija = findViewById(R.id.buttonIzmeniNalog);

        buttonRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Korisnik korisnik = new Korisnik();

                ime = etIme.getText().toString();
                prezime = etPrezime.getText().toString();
                email = etEmail.getText().toString();
                lozinka = etLozinka.getText().toString();
                brVozackeDozvole = etBrVozackeDozvole.getText().toString();
                JMBG = etJMBG.getText().toString();

                if (isIsparavanUnos()) {
                    korisnik.setIme(ime);
                    korisnik.setPrezime(prezime);
                    korisnik.setEmail(email);
                    korisnik.setBrVozackeDozvole(brVozackeDozvole);
                    korisnik.setJmbg(JMBG);

                    try {
                        korisnik.setLozinka(Security.encrypt(lozinka));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new FirebaseDatabaseHelperKorisnici().dodajKorisnika(korisnik, new FirebaseDatabaseHelperKorisnici.DataStatus() {
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

                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Uspesno ste se registrovali.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    buttonRegistracija.startAnimation(AnimationUtils.loadAnimation(RegisterActivity.this,R.anim.shake));
                    Toast.makeText(RegisterActivity.this, "Pogresno uneti podaci.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        etEmail.setText("");
        etIme.setText("");
        etPotvrdiLozinku.setText("");
        etPrezime.setText("");
        etJMBG.setText("");
        etBrVozackeDozvole.setText("");
        etLozinka.setText("");
    }

    private boolean isIsparavanUnos() {
        boolean ispravanUnos = true;

        if (etIme.getText().toString().isEmpty()) {
            etIme.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etPrezime.getText().toString().isEmpty()) {
            etPrezime.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etLozinka.getText().toString().isEmpty()) {
            etLozinka.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etPotvrdiLozinku.getText().toString().isEmpty()) {
            etPotvrdiLozinku.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etBrVozackeDozvole.getText().toString().isEmpty()) {
            etBrVozackeDozvole.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (etJMBG.getText().toString().isEmpty()) {
            etJMBG.setError("NEOPHODAN UNOS");
            ispravanUnos = false;
        }
        if (!etLozinka.getText().toString().equals(etPotvrdiLozinku.getText().toString())) {
            etPotvrdiLozinku.setError("POTVRDITE LOZINKU");
            ispravanUnos = false;
        }
        if (etBrVozackeDozvole.getText().toString().length() != 9) {
            etBrVozackeDozvole.setError("MORA IMATI 9 CIFARA");
            ispravanUnos = false;
        }
        if (etJMBG.getText().toString().length() != 13) {
            etJMBG.setError("MORA IMATI 13 CIFARA");
            ispravanUnos = false;
        }
        if (etLozinka.getText().toString().length() < 8) {
            etLozinka.setError("MORA IMATI BAR 8 KARAKTERA");
            ispravanUnos = false;
        }
        if (!etEmail.getText().toString().contains("@") || !etEmail.getText().toString().contains(".com")) {
            etEmail.setError("UNESITE VALIDNU E-MAIL ADRESU");
            ispravanUnos = false;
        }

        return ispravanUnos;
    }

}
