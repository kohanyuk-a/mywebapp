package com.example.mywebapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
                <html>
                <body>
                <h1>MyWebApp</h1>
                <ul>
                    <li>GET /notes</li>
                    <li>POST /notes</li>
                    <li>GET /notes/{id}</li>
                    <li>GET /health/alive</li>
                    <li>GET /health/ready</li>
                </ul>
                </body>
                </html>
                """;
    }
}
