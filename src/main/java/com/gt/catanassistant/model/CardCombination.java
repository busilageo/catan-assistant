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
    /*
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

    public void setResource(String res, int value)
    {
        if (res.equals("lumber"))
            this.lumber = value;
        else if (res.equals("brick"))
            this.brick = value;
        else if (res.equals("wool"))
            this.wool = value;
        else if (res.equals("grain"))
            this.grain = value;
        else if (res.equals("ore"))
            this.ore = value;
    }

    public boolean invalid()
    {
        return lumber < 0 ||
                brick < 0 ||
                wool < 0 ||
                grain < 0 ||
                ore < 0;
    }*/

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CardCombination that = (CardCombination) obj;

        // Implement a custom equals method based on your requirements
        return lumber == that.lumber &&
                brick == that.brick &&
                wool == that.wool &&
                grain == that.grain &&
                ore == that.ore;
    }

    @Override
    public int hashCode()
    {
        // Implement a custom hashCode method based on your requirements
        return Objects.hash(lumber, brick, wool, grain, ore);
    }
}
