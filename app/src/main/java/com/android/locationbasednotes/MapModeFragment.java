package com.android.locationbasednotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                putOnMSP(note);
                                ShowDialogNote(note);
                                return false;
                            }
                        });
            }
        }
    }
    protected void putOnMSP(Note currentNote) {
        String note = gson.toJson(currentNote);
        msp.putString(getString(R.string.noteKey), note);
    }


    private void ShowDialogNote(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(note.getTitle());
        builder.setMessage(note.getBody());
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Edit Note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), EditNoteActivity.class));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}