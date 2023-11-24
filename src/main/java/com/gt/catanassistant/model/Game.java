package com.gt.catanassistant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Game {
    public UUID id;
    public String link;
    public List<Player> players;

    public Game() {
        this.id = UUID.randomUUID();
        this.players = new ArrayList<>();
    }

    public Game(@JsonProperty("id") UUID id,
                @JsonProperty("link") String link,
                @JsonProperty("players") List<Player> players) {
        this.id = id;
        this.link = link;
        this.players = players;
    }

    public Game(UUID id, Game game) {
        this.id = id;
        this.link = game.getLink();
        this.players = game.getPlayers();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void addPlayer(Player player)
    {
        players.add(player);
    }

    public Player getPlayerById(UUID id)
    {
        Optional<Player> playerMaybe = players.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();
        return playerMaybe.orElse(null);
    }
    public Player getPlayerByName(String name)
    {
        Optional<Player> playerMaybe = players.stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
        return playerMaybe.orElse(null);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", players=" + players +
                '}';
    }
}
