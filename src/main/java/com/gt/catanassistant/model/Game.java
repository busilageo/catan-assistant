package com.gt.catanassistant.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Game {
    private UUID id;
    private String link;
    private List<Player> players = new ArrayList<>();
    private List<List<Player>> rounds = new ArrayList<>();
    private Map<String, String> colors = new HashMap<>();
    private String status = "wait";
    private boolean ready = false;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Game() {
        this.id = UUID.randomUUID();
        date = LocalDate.now();
        startTime = LocalTime.now();
    }

    public Game(UUID id, Game game) {
        this.id = id;
        this.link = game.getLink();
        this.players = game.getPlayers();
        this.rounds = game.getRounds();
        this.colors = game.getColors();
        this.status = game.getStatus();
        this.ready = game.isReady();
        this.date = game.getDate();
        this.startTime = game.getStartTime();
        this.endTime = game.getEndTime();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getDuration()
    {
        LocalTime time2 = (status.equals("live") || status.equals("wait")) ? LocalTime.now() : this.endTime;
        Duration duration = Duration.between(time2, this.startTime);
        String durationString = "";
        if (duration.toHours() > 0)
        {
            if (duration.toHours() < 10)
                durationString += 0;
            durationString = String.valueOf(Math.abs(duration.toHours())) + ':';
        }
        if (Math.abs(duration.toMinutes()) % 60 < 10)
            durationString += 0;
        durationString += String.valueOf(Math.abs(duration.toMinutes()) % 60) + ':';
        if (Math.abs(duration.toSeconds()) % 60 < 10)
            durationString += 0;
        durationString += String.valueOf(Math.abs(duration.toSeconds()) % 60);

        return durationString;
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
        endTime = LocalTime.now();
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
