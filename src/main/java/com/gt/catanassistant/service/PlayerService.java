package com.gt.catanassistant.service;

import com.gt.catanassistant.dao.PlayerDao;
import com.gt.catanassistant.model.Player;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService
{
    private final PlayerDao playerDao;

    public PlayerService(@Qualifier("fakeDao") PlayerDao playerDao) { this.playerDao = playerDao; }

    public boolean addPlayer(Player player) { return playerDao.insertPlayer(player); }
    public List<Player> getAllPlayers() { return playerDao.selectAllPlayers(); }
    public Optional<Player> getPlayerById(UUID id) { return  playerDao.selectPlayerById(id); }
    public boolean deletePlayerById(UUID id) { return playerDao.deletePlayerById(id); }
    public boolean updatePlayerById(UUID id, Player player) { return playerDao.updatePlayerById(id, player); }

}
