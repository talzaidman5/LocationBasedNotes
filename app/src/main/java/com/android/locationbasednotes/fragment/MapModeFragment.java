package com.android.locationbasednotes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.activities.EditNoteActivity;
import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


public class MapModeFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private User currentUser;
    private MySheredP msp;
    private GoogleMap mMap;
    protected Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.fragment_map_mode, container, false);
        msp = new MySheredP(getContext());
        getFromMSP();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_mode_FRG_map);
        mapFragment.getMapAsync(this);
        return view;
    }

    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

       @Override
    public void onMapReady(GoogleMap googleMap) {
        if(currentUser.getNoteList()!=null) {
            for (Note note : currentUser.getNoteList()) {
                mMap = googleMap;
                LatLng latLng = new LatLng(note.getLocation().get(0), note.getLocation().get(1));
                mMap.addMarker(new MarkerOptions().position(latLng).title(note.getTitle()));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                putOnMSP(note);
                                startActivity(new Intent(getContext(), EditNoteActivity.class));
                                return false;
                            }
                        });
            }
            LatLng latLng= new LatLng(currentUser.getNoteList().get(0).getLocation().get(0), currentUser.getNoteList().get(0).getLocation().get(1));
            mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(latLng,10));

        }
    }
    protected void putOnMSP(Note currentNote) {
        String note = gson.toJson(currentNote);
        msp.putString(getString(R.string.noteKey), note);
    }




}