package com.example.mywebapp.controller;

import com.example.mywebapp.entity.Note;
import com.example.mywebapp.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService service;

    @InjectMocks
    private NoteController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        note1 = buildNote(1L, "First",  "Content A");
        note2 = buildNote(2L, "Second", "Content B");
    }


    @Test
    @DisplayName("GET /notes [JSON] → 200 зі списком нотаток")
    void getNotesJson_returns200WithList() throws Exception {
        when(service.findAll()).thenReturn(List.of(note1, note2));

        mockMvc.perform(get("/notes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("First")))
                .andExpect(jsonPath("$[1].title", is("Second")));
    }

    @Test
    @DisplayName("GET /notes [JSON] → порожній масив, якщо нотаток немає")
    void getNotesJson_returnsEmptyArray_whenNoNotes() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/notes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @DisplayName("GET /notes [HTML] → 200 з таблицею нотаток")
    void getNotesHtml_returns200WithTable() throws Exception {
        when(service.findAll()).thenReturn(List.of(note1, note2));

        mockMvc.perform(get("/notes").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("<table")))
                .andExpect(content().string(containsString("First")))
                .andExpect(content().string(containsString("Second")));
    }

    @Test
    @DisplayName("GET /notes [HTML] → порожня таблиця, якщо нотаток немає")
    void getNotesHtml_returnsEmptyTable_whenNoNotes() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/notes").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<table")))
                .andExpect(content().string(not(containsString("<td>"))));
    }


    @Test
    @DisplayName("GET /notes/1 [JSON] → 200 з конкретною нотаткою")
    void getNoteJson_returns200_whenFound() throws Exception {
        when(service.findById(1L)).thenReturn(note1);

        mockMvc.perform(get("/notes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",    is(1)))
                .andExpect(jsonPath("$.title", is("First")));
    }

    @Test
    @DisplayName("GET /notes/99 [JSON] → 500, якщо нотатка не знайдена")
    void getNoteJson_returns500_whenNotFound() throws Exception {
        when(service.findById(99L)).thenThrow(new RuntimeException("Note not found"));

        mockMvc.perform(get("/notes/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("GET /notes/1 [HTML] → 200 з деталями нотатки")
    void getNoteHtml_returns200_whenFound() throws Exception {
        when(service.findById(1L)).thenReturn(note1);

        mockMvc.perform(get("/notes/1").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("First")))
                .andExpect(content().string(containsString("Content A")));
    }

    @Test
    @DisplayName("GET /notes/99 [HTML] → 500, якщо нотатка не знайдена")
    void getNoteHtml_returns500_whenNotFound() throws Exception {
        when(service.findById(99L)).thenThrow(new RuntimeException("Note not found"));

        mockMvc.perform(get("/notes/99").accept(MediaType.TEXT_HTML))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("POST /notes → 200 зі створеною нотаткою")
    void createNote_returns200WithCreatedNote() throws Exception {
        when(service.create("New Title", "New Content")).thenReturn(note1);

        mockMvc.perform(post("/notes")
                        .param("title", "New Title")
                        .param("content", "New Content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",    is(1)))
                .andExpect(jsonPath("$.title", is("First")));

        verify(service).create("New Title", "New Content");
    }


    private static Note buildNote(Long id, String title, String content) {
        Note n = new Note();
        n.setTitle(title);
        n.setContent(content);
        try {
            var idField = Note.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(n, id);

            var tsField = Note.class.getDeclaredField("createdAt");
            tsField.setAccessible(true);
            tsField.set(n, Instant.parse("2024-01-01T00:00:00Z"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return n;
    }
}