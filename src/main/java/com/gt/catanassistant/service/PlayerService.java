package com.gt.catanassistant.service;

import com.gt.catanassistant.dao.PlayerDao;
import com.gt.catanassistant.model.CardCombination;
import com.gt.catanassistant.model.Player;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

//TODO handle player initiating with no resources
//Rettig0613 placed a settlement
//UserRettig0613 placed a road
@Service
public class PlayerService
{
    private final PlayerDao playerDao;

    public PlayerService(@Qualifier("fakeDao") PlayerDao playerDao) { this.playerDao = playerDao; }

    public boolean addPlayer(Player player) { return playerDao.insertPlayer(player); }
    public List<Player> getAllPlayers() { return playerDao.selectAllPlayers(); }
    public Player getPlayerById(UUID id) { return  playerDao.selectPlayerById(id); }
    public UUID getPlayerIdByName(String name) { return playerDao.selectPlayerIdByName(name); }
    public boolean deletePlayerById(UUID id) { return playerDao.deletePlayerById(id); }
    public boolean updatePlayerById(UUID id, Player player) { return playerDao.updatePlayerById(id, player); }

    public CardCombination stringToCardCombination(String string)
    {
        CardCombination cardCombination = new CardCombination();
        String[] resources = string.split(" ");

        for (String resource : resources)
            cardCombination.incrementResource(resource);

        return cardCombination;
    }

    public boolean resolveAction(String action) {
        if (action.contains("received starting resources")) {
            String name = action.split("received starting resources")[0].trim();
            CardCombination cardCombination = stringToCardCombination(action.split("received starting resources")[1].trim());
            List<CardCombination> cardCombinations = new ArrayList<>();
            cardCombinations.add(cardCombination);
            addPlayer(new Player(name, cardCombinations));

        } else if (action.contains("rolled")) {
            //TODO game started
            return false;

        } else if (action.contains("got")) {
            String name = action.split("got")[0].trim();
            CardCombination cardCombinationToAdd = stringToCardCombination(action.split("got")[1].trim());
            UUID id = getPlayerIdByName(name);
            Player player = getPlayerById(id);
            List<CardCombination> cardCombinations = player.getCardCombinations();
            for (CardCombination cardCombination : cardCombinations)
                cardCombination.add(cardCombinationToAdd);

        } else if (action.contains("discarded")) {
            String name = action.split("discarded")[0].trim();
            CardCombination cardCombinationToAdd = stringToCardCombination(action.split("discarded")[1].trim());
            UUID id = getPlayerIdByName(name);
            Player player = getPlayerById(id);
            List<CardCombination> cardCombinations = player.getCardCombinations();
            for (CardCombination cardCombination : cardCombinations)
                cardCombination.subtract(cardCombinationToAdd);
            player.removeDuplicatesAndInvalids();

        } else if (action.contains("stole card from"))
        {
            String player1Name = action.split("stole card from")[0].trim();
            String player2Name = action.split("stole card from")[1].trim();

            Player player1 = getPlayerById(getPlayerIdByName(player1Name));
            Player player2 = getPlayerById(getPlayerIdByName(player2Name));

            steal(player1, player2);

        } else if (action.contains("traded"))
        {
            String player1Name = action.split("traded")[0].trim();
            String player1Trade = action.split("traded")[1].split("for")[0].trim();
            String player2Name = action.split("with")[1].trim();
            String player2Trade = action.split("for")[1].split("with")[0].trim();
            Player player1 = getPlayerById(getPlayerIdByName(player1Name));
            Player player2 = getPlayerById(getPlayerIdByName(player2Name));
            List<CardCombination> newCardCombinations1 = new ArrayList<>();
            List<CardCombination> newCardCombinations2 = new ArrayList<>();

            for (CardCombination combination : player1.getCardCombinations())
            {
                combination.subtract(stringToCardCombination(player1Trade));
                combination.add(stringToCardCombination(player2Trade));
                newCardCombinations1.add(combination);
            }
            for (CardCombination combination : player2.getCardCombinations())
            {
                combination.subtract(stringToCardCombination(player2Trade));
                combination.add(stringToCardCombination(player1Trade));
                newCardCombinations2.add(combination);
            }
            player1.removeDuplicatesAndInvalids();
            player2.removeDuplicatesAndInvalids();

        } else if (action.contains("gave bank"))
        {
            String name = action.split("gave bank")[0].trim();
            String gave = action.split("gave bank")[1].split("and took")[0].trim();
            String took = action.split("and took")[1].trim();
            Player player = getPlayerById(getPlayerIdByName(name));
            List<CardCombination> newCardCombinations = new ArrayList<>();

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                cardCombination.subtract(stringToCardCombination(gave));
                cardCombination.add(stringToCardCombination(took));
                newCardCombinations.add(cardCombination);
            }
            player.removeDuplicatesAndInvalids();

        } else if (action.contains("built"))
        {
            String name = action.split("built")[0].trim();
            Player player = getPlayerById(getPlayerIdByName(name));
            List<CardCombination> newCardCombinations = new ArrayList<>();

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                if (action.contains("road"))
                {
                    cardCombination.decrementResource("lumber");
                    cardCombination.decrementResource("brick");
                }
                else if (action.contains("settlement"))
                {
                    cardCombination.decrementResource("lumber");
                    cardCombination.decrementResource("brick");
                    cardCombination.decrementResource("wool");
                    cardCombination.decrementResource("grain");
                }
                else if (action.contains("city"))
                {
                    for (int i = 0; i < 2; i++)
                    {
                        cardCombination.decrementResource("grain");
                        cardCombination.decrementResource("ore");
                    }
                    cardCombination.decrementResource("ore");
                }
                newCardCombinations.add(cardCombination);
            }

