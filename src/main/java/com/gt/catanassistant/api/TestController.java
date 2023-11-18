package com.gt.catanassistant.api;

import com.gt.catanassistant.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    private final PlayerService playerService;
    @Autowired
    public TestController(PlayerService playerService) {
        this.playerService = playerService;
    }
    @GetMapping
    public String getAllPlayers(Model model)
    {
        model.addAttribute("players", playerService.getAllPlayers());
        return "game";
    }
}
