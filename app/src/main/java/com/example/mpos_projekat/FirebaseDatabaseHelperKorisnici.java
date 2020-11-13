package com.example.mpos_projekat;

import androidx.annotation.NonNull;

import com.example.mpos_projekat.Vozilo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelperKorisnici {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private List<Korisnik> korisnici = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Korisnik> korisnici, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelperKorisnici() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUsers = mDatabase.getReference("Korisnici");
    }

    public void vratiKorisnike (final com.example.mpos_projekat.FirebaseDatabaseHelperKorisnici.DataStatus dataStatus) {
        mReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                korisnici.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Korisnik korisnik = keyNode.getValue(Korisnik.class);
                    korisnici.add(korisnik);
                }
                dataStatus.DataIsLoaded(korisnici, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dodajKorisnika(Korisnik korisnik, final DataStatus dataStatus) {
        String key = mReferenceUsers.push().getKey();
        mReferenceUsers.child(key).setValue(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void izmeniKorisnika(String key, Korisnik korisnik,
                                final com.example.mpos_projekat.FirebaseDatabaseHelperKorisnici.DataStatus dataStatus) {
        mReferenceUsers.child(key).setValue(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void obrisiKorisnika(String key, final com.example.mpos_projekat.FirebaseDatabaseHelperKorisnici.DataStatus dataStatus) {
        mReferenceUsers.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}