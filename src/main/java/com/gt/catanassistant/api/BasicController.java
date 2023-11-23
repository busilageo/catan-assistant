package com.gt.catanassistant.api;

import com.gt.catanassistant.model.Game;
import com.gt.catanassistant.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class BasicController {
    private final PlayerService playerService; // Game service? todo
    @Autowired
    public BasicController(PlayerService playerService) {
        this.playerService = playerService;
    }
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("game", new Game());
        return "index";
    }

    @GetMapping("/game")
    public String game(Model model) {
        model.addAttribute("players", playerService.getAllPlayers());
        return "game";
    }

    @PostMapping("/game")
    public String startGame(@ModelAttribute Game game) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/python/scraper.py", game.getLink());
        Process pythonProcess = processBuilder.start();

        return "redirect:/game";
    }
}
