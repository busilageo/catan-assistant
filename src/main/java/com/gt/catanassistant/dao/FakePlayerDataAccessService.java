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
    public Player selectPlayerById(UUID id) {
        Optional<Player> playerMaybe = DB.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();
        return playerMaybe.orElse(null);
    }

    @Override
    public UUID selectPlayerIdByName(String name)
    {
        Optional<Player> playerMaybe = DB.stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
        return playerMaybe.map(Player::getId).orElse(null);
    }

    @Override
    public boolean deletePlayerById(UUID id) {
        Player playerMaybe = selectPlayerById(id);
        if (playerMaybe == null)
            return false;
        DB.remove(playerMaybe);
        return true;
    }

    @Override
    public boolean updatePlayerById(UUID id, Player player) {
        Player p = selectPlayerById(id);
        int index = DB.indexOf(p);

        if (index >= 0)
        {
            DB.set(index, new Player(id, player));
            return true;
        }

        return false;
    }
}
