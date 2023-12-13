package com.gt.catanassistant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.PublicKey;
import java.util.*;

public class Game {
    public UUID id;
    public String link;
    public List<Player> players;
    public List<List<Player>> rounds;
    public Map<String, String> colors = new HashMap<>();
    public String status = "wait";
    public boolean ready = false;

    public Game() {
        this.id = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.rounds = new ArrayList<>();
    }

    public Game(@JsonProperty("id") UUID id,
                @JsonProperty("link") String link,
                @JsonProperty("players") List<Player> players,
                @JsonProperty("rounds") List<List<Player>> rounds,
                @JsonProperty("status") String status) {
        this.id = id;
        this.link = link;
        this.players = players;
        this.rounds = rounds;
        this.status = status;
    }

    public Game(@JsonProperty("id") UUID id,
                @JsonProperty("link") String link,
                @JsonProperty("players") List<Player> players,
                @JsonProperty("rounds") List<List<Player>> rounds,
                @JsonProperty("colors") Map<String, String> colors,
                @JsonProperty("status") String status) {
        this.id = id;
        this.link = link;
        this.players = players;
        this.rounds = rounds;
        this.colors = colors;
        this.status = status;
    }

    public Game(UUID id, Game game) {
        this.id = id;
        this.link = game.getLink();
        this.players = game.getPlayers();
        this.rounds = game.getRounds();
        this.colors = game.getColors();
        this.status = game.getStatus();
        this.ready = game.isReady();
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Map<String, String> getColors() {
        return colors;
    }

    public void setColors(Map<String, String> colors) {
        this.colors = colors;
    }

    public List<List<Player>> getRounds() {
        return rounds;
    }

    public void setRounds(List<List<Player>> rounds) {
        this.rounds = rounds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void pushRound()
    {
        List<Player> roundPlayers = new ArrayList<>();
        for (Player player : players)
            roundPlayers.add(new Player(player.getId(), player));
        rounds.add(roundPlayers);
    }

    public List<Player> getRound(int index)
    {
        return rounds.get(index);
    }

    public int getRoundsNr()
    {
        return rounds.size();
    }

    public void finish()
    {
        status = "finished";
    }

    public void addColor(String name, String color)
    {
        colors.put(name, color);
    }

    public String getColorOfName(String name)
    {
        return colors.get(name);
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
