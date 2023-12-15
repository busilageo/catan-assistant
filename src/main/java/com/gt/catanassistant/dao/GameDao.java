package com.gt.catanassistant.dao;

import com.gt.catanassistant.model.Game;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameDao extends CrudRepository<Game, UUID> {
}
