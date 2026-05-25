package com.example.mywebapp;

import com.example.mywebapp.entity.Note;
import com.example.mywebapp.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyWebAppIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void cleanDb() {
        noteRepository.deleteAll();
    }


    @Test
    @DisplayName("[INT] GET / → HTML з переліком ендпоінтів")
    void home_returnsHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("MyWebApp")))
                .andExpect(content().string(containsString("/notes")));
    }


    @Test
    @DisplayName("[INT] GET /health/alive → 200 OK")
    void alive_returns200() throws Exception {
        mockMvc.perform(get("/health/alive"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("[INT] GET /health/ready → 200 OK (H2 доступна)")
    void ready_returns200() throws Exception {
        mockMvc.perform(get("/health/ready"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }


    @Test
    @DisplayName("[INT] POST /notes → 200, нотатка збережена в БД")
    void createNote_persistsInDb() throws Exception {
        mockMvc.perform(post("/notes")
                        .param("title", "Integration Title")
                        .param("content", "Integration Content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("Integration Title")))
                .andExpect(jsonPath("$.content", is("Integration Content")))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        // Перевіряємо безпосередньо в БД
        var notes = noteRepository.findAll();
        org.assertj.core.api.Assertions.assertThat(notes)
                .hasSize(1)
                .first()
                .satisfies(n -> {
                    org.assertj.core.api.Assertions.assertThat(n.getTitle()).isEqualTo("Integration Title");
                    org.assertj.core.api.Assertions.assertThat(n.getContent()).isEqualTo("Integration Content");
                    org.assertj.core.api.Assertions.assertThat(n.getCreatedAt()).isNotNull();
                });
    }


    @Test
    @DisplayName("[INT] GET /notes [JSON] → порожній список, якщо БД пуста")
    void getNotesJson_emptyList_whenDbEmpty() throws Exception {
        mockMvc.perform(get("/notes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("[INT] GET /notes [JSON] → список зі збережених нотаток")
    void getNotesJson_returnsSavedNotes() throws Exception {
        saveNote("Alpha", "aaa");
        saveNote("Beta",  "bbb");

        mockMvc.perform(get("/notes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Alpha", "Beta")));
    }


    @Test
    @DisplayName("[INT] GET /notes [HTML] → HTML-таблиця зі збережених нотаток")
    void getNotesHtml_returnsTable() throws Exception {
        saveNote("Gamma", "ggg");

        mockMvc.perform(get("/notes").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Gamma")))
                .andExpect(content().string(containsString("<table")));
    }


    @Test
    @DisplayName("[INT] GET /notes/{id} [JSON] → нотатка за id")
    void getNoteJson_returnsNote_whenExists() throws Exception {
        Note saved = saveNote("Delta", "ddd");

        mockMvc.perform(get("/notes/" + saved.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",      is(saved.getId().intValue())))
                .andExpect(jsonPath("$.title",   is("Delta")))
                .andExpect(jsonPath("$.content", is("ddd")));
    }

    @Test
    @DisplayName("[INT] GET /notes/99999 [JSON] → 500 якщо не знайдено")
    void getNoteJson_returns500_whenNotFound() throws Exception {
        mockMvc.perform(get("/notes/99999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("[INT] GET /notes/{id} [HTML] → HTML-деталі нотатки")
    void getNoteHtml_returnsHtml_whenExists() throws Exception {
        Note saved = saveNote("Epsilon", "eee");

        mockMvc.perform(get("/notes/" + saved.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Epsilon")))
                .andExpect(content().string(containsString("eee")))
                .andExpect(content().string(containsString("Created at:")));
    }



    private Note saveNote(String title, String content) {
        Note n = new Note();
        n.setTitle(title);
        n.setContent(content);
        return noteRepository.save(n);
    }
}