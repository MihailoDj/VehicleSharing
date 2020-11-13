package com.example.mpos_projekat;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceVehicles;
    private List<Vozilo> vozila = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Vozilo> vozila, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceVehicles = mDatabase.getReference("Vozila");
    }

    public void readVehicles (final DataStatus dataStatus) {
        mReferenceVehicles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vozila.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Vozilo vozilo = keyNode.getValue(Vozilo.class);
                    vozila.add(vozilo);
                }
                dataStatus.DataIsLoaded(vozila, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dodajVozilo(Vozilo vozilo, final DataStatus dataStatus) {
        String key = mReferenceVehicles.push().getKey();
        mReferenceVehicles.child(key).setValue(vozilo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void izmeniVozilo(String key, Vozilo vozilo, final DataStatus dataStatus) {
        mReferenceVehicles.child(key).setValue(vozilo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void obrisiVozilo(String key, final DataStatus dataStatus) {
        mReferenceVehicles.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
