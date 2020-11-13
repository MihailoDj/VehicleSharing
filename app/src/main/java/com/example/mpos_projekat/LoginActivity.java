package com.example.mpos_projekat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    EditText etLozinka;
    Button buttonLogin;
    ConstraintLayout layoutLogin;
    boolean emailPostoji = false;

    String ulogovanKorisnik = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etLozinka = findViewById(R.id.etLozinka);
        buttonLogin = findViewById(R.id.buttonLogin);
        layoutLogin = findViewById(R.id.layoutLogin);

        buttonLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonLogin:
                if (!etEmail.getText().toString().isEmpty() && !etLozinka.getText().toString().isEmpty()) {

                    new FirebaseDatabaseHelperKorisnici().vratiKorisnike(new FirebaseDatabaseHelperKorisnici.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Korisnik> korisnici, List<String> keys) {
                            for (Korisnik korisnik:korisnici) {

                                if (etEmail.getText().toString().equals(korisnik.getEmail())) {
                                    emailPostoji = true;

                                    try {
                                        if (Security.decrypt(korisnik.getLozinka()).equals(etLozinka.getText().toString())) {
                                            if (etEmail.getText().toString().equals("admin@carsharing.com")) {
                                                Intent intent = new Intent(LoginActivity.this, AdminPanelNovi.class);
                                                startActivity(intent);
                                                break;
                                            } else {
                                                ulogovanKorisnik = etEmail.getText().toString();

                                                Intent intent = new Intent(LoginActivity.this, IzborTipaActivity.class);
                                                intent.putExtra("ulogovanKorisnik", ulogovanKorisnik);

                                                startActivity(intent);
                                                break;
                                            }
                                        } else {
                                            etLozinka.setError("POGRESNA LOZINKA");
                                            buttonLogin.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.shake));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            if (!emailPostoji) {
                                etEmail.setError("NEVALIDAN E-MAIL");
                                buttonLogin.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.shake));
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



                } else {
                    Toast.makeText(this, "Greška, unesite korisničko ime i lozinku.", Toast.LENGTH_SHORT).show();

                    buttonLogin.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                }
        }
    }
}
