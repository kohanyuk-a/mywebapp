package com.example.mywebapp.repository;

import com.example.mywebapp.entity.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("save() зберігає нотатку і генерує id")
    void save_persistsNote() {
        Note note = new Note();
        note.setTitle("Repo Test");
        note.setContent("Some content");

        Note saved = repository.save(note);

        assertThat(saved.getId()).isNotNull().isPositive();
        assertThat(saved.getTitle()).isEqualTo("Repo Test");
    }


    @Test
    @DisplayName("findById() повертає Optional з нотаткою за існуючим id")
    void findById_returnsNote_whenExists() {
        Note persisted = persistNote("Find Me", "body");

        Optional<Note> result = repository.findById(persisted.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Find Me");
    }

    @Test
    @DisplayName("findById() повертає порожній Optional для відсутнього id")
    void findById_returnsEmpty_whenNotExists() {
        Optional<Note> result = repository.findById(Long.MAX_VALUE);
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("findAll() повертає всі збережені нотатки")
    void findAll_returnsAllNotes() {
        persistNote("Note A", "a");
        persistNote("Note B", "b");
        persistNote("Note C", "c");

        List<Note> all = repository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(3)
                .extracting(Note::getTitle)
                .contains("Note A", "Note B", "Note C");
    }


    @Test
    @DisplayName("deleteById() видаляє нотатку з БД")
    void deleteById_removesNote() {
        Note persisted = persistNote("To Delete", "bye");
        Long id = persisted.getId();

        repository.deleteById(id);
        entityManager.flush();

        assertThat(repository.findById(id)).isEmpty();
    }


    @Test
    @DisplayName("createdAt встановлюється автоматично при збереженні через JPA")
    void save_setsCreatedAt_viaPrePersist() {
        Note note = new Note();
        note.setTitle("Timestamp");
        note.setContent("check");

        Note saved = repository.save(note);

        assertThat(saved.getCreatedAt()).isNotNull();
    }


    private Note persistNote(String title, String content) {
        Note n = new Note();
        n.setTitle(title);
        n.setContent(content);
        return entityManager.persistAndFlush(n);
    }
}