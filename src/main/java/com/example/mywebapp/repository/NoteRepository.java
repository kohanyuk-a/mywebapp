package com.example.mywebapp.repository;

import com.example.mywebapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
