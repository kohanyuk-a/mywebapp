package com.example.mywebapp.controller;

import com.example.mywebapp.entity.Note;
import com.example.mywebapp.service.NoteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping(value = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Note> getNotesJson() {
        return service.findAll();
    }

    @GetMapping(value = "/notes", produces = MediaType.TEXT_HTML_VALUE)
    public String getNotesHtml() {

        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h1>Notes</h1>");
        html.append("<table border='1'>");
        html.append("<tr><th>ID</th><th>Title</th></tr>");

        for (Note note : service.findAll()) {
            html.append("<tr>");
            html.append("<td>").append(note.getId()).append("</td>");
            html.append("<td>").append(note.getTitle()).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }

    @GetMapping(value = "/notes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Note getNoteJson(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/notes/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getNoteHtml(@PathVariable Long id) {

        Note note = service.findById(id);

        return "<html><body>" +
                "<h1>" + note.getTitle() + "</h1>" +
                "<p>" + note.getContent() + "</p>" +
                "<p>Created at: " + note.getCreatedAt() + "</p>" +
                "</body></html>";
    }

    @PostMapping("/notes")
    public Note create(@RequestParam String title,
                       @RequestParam String content) {

        return service.create(title, content);
    }
}
