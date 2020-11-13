package com.example.mpos_projekat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class DodajVoziloActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText etBrojPutnika, etLokacija, etCenaPoMinutu, etRegistracija, etMarka, etModel;
    Button buttonDodaj;
    ArrayAdapter<CharSequence> adapter;
    String tip = "";

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_vozilo);

        etBrojPutnika = findViewById(R.id.etBrojPutnika);
        etLokacija = findViewById(R.id.etLokacija);
        etCenaPoMinutu = findViewById(R.id.etCenaPoMinutu);
        etRegistracija = findViewById(R.id.etRegistracija);
        etMarka = findViewById(R.id.etMarka);
        etModel = findViewById(R.id.etModel);
        spinner = findViewById(R.id.spinner);
        buttonDodaj = findViewById(R.id.buttonIzmeniNalog);

        database = FirebaseDatabase.getInstance().getReference("Vozila");

        adapter = ArrayAdapter.createFromResource(this, R.array.tipovi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(this);

        buttonDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vozilo vozilo = new Vozilo();

                int brojPutnika = Integer.parseInt(etBrojPutnika.getText().toString());
                String lokacija = etLokacija.getText().toString();
                double cenaPoMinutu = Double.parseDouble(etCenaPoMinutu.getText().toString());
                String registracija = etRegistracija.getText().toString();
                String marka = etMarka.getText().toString();
                String model = etModel.getText().toString();

                double lat = getLocationFromAddress(getApplicationContext(), lokacija).latitude;
                double lng = getLocationFromAddress(getApplicationContext(), lokacija).longitude;

                vozilo.setTip(tip);
                vozilo.setMarka(marka);
                vozilo.setModel(model);
                vozilo.setBrojPutnika(brojPutnika);
                vozilo.setCenaPoMinutu(cenaPoMinutu);
                vozilo.setRegistracija(registracija);
                vozilo.setRezervisan(false);
                vozilo.setLat(lat);
                vozilo.setLng(lng);

                new FirebaseDatabaseHelper().dodajVozilo(vozilo, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(DodajVoziloActivity.this, "Uspesan unos.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tip = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
