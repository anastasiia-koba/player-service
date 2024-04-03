package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.service.PlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/player")
public class PlayerController {

    // todo add ExceptionHandlers
    private PlayerService playerService;
    @Value("${csv.out.file.path}")
    private String outPathName;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public String getAllPlayers(HttpServletResponse response) {
        // prepare response encoding and Headers
        response.setContentType("text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // specify the real file name users will get when download
        response.setHeader("Content-Disposition", "attachment; filename=" + outPathName);

        return playerService.getCsvPlayersInformation();
    }
}
