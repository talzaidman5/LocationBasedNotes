package com.android.locationbasednotes.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.activities.EditNoteActivity;
import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.List;

public class Adapter_Note extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_NORMAL = 0;
    private Context context;
    private List<Note> notes;
    private Gson gson = new Gson();
    private MySheredP msp;
    private StorageReference mStorageRef;
    private User currentUser;
    private Note currentNote;

    public Adapter_Note( List<Note> notes,Context context) {
        this.context = context;
        this.notes = notes;

    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
            msp = new MySheredP(context);
            mStorageRef = FirebaseStorage.getInstance().getReference();

            return new ViewHolder_Normal(view);

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
        ViewHolder_Normal myViewHolderNormal = new ViewHolder_Normal(view);
        return myViewHolderNormal;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        currentNote = (Note)this.getItem(pos);

       ViewHolder_Normal mHolder = (ViewHolder_Normal) holder;
        if(currentNote.isImage())
            downloadImage(mHolder);
        else
            mHolder.note_IMG_image.setBackgroundColor(context.getColor(R.color.noteWithoutImage));
        mHolder.note_LBL_title.setText(currentNote.getTitle());
        mHolder.note_LBL_body.setText(currentNote.getBody());
        DateFormat dateFormat =  android.text.format.DateFormat.getDateFormat(context);
        mHolder.note_LBL_date.setText(dateFormat.format(currentNote.getDate()));

        mHolder.note_BTN_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentNote = (Note) getItem(mHolder.getLayoutPosition());
                editNote(v);
            }
        });

    }
    private void editNote(View v) {
        saveNoteInMSF();
        v.getContext().startActivity(new Intent(v.getContext(), EditNoteActivity.class));

    }
    private void downloadImage(ViewHolder_Normal mHolder) {
        getUserFromMSP();
        mStorageRef.child(currentUser.getUid()).child(currentNote.getID()).getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide
                                .with(context)
                                .load(uri)
                                .into(mHolder.note_IMG_image);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

            }
        });

    }


    private void saveNoteInMSF() {
        String note = gson.toJson(currentNote);
        msp.putString(context.getString(R.string.noteKey), note);

    }

    private Note getItem(int position) {
        return notes.get(position);
    }


    static class ViewHolder_Normal extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView note_LBL_title;
        public TextView note_LBL_body;
        public TextView note_LBL_date;
        public Button note_BTN_edit;
        public ImageView note_IMG_image;

        public ViewHolder_Normal(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            note_LBL_title = itemView.findViewById(R.id.note_LBL_title);
            note_LBL_date = itemView.findViewById(R.id.note_LBL_date);
            note_LBL_body = itemView.findViewById(R.id.note_LBL_body);
            note_BTN_edit = itemView.findViewById(R.id.note_BTN_edit);
            note_IMG_image = itemView.findViewById(R.id.note_IMG_image);
        }

        @Override
        public void onClick(View view) {
        }
    }
    private User getUserFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(context.getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
        return currentUser;
    }
}
