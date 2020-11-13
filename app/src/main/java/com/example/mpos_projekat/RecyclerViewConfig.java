package com.example.mpos_projekat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecyclerViewConfig {
    private Context mContext;
    private VoziloAdapter mVoziloAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Vozilo> vozila, List<String> keys) {
        mContext = context;
        mVoziloAdapter = new VoziloAdapter(vozila, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mVoziloAdapter);
    }


    class VehicleItemView extends RecyclerView.ViewHolder {
        private TextView mTip;
        private TextView mMarka;
        private TextView mModel;
        private TextView mRegistracija;
        private TextView mRezervisan;
        private TextView mCena;
        private TextView mBrPutnika;
        private TextView mLokacija;
        private String key;

        public VehicleItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.vozila_list_item, parent, false));
            mTip = itemView.findViewById(R.id.txtTip);
            mMarka = itemView.findViewById(R.id.txtMarka);
            mModel = itemView.findViewById(R.id.txtModel);
            mRegistracija = itemView.findViewById(R.id.txtRegistracija);
            mRezervisan = itemView.findViewById(R.id.txtRezervisan);
            mCena = itemView.findViewById(R.id.txtCena);
            mBrPutnika = itemView.findViewById(R.id.txtBrPutnika);
            mLokacija = itemView.findViewById(R.id.txtLokacija);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VoziloDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("tip", mTip.getText().toString());
                    intent.putExtra("marka", mMarka.getText().toString());
                    intent.putExtra("model", mModel.getText().toString());
                    intent.putExtra("registracija", mRegistracija.getText().toString());
                    intent.putExtra("rezervisan", mRezervisan.getText().toString());
                    intent.putExtra("cena", mCena.getText().toString());
                    intent.putExtra("putnici", mBrPutnika.getText().toString());
                    intent.putExtra("lokacija", mLokacija.getText().toString());

                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(Vozilo vozilo, String key) {
            mTip.setText(vozilo.getTip());
            mMarka.setText(vozilo.getMarka());
            mModel.setText(vozilo.getModel());
            mRegistracija.setText(vozilo.getRegistracija());
            mCena.setText(vozilo.getCenaPoMinutu() + "");
            mBrPutnika.setText(vozilo.getBrojPutnika() + "");
            mLokacija.setText(getAddress(new LatLng(vozilo.getLat(), vozilo.getLng())));

            if (vozilo.isRezervisan()) {
                mRezervisan.setText("zauzet");
                mRezervisan.setTextColor(Color.RED);
            } else {
                mRezervisan.setText("slobodan");
                mRezervisan.setTextColor(Color.GREEN);
            }

            this.key = key;
        }
    }

    class VoziloAdapter extends RecyclerView.Adapter<VehicleItemView>{
        private List<Vozilo> mVozila;
        private List<String> mKeys;

        public VoziloAdapter(List<Vozilo> mVozila, List<String> mKeys) {
            this.mVozila = mVozila;
            this.mKeys = mKeys;
        }
        @NonNull
        @Override
        public VehicleItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VehicleItemView(parent);
        }
        @Override
        public void onBindViewHolder(@NonNull VehicleItemView holder, int position) {
            holder.bind(mVozila.get(position), mKeys.get(position));
        }
        @Override
        public int getItemCount() {
            return mVozila.size();
        }
    }

    private String getAddress(LatLng myCoordinates) {
        String adresa[] = null;

        Geocoder geocoder = new Geocoder(new VoziloDetailsActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            adresa= addresses.get(0).getAddressLine(0).split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adresa[0];
    }
}