            player.removeDuplicatesAndInvalids();

        } else if (action.contains("bought development card"))
        {
            String name = action.split("bought")[0].trim();
            Player player = getPlayerById(getPlayerIdByName(name));

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                cardCombination.decrementResource("wool");
                cardCombination.decrementResource("grain");
                cardCombination.decrementResource("ore");
            }

            player.removeDuplicatesAndInvalids();

        }
        // Year of plenty
        else if (action.contains("took from bank"))
        {
            String name = action.split("took")[0].trim();
            String took = action.split("bank")[1].trim();
            Player player = getPlayerById(getPlayerIdByName(name));
            List<CardCombination> newCardCombinations = new ArrayList<>();

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                cardCombination.add(stringToCardCombination(took));

                newCardCombinations.add(cardCombination);
            }

        } else if (action.contains("stole") && !action.contains("card"))
        {
            String name = action.split("stole")[0].trim();
            int amount = Integer.parseInt(action.split("stole")[1].trim().split(" ")[0]);
            String resource = action.split(" ")[3].trim();
            Player player = getPlayerById(getPlayerIdByName(name));
            List<CardCombination> newCardCombinations = new ArrayList<>();

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                for (int i = 0; i < amount; i++)
                    cardCombination.incrementResource(resource);

                newCardCombinations.add(cardCombination);
            }

            for (Player playerFromList : getAllPlayers())
                if (!playerFromList.equals(player))
                {
                    for (CardCombination cardCombination : playerFromList.getCardCombinations())
                        while (cardCombination.getResource(resource) > 0)
                            cardCombination.decrementResource(resource);
                }

        } else if (action.contains("trophy"))
        {
            //TODO game ended
            return false;
        }
        return false;
    }

    public void steal(Player player1, Player player2)
    {
        int size1 = player1.getCardCombinations().size();
        int size2 = player2.getCardCombinations().size();

        String[] resources = {"lumber", "brick", "wool", "grain", "ore"};

        for (int i = 0; i < size2; i++)
        {
            for (String res : resources)
                if (player2.getCardCombinations().get(i).getResource(res) > 0)
                {
                    CardCombination combo2 = new CardCombination(player2.getCardCombinations().get(i));
                    combo2.decrementResource(res);
                    player2.addCardCombination(combo2);

                    for (int j = 0; j < size1; j++)
                    {
                        CardCombination combo1 = new CardCombination(player1.getCardCombinations().get(j));
                        combo1.incrementResource(res);
                        player1.addCardCombination(combo1);
                    }

                }
        }
        for (int i = size1 - 1; i >= 0; i--)
            player1.removeCardCombination(i);
        for (int i = size2 - 1; i >= 0; i--)
            player2.removeCardCombination(i);

        player1.removeDuplicatesAndInvalids();
        player2.removeDuplicatesAndInvalids();
    }
}
