package com.android.locationbasednotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;


public class ListModeFragment extends Fragment {
    private View view;
    private User currentUser;
    private MySheredP msp;
    private RecyclerView activity_main_LST_notes;
    private TextView activity_main_TXT_noNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.fragment_list_mode, container, false);
        msp = new MySheredP(getContext());
        findViews();
        getFromMSP();

        showAllNotes();

        return view;
    }
    public void showAllNotes() {
        Collections.sort(currentUser.getNoteList(), new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return note1.getDate().compareTo(note2.getDate());
            }
        });

        if (currentUser.getNoteList() != null) {

            Adapter_Note adapter_note = new Adapter_Note(currentUser.getNoteList(),getContext());
            activity_main_LST_notes.setLayoutManager(new LinearLayoutManager(getContext()));
            activity_main_LST_notes.setItemAnimator(new DefaultItemAnimator());
            activity_main_LST_notes.setAdapter(adapter_note);
        }
    }
    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

    private void findViews(){
        activity_main_LST_notes = view.findViewById(R.id.activity_main_LST_notes);

    }
}