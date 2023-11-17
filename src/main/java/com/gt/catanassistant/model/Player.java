package com.gt.catanassistant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gt.catanassistant.dao.PlayerDao;

import java.util.*;

public class Player {
    private final UUID id;
    private final String name;
    private List<CardCombination> cardCombinations = new ArrayList<>();

    public Player(@JsonProperty("id") UUID id,
                  @JsonProperty("name") String name,
                  @JsonProperty("cardCombinations") List<CardCombination> cardCombinations) {
        this.id = id;
        this.name = name;
        this.cardCombinations = cardCombinations;
    }

    public Player(@JsonProperty("id") UUID id, Player player) {
        this.id = id;
        this.name = player.getName();
        this.cardCombinations = player.getCardCombinations();
    }

    public Player(String name, List<CardCombination> cardCombinations)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cardCombinations = cardCombinations;
    }

    public String getName() {
        return name;
    }

    public List<CardCombination> getCardCombinations() {
        return cardCombinations;
    }

    public void removeCombination(CardCombination cardCombination)
    {
        cardCombinations.remove(cardCombination);
    }

    public void addCardCombination(CardCombination cardCombination)
    {
        cardCombinations.add(cardCombination);
    }

    public void removeCardCombination(int index)
    {
        cardCombinations.remove(index);
    }

    public void removeCardCombination(CardCombination cardCombination)
    {
        cardCombinations.remove(cardCombination);
    }

    public void setCardCombinations(List<CardCombination> cardCombinations) {
        this.cardCombinations = cardCombinations;
    }

    public void removeDuplicatesAndInvalids()
    {
        for (int i = 0; i < getCardCombinations().size() - 1; i++)
            for (int j = i + 1; j < getCardCombinations().size(); j++)
                if (getCardCombinations().get(i).equals(getCardCombinations().get(j)))
                {
                    removeCardCombination(getCardCombinations().get(j));
                    j--;
                }
    }

    public UUID getId() {
        return id;
    }

    /*

    public boolean canMakeRoad()
    {
        for (CardCombination combination : cardCombinations)
            if (combination.getLumber() > 0 &&
                combination.getBrick() > 0)
                return true;
        return false;
    }

    public boolean canMakeSettlement()
    {
        for (CardCombination combination : cardCombinations)
            if (combination.getLumber() > 0 &&
                combination.getBrick() > 0 &&
                combination.getGrain() > 0 &&
                combination.getWool() > 0)
                return true;
        return false;
    }

    public boolean canMakeCity()
    {
        for (CardCombination combination : cardCombinations)
            if (combination.getGrain() >= 2 &&
                combination.getOre() >= 3)
                return true;
        return false;
    }

    public boolean canBuyDevelopmentCard()
    {
        for (CardCombination combination : cardCombinations)
            if (combination.getWool() > 0 &&
                combination.getGrain() > 0 &&
                combination.getOre() > 0)
                return true;
        return false;
    }

    public boolean canTradeBank()
    {
        for (CardCombination combination : cardCombinations)
            if (combination.getLumber() >= 4 ||
                combination.getBrick() >= 4 ||
                combination.getWool() >= 4 ||
                combination.getGrain() >= 4 ||
                combination.getOre() >= 4)
                return true;
        return false;
    }

    public void buildRoad()
    {
        for (CardCombination combination : cardCombinations)
        {
            combination.setResource("lumber", combination.getLumber() - 1);
            combination.setResource("brick", combination.getBrick() - 1);
        }
    }

    public boolean*/
}
