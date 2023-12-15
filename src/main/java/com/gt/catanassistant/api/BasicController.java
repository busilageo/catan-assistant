package com.gt.catanassistant.api;

import com.gt.catanassistant.model.Game;
import com.gt.catanassistant.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO Add game comments (anonymously)     BRANCH GT
//TODO Add JPA DB                          main branch, in parallel
//TODO Add local JPA                       BRANCH GT

@Controller
public class BasicController {
    private final GameService gameService;

    @Autowired
    BasicController(GameService gameService)
    {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String defaultPage()
    {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
        model.addAttribute("game", new Game());
        model.addAttribute("error", error);
        return "index";
    }


    @GetMapping("/game/{id}")
    public String game(@PathVariable("id")UUID id,
                       @RequestParam(value = "round", required = false) Integer round,
                       Model model) {
        Game game = gameService.getGameById(id);

        model.addAttribute("players", (round == null) ? game.getPlayers() :
                                                                    game.getRound(round));
        model.addAttribute("round", (round == null) ? game.getRoundsNr() - 1 : round);
        model.addAttribute("maxRounds", game.getRoundsNr() - 1);
        model.addAttribute("status", game.getStatus());
        model.addAttribute("id", id);
        model.addAttribute("isReady", game.isReady());
        return "game";
    }

    @GetMapping("/archive")
    public String archive(Model model)
    {
        model.addAttribute("games", gameService.getAllGames());
        return "archive";
    }

    @PostMapping("/createGame")
    public String createGame(@ModelAttribute Game game) throws IOException {
        // Check for validity
        String regex = "https://colonist.io/#[a-zA-Z0-9]{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(game.getLink());

        if (!matcher.matches())
            return "redirect:/index?error=true";

        gameService.addGame(new Game(game.getId(), game));

        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/python/scraper.py", game.getLink(), game.getId().toString());
        processBuilder.redirectErrorStream(true);
        Process pythonProcess = processBuilder.start();

        return "redirect:/game/" + game.getId();
    }
}
