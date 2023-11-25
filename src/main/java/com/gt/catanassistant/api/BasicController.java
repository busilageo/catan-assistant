package com.gt.catanassistant.api;

import com.gt.catanassistant.model.Game;
import com.gt.catanassistant.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Controller
public class BasicController {
    private final GameService gameService;

    @Autowired
    BasicController(GameService gameService)
    {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("game", new Game());
        return "index";
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable("id")UUID id,
                       @RequestParam(value = "round", required = false) Integer round,
                       Model model) {
        model.addAttribute("players", (round == null) ? gameService.getGameById(id).getPlayers() :
                                                                    gameService.getGameById(id).getRound(round));
        model.addAttribute("round", (round == null) ? gameService.getGameById(id).getRoundsNr() - 1 : round);
        model.addAttribute("maxRounds", gameService.getGameById(id).getRoundsNr() - 1);
        model.addAttribute("status", gameService.getGameById(id).getStatus());
        model.addAttribute("id", id);
        return "game";
    }

    @PostMapping("/createGame")
    public String createGame(@ModelAttribute Game game) throws IOException {
        gameService.addGame(new Game(game.getId(), game));

        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/python/scraper.py", game.getLink(), game.getId().toString());
        Process pythonProcess = processBuilder.start();

        return "redirect:/game/" + game.getId();
    }
}
