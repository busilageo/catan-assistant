package com.gt.catanassistant.dao;

import com.gt.catanassistant.model.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeGameDao")
public class FakeGameDataAccessService implements GameDao
{
    List<Game> DB = new ArrayList<>();

    @Override
    public void addGame(UUID id, Game game) {
        DB.add(new Game(id, game));
    }

    @Override
    public List<Game> getAllGames() {
        return DB;
    }

    @Override
    public Game getGameById(UUID id) {
        Optional<Game> gameMaybe = DB.stream()
                .filter(game -> game.getId().equals(id))
                .findFirst();
        return gameMaybe.orElse(null);
    }

    @Override
    public void deleteGameById(UUID id) {
        Game gameMaybe = getGameById(id);
        if (gameMaybe != null)
            DB.remove(gameMaybe);
    }
}
