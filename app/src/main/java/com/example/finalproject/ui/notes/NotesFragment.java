package com.example.finalproject.ui.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.databaseFunctions;
import com.example.finalproject.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
    private static final String PREFS_NAME = "NotePrefs";

    private LinearLayout notesContainer;
    private final List<Note> noteList = new ArrayList<>();
    private databaseFunctions dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        dbHelper = new databaseFunctions(requireContext());
        notesContainer = root.findViewById(R.id.notesContainer);
        Button saveButton = root.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        String currentUser = getCurrentUser();
        loadNotesFromDatabase(currentUser);
        displayNotes();

        return root;
    }

    private String getCurrentUser() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }


    private void setCurrentUser(String username) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void displayNotes() {
        notesContainer.removeAllViews(); // Clear previous notes before displaying
        for (Note note : noteList) {
            createNoteView(note);
        }
    }

    private void loadNotesFromDatabase(String username) {
        if (username != null) {
            List<Note> notes = dbHelper.fetchNotesByUsername(username);
            if (notes != null) {
                noteList.addAll(notes);
            }
        } else {
            Toast.makeText(requireContext(), "Username is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNote() {
        EditText titleEditText = requireView().findViewById(R.id.titleEditText);
        EditText contentEditText = requireView().findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        String currentUser = getCurrentUser();

        if (!title.isEmpty() && !content.isEmpty() && currentUser != null) {
            if (dbHelper.insertNote(currentUser, title, content)) {
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setUsername(currentUser);

                noteList.add(note);
                createNoteView(note);
                clearInputFields();
                Toast.makeText(requireContext(), "Saved note", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to save note. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Title is empty", Toast.LENGTH_SHORT).show();
            }
            if (content.isEmpty()) {
                Toast.makeText(requireContext(), "Content is empty", Toast.LENGTH_SHORT).show();
            }
            if (currentUser == null) {
                Toast.makeText(requireContext(), "Current user is null", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(requireContext(), "Please enter a title and content.", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearInputFields() {
        EditText titleEditText = requireView().findViewById(R.id.titleEditText);
        EditText contentEditText = requireView().findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView = LayoutInflater.from(requireContext()).inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete this note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteAndRefresh(note);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note) {
        noteList.remove(note);
        dbHelper.deleteNoteByUsername(note.getUsername(), note.getTitle());
        refreshNoteViews();
    }

    private void refreshNoteViews() {
        notesContainer.removeAllViews();
        displayNotes();
    }
}
