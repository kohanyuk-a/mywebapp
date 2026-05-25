package com.example.mywebapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class NoteTest {

    @Test
    @DisplayName("setTitle / getTitle зберігають значення")
    void setAndGetTitle() {
        Note note = new Note();
        note.setTitle("Hello");
        assertThat(note.getTitle()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("setContent / getContent зберігають значення")
    void setAndGetContent() {
        Note note = new Note();
        note.setContent("Some body");
        assertThat(note.getContent()).isEqualTo("Some body");
    }

    @Test
    @DisplayName("Новий об'єкт має null id та null createdAt до збереження")
    void newNote_hasNullIdAndTimestamp() {
        Note note = new Note();
        assertThat(note.getId()).isNull();
        assertThat(note.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("prePersist() встановлює createdAt близько до поточного часу")
    void prePersist_setsCreatedAt() {
        Note note = new Note();
        Instant before = Instant.now();
        note.prePersist();
        Instant after = Instant.now();

        assertThat(note.getCreatedAt())
                .isNotNull()
                .isAfterOrEqualTo(before)
                .isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("prePersist() можна викликати повторно — оновлює timestamp")
    void prePersist_canBeCalledTwice() throws InterruptedException {
        Note note = new Note();
        note.prePersist();
        Instant first = note.getCreatedAt();

        Thread.sleep(5);
        note.prePersist();
        Instant second = note.getCreatedAt();

        assertThat(second).isAfterOrEqualTo(first);
    }

    @Test
    @DisplayName("setTitle(null) приймається без NPE")
    void setTitle_acceptsNull() {
        Note note = new Note();
        assertThatCode(() -> note.setTitle(null)).doesNotThrowAnyException();
        assertThat(note.getTitle()).isNull();
    }

    @Test
    @DisplayName("setContent(null) приймається без NPE")
    void setContent_acceptsNull() {
        Note note = new Note();
        assertThatCode(() -> note.setContent(null)).doesNotThrowAnyException();
        assertThat(note.getContent()).isNull();
    }
}