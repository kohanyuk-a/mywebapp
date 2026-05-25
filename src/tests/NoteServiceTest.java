package com.example.mywebapp.service;

import com.example.mywebapp.entity.Note;
import com.example.mywebapp.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository repository;

    @InjectMocks
    private NoteService service;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = buildNote(1L, "Title 1", "Content 1");
        note2 = buildNote(2L, "Title 2", "Content 2");
    }

    @Test
    @DisplayName("findAll() повертає всі нотатки з репозиторію")
    void findAll_returnsAllNotes() {
        when(repository.findAll()).thenReturn(List.of(note1, note2));

        List<Note> result = service.findAll();

        assertThat(result).hasSize(2)
                .extracting(Note::getTitle)
                .containsExactly("Title 1", "Title 2");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll() повертає порожній список, якщо нотаток немає")
    void findAll_returnsEmptyList_whenNoNotes() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }


    @Test
    @DisplayName("findById() повертає нотатку за існуючим id")
    void findById_returnsNote_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(note1));

        Note result = service.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Title 1");
    }

    @Test
    @DisplayName("findById() кидає RuntimeException для відсутнього id")
    void findById_throwsException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Note not found");
    }


    @Test
    @DisplayName("create() зберігає нову нотатку і повертає збережену сутність")
    void create_savesAndReturnsNote() {
        Note saved = buildNote(3L, "New", "Body");
        when(repository.save(any(Note.class))).thenReturn(saved);

        Note result = service.create("New", "Body");

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("New");
        assertThat(result.getContent()).isEqualTo("Body");

        verify(repository).save(argThat(n ->
                "New".equals(n.getTitle()) && "Body".equals(n.getContent())
        ));
    }

    @Test
    @DisplayName("create() викликає repository.save() рівно один раз")
    void create_callsRepositorySaveOnce() {
        when(repository.save(any(Note.class))).thenReturn(note1);
        service.create("T", "C");
        verify(repository, times(1)).save(any(Note.class));
    }


    private static Note buildNote(Long id, String title, String content) {
        Note n = new Note();
        n.setTitle(title);
        n.setContent(content);
        try {
            var field = Note.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(n, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return n;
    }
}