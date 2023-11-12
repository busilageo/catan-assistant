package com.gt.catanassistant.dao;

import com.gt.catanassistant.model.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePlayerDataAccessService implements PlayerDao
{
    List<Player> DB = new ArrayList<>();

    @Override
    public boolean insertPlayer(UUID id, Player player) {
        DB.add(new Player(id, player));
        return true;
    }

    @Override
    public List<Player> selectAllPlayers() {
        return DB;
    }

    @Override
    public Optional<Player> selectPlayerById(UUID id) {
        return DB.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deletePlayerById(UUID id) {
        Optional<Player> playerMaybe = selectPlayerById(id);
        if (playerMaybe.isEmpty())
            return false;
        DB.remove(playerMaybe.get());
        return true;
    }

    @Override
    public boolean updatePlayerById(UUID id, Player player) {
        return selectPlayerById(id)
                .map(p -> {
                    int index = DB.indexOf(p);
                    if (index >= 0)
                    {
                        DB.set(index, player);
                        return true;
                    }
                    return false;
                }).orElse(false);
    }
}
