package com.example.mywebapp.service;

import com.example.mywebapp.entity.Note;
import com.example.mywebapp.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public List<Note> findAll() {
        return repository.findAll();
    }

    public Note findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public Note create(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        return repository.save(note);
    }
}
