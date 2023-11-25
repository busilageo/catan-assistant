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
    public String game(@PathVariable("id")UUID id, Model model) {
        model.addAttribute("players", gameService.getGameById(id).getPlayers());
        model.addAttribute("round", gameService.getGameById(id).getRoundsNr() - 1);
        model.addAttribute("prevRound", gameService.getGameById(id).getRoundsNr() - 2);
        model.addAttribute("nextRound", gameService.getGameById(id).getRoundsNr());
        model.addAttribute("maxRounds", gameService.getGameById(id).getRoundsNr() - 1);
        model.addAttribute("status", gameService.getGameById(id).getStatus());
        model.addAttribute("id", id);
        return "game";
    }

    @GetMapping("/game/{id}?round={nr}")
    public String game(@PathVariable("id")UUID id, @PathVariable("nr") int nr, Model model)
    {
        model.addAttribute("players", gameService.getGameById(id).getRound(nr));
        model.addAttribute("round", nr);
        model.addAttribute("prevRound", nr - 1);
        model.addAttribute("nextRound", nr + 1);
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
