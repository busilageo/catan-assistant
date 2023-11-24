package com.gt.catanassistant.api;

import com.gt.catanassistant.model.Game;
import com.gt.catanassistant.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/games")
@RestController
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public void addGame(@RequestBody Game game)
    {
        gameService.addGame(game);
    }

    @PostMapping(path = "{id}/request")
    public void handleRequest(@PathVariable("id") UUID id, @RequestBody String request)
    {
        gameService.handleRequest(id, request);
    }

    @GetMapping
    public List<Game> getAllGames()
    {
        return gameService.getAllGames();
    }

    @GetMapping(path = "{id}")
    public Game getGameById(@PathVariable("id") UUID id)
    {
        return gameService.getGameById(id);
    }

    @DeleteMapping(path = "{id}")
    public void deleteGameById(@PathVariable("id") UUID id)
    {
        gameService.deleteGameById(id);
    }
}
