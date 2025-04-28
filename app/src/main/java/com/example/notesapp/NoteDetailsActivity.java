package com.example.notesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleedittext, contentedittext;
    ImageButton savenotebtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleedittext = findViewById(R.id.notes_title_text);
        contentedittext = findViewById(R.id.notes_content_text);
        savenotebtn = findViewById(R.id.savenote_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn= findViewById(R.id.deletenote_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty())
            isEditMode = true;

        titleedittext.setText(title);
        contentedittext.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }


        savenotebtn.setOnClickListener((v) -> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v -> deleteNoteFromFirebase()));

    }

    void saveNote() {
        String title = titleedittext.getText().toString();
        String content = contentedittext.getText().toString();

        if (title.isEmpty()) {
            titleedittext.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }


    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                }
            }
        });

    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Note Deleted successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            }
        });
    }

}