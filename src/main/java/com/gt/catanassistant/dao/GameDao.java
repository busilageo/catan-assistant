package com.gt.catanassistant.dao;

import com.gt.catanassistant.model.Game;

import java.util.List;
import java.util.UUID;

public interface GameDao
{
    public void addGame(UUID id, Game game);
    default void addGame(Game game)
    {
        UUID id = UUID.randomUUID();
        addGame(id, game);
    }
    List<Game> getAllGames();
    Game getGameById(UUID id);
    void deleteGameById(UUID id);
}
