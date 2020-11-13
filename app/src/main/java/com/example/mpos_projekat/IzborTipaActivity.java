package com.example.mpos_projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class IzborTipaActivity extends AppCompatActivity {

    ImageView imageViewAuto;
    ImageView imageViewBicikl;
    ImageView imageViewSkuter;
    ImageView imageViewTrotinet;

    String ulogovanKorisnik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izbor_tipa);

        imageViewAuto = findViewById(R.id.imageViewAuto);
        imageViewBicikl = findViewById(R.id.imageViewBicikl);
        imageViewSkuter = findViewById(R.id.imageViewSkuter);
        imageViewTrotinet = findViewById(R.id.imageViewTrotinet);

        ulogovanKorisnik = getIntent().getStringExtra("ulogovanKorisnik");
    }

    public void izaberiTipVozila(View v) {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        String tip = "";

        switch(v.getId()) {
            case R.id.imageViewAuto:
                tip = "Auto";
                break;
            case R.id.imageViewBicikl:
                tip = "Bicikl";
                break;
            case R.id.imageViewSkuter:
                tip = "Skuter";
                break;
            case R.id.imageViewTrotinet:
                tip = "Trotinet";
                break;

        }
        intent.putExtra("tipVozila", tip);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(this, LoginRegisterActivity.class));
                return true;
            case R.id.settingsItem:
                Intent intent = new Intent(this, PodesavanjaNalogaActivity.class);
                intent.putExtra("ulogovanKorisnik", ulogovanKorisnik);

                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
