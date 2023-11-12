package com.gt.catanassistant.api;

import com.gt.catanassistant.model.Player;
import com.gt.catanassistant.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/players")
@RestController
public class PlayerController
{
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player)
    {
        playerService.addPlayer(player);
    }

    @GetMapping
    public List<Player> getAllPlayers()
    {
        return playerService.getAllPlayers();
    }

    @GetMapping(path = "{id}")
    public Player getPlayerById(@PathVariable("id") UUID id)
    {
        return playerService.getPlayerById(id).orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deletePlayerById(@PathVariable("id") UUID id)
    {
        playerService.deletePlayerById(id);
    }

    @PutMapping(path = "{id}")
    public void updatePlayerById(@PathVariable("id") UUID id, @RequestBody Player player)
    {
        playerService.updatePlayerById(id, player);
    }

    //TODO
    @PostMapping(path = "action")
    public void resolveAction(@RequestBody String action)
    {
        //playerService.resolveAction(action);
    }
}
