package com.gt.catanassistant.model;

import java.util.Set;

public interface PlayerActions
{
    void addCombination(CardCombination combination);
    Set<CardCombination> getAllCombinations();

    boolean canBuildRoad();
    boolean canBuildSettlement();
    boolean canBuildCity();
    boolean canBuyDevelopmentCard();
    boolean canTradeBank();
    void buildRoad();
    void buildSettlement();
    void buildCity();
    void buyDevelopmentCard();
    void rob();
    void getRobbed();
}
