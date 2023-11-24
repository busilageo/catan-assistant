package com.gt.catanassistant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CardCombination {
    private int lumber;
    private int brick;
    private int wool;
    private int grain;
    private int ore;

    public CardCombination(@JsonProperty("lumber") int lumber,
                           @JsonProperty("brick") int brick,
                           @JsonProperty("wool") int wool,
                           @JsonProperty("grain") int grain,
                           @JsonProperty("ore") int ore)
    {
        this.lumber = lumber;
        this.brick = brick;
        this.wool = wool;
        this.grain = grain;
        this.ore = ore;
    }
    public CardCombination() {
        this.lumber = 0;
        this.brick = 0;
        this.wool = 0;
        this.grain = 0;
        this.ore = 0;
    }

    public CardCombination(CardCombination cardCombination) {
        this.lumber = cardCombination.getLumber();
        this.brick = cardCombination.getBrick();
        this.wool = cardCombination.getWool();
        this.grain = cardCombination.getGrain();
        this.ore = cardCombination.getOre();
    }

    public int getLumber() {
        return lumber;
    }

    public int getBrick() {
        return brick;
    }

    public int getWool() {
        return wool;
    }

    public int getGrain() {
        return grain;
    }

    public int getOre() {
        return ore;
    }

    public int getResource(String resource)
    {
        if (resource.equals("lumber"))
            return lumber;
        if (resource.equals("brick"))
            return brick;
        if (resource.equals("wool"))
            return wool;
        if (resource.equals("grain"))
            return grain;
        if (resource.equals("ore"))
            return ore;
        return -1;
    }

    public void setLumber(int lumber) {
        this.lumber = lumber;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public void setGrain(int grain) {
        this.grain = grain;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }

    public boolean invalid()
    {
        return lumber < 0 ||
                brick < 0 ||
                wool < 0 ||
                grain < 0 ||
                ore < 0;
    }

    public void incrementResource(String resource)
    {
        if (resource.equals("lumber"))
            lumber++;
        if (resource.equals("brick"))
            brick++;
        if (resource.equals("wool"))
            wool++;
        if (resource.equals("grain"))
            grain++;
        if (resource.equals("ore"))
            ore++;
    }

    public void decrementResource(String resource)
    {
        if (resource.equals("lumber"))
            lumber--;
        if (resource.equals("brick"))
            brick--;
        if (resource.equals("wool"))
            wool--;
        if (resource.equals("grain"))
            grain--;
        if (resource.equals("ore"))
            ore--;
    }

    public void add(CardCombination cardCombination)
    {
        this.lumber += cardCombination.getLumber();
        this.brick += cardCombination.getBrick();
        this.wool += cardCombination.getWool();
        this.grain += cardCombination.getGrain();
        this.ore += cardCombination.getOre();
    }

    public void subtract(CardCombination cardCombination)
    {
        this.lumber -= cardCombination.getLumber();
        this.brick -= cardCombination.getBrick();
        this.wool -= cardCombination.getWool();
        this.grain -= cardCombination.getGrain();
        this.ore -= cardCombination.getOre();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CardCombination that = (CardCombination) obj;

        // Implement a custom equals method based on your requirements
        return (lumber == that.lumber &&
                brick == that.brick &&
                wool == that.wool &&
                grain == that.grain &&
                ore == that.ore);
    }

    @Override
    public int hashCode()
    {
        // Implement a custom hashCode method based on your requirements
        return Objects.hash(lumber, brick, wool, grain, ore);
    }

    public static CardCombination stringToCardCombination(String string)
    {
        CardCombination cardCombination = new CardCombination();
        String[] resources = string.split(" ");

        for (String resource : resources)
            cardCombination.incrementResource(resource);

        return cardCombination;
    }
}
