package com.gt.catanassistant.dao;

import com.gt.catanassistant.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerDao
{
    boolean insertPlayer(UUID id, Player player);
    default boolean insertPlayer(Player player)
    {
        UUID id = UUID.randomUUID();
        return insertPlayer(id, player);
    }
    List<Player> selectAllPlayers();
    Optional<Player> selectPlayerById(UUID id);
    boolean deletePlayerById(UUID id);
    boolean updatePlayerById(UUID id, Player player);
}
