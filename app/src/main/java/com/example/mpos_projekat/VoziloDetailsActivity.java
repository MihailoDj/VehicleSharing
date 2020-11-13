package com.example.mpos_projekat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class VoziloDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    EditText etBrojPutnika, etLokacija, etCenaPoMinutu, etRegistracija, etMarka, etModel;
    Button buttonIzmeniVozilo, buttonObrisi;
    ArrayAdapter<CharSequence> adapter;

    private String key, tip, marka, model, registracija, rezervisan, lokacija, cena, putnici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vozilo_details);

        key = getIntent().getStringExtra("key");
        tip = getIntent().getStringExtra("tip");
        marka = getIntent().getStringExtra("marka");
        model = getIntent().getStringExtra("model");
        registracija = getIntent().getStringExtra("registracija");
        rezervisan = getIntent().getStringExtra("rezervisan");
        lokacija = getIntent().getStringExtra("lokacija");
        cena = getIntent().getStringExtra("cena");
        putnici = getIntent().getStringExtra("putnici");

        etBrojPutnika = findViewById(R.id.etBrojPutnika);
        etBrojPutnika.setText(putnici);

        etLokacija = findViewById(R.id.etLokacija);
        etLokacija.setText(lokacija);

        etCenaPoMinutu = findViewById(R.id.etCenaPoMinutu);
        etCenaPoMinutu.setText(cena);

        etRegistracija = findViewById(R.id.etRegistracija);
        etRegistracija.setText(registracija);

        etMarka = findViewById(R.id.etMarka);
        etMarka.setText(marka);

        etModel = findViewById(R.id.etModel);
        etModel.setText(model);

        spinner = findViewById(R.id.spinner);
        buttonIzmeniVozilo = findViewById(R.id.buttonIzmeniVozilo);
        buttonObrisi = findViewById(R.id.buttonObrisi);

        adapter = ArrayAdapter.createFromResource(this, R.array.tipovi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(getIndex_SpinnerItem(spinner, tip));

        buttonIzmeniVozilo.setOnClickListener(this);
        buttonObrisi.setOnClickListener(this);
    }

    private int getIndex_SpinnerItem(Spinner spinner, String item) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(item)) {
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonIzmeniVozilo:
                Vozilo vozilo = new Vozilo();

                vozilo.setMarka(etMarka.getText().toString());
                vozilo.setModel(etModel.getText().toString());
                vozilo.setRegistracija(etRegistracija.getText().toString());
                vozilo.setCenaPoMinutu(Double.parseDouble(etCenaPoMinutu.getText().toString()));
                vozilo.setBrojPutnika(Integer.parseInt(etBrojPutnika.getText().toString()));
                vozilo.setTip(spinner.getSelectedItem().toString());
                vozilo.setLat(getLocationFromAddress(this, etLokacija.getText().toString()).latitude);
                vozilo.setLng(getLocationFromAddress(this, etLokacija.getText().toString()).longitude);

                if (rezervisan.equals("zauzet")) {
                    vozilo.setRezervisan(true);
                } else {
                    vozilo.setRezervisan(false);
                }


                new FirebaseDatabaseHelper().izmeniVozilo(key, vozilo, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(VoziloDetailsActivity.this, "Uspesno izmenjeni podaci.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

                break;
            case R.id.buttonObrisi:
                new FirebaseDatabaseHelper().obrisiVozilo(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                    }
                    @Override
                    public void DataIsInserted() {

                    }
                    @Override
                    public void DataIsUpdated() {

                    }
                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(VoziloDetailsActivity.this, "Uspesno brisanje vozila.", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                });
                break;
        }
    }

    public LatLng getLocationFromAddress (Context context, String strAddress){

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
