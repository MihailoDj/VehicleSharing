package com.example.mpos_projekat;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.List;
import java.util.Random;


public class PopupDialog extends AppCompatDialogFragment {
    TextView txtModel, txtMarka, txtBrPutnikcaLabel, txtBrPutnika, txtRegistracijaLabel, txtRegistracija, txtCenaLabel, txtCena, txtSati;
    private ExampleDialogListener listener;
    EditText etBrojSati;
    String model,marka,registracija, kljuc, tip;
    boolean rezervisan;
    double lat, lng, cena;
    int brPutnika;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);

        txtModel = view.findViewById(R.id.txtModel);
        txtMarka = view.findViewById(R.id.txtMarka);
        txtBrPutnikcaLabel = view.findViewById(R.id.txtBrPutnikaLabel);
        txtBrPutnika = view.findViewById(R.id.txtBrPutnika);
        txtRegistracija = view.findViewById(R.id.txtRegistracija);
        txtRegistracijaLabel = view.findViewById(R.id.txtRegistracijaLabel);
        txtCena = view.findViewById(R.id.txtCena);
        txtCenaLabel = view.findViewById(R.id.txtCenaLabel);
        etBrojSati = view.findViewById(R.id.etBrojSati);
        txtSati = view.findViewById(R.id.txtSati);

        Bundle bundle = getArguments();

        marka = bundle.getString("marka", "");
        model = bundle.getString("model", "");
        cena = bundle.getDouble("cena", 0);
        brPutnika = bundle.getInt("brPutnika", 0);
        registracija = bundle.getString("registracija", "");
        lat = bundle.getDouble("lat", 0);
        lng = bundle.getDouble("lng", 0);
        rezervisan = bundle.getBoolean("rezervisan", false);
        tip = bundle.getString("tip", "");

        kljuc = bundle.getString("kljuc", "");

        txtMarka.setText(Character.toString(marka.charAt(0)).toUpperCase() + marka.substring(1));
        txtModel.setText(Character.toString(model.charAt(0)).toUpperCase() + model.substring(1));
        txtCena.setText(cena*60 + " RSD");
        txtBrPutnika.setText(String.valueOf(brPutnika));
        txtRegistracija.setText(registracija);


        builder.setView(view)
                .setTitle("Rezervisi")
                .setNegativeButton("odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("potvrdi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!etBrojSati.getText().toString().isEmpty() && !(Integer.parseInt(etBrojSati.getText().toString()) == 0)) {
                            ((GoogleMapActivity)getActivity()).prikaziNotifikaciju("Sa racuna vam je skinuto "
                                    + cena * Double.parseDouble(etBrojSati.getText().toString())
                                    + " RSD\nVas kod za pristup vozilu je: " + kodZaPistup(8)
                                    + "\nRegistracija vozila je " + registracija, "Uspesna rezervacija");

                            Vozilo vozilo = new Vozilo();

                            vozilo.setMarka(marka);
                            vozilo.setModel(model);
                            vozilo.setTip(tip);
                            vozilo.setRezervisan(true);
                            vozilo.setRegistracija(registracija);
                            vozilo.setLat(lat);
                            vozilo.setLng(lng);
                            vozilo.setCenaPoMinutu(cena);
                            vozilo.setBrojPutnika(brPutnika);

                            ((GoogleMapActivity)getActivity()).brMinuta = Integer.parseInt(etBrojSati.getText().toString());
                            ((GoogleMapActivity)getActivity()).pokreniTimer();
                            ((GoogleMapActivity)getActivity()).vecRezervisao = true;


                            new FirebaseDatabaseHelper().izmeniVozilo(kljuc, vozilo, new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {

                                }

                                @Override
                                public void DataIsUpdated() {

                                    dismiss();
                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            });
                        } else {
                            Toast.makeText((GoogleMapActivity)getActivity(), "Unesite trajanje voznje.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        return builder.create();
    }


    public interface ExampleDialogListener {

    }

    private static final String ALLOWED_CHARACTERS ="0123456789abcdefghijklmnopqrstuvwxyz";

    private static String kodZaPistup(final int sizeOfRandomString)
    {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);

        for(int i = 0; i < sizeOfRandomString ; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}