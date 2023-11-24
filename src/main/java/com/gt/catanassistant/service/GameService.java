package com.gt.catanassistant.service;

import com.gt.catanassistant.dao.GameDao;
import com.gt.catanassistant.model.CardCombination;
import com.gt.catanassistant.model.Game;
import com.gt.catanassistant.model.Player;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameDao gameDao;

    public GameService(@Qualifier("fakeGameDao") GameDao gameDao)
    {
        this.gameDao = gameDao;
    }

    public void addGame(Game game) {
        gameDao.addGame(game.getId(), game);
    }
    public List<Game> getAllGames() { return gameDao.getAllGames(); }
    public Game getGameById(UUID id) { return  gameDao.getGameById(id); }
    public void deleteGameById(UUID id) { gameDao.deleteGameById(id); }

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

    public void handleRequest(UUID id, String request)
    {
        if (request.contains("received starting resources")) {
            String name = request.split("received starting resources")[0].trim();
            CardCombination cardCombination = CardCombination.stringToCardCombination(request.split("received starting resources")[1].trim());
            List<CardCombination> cardCombinations = new ArrayList<>();
            cardCombinations.add(cardCombination);
            gameDao.getGameById(id).addPlayer(new Player(name, cardCombinations));

        } else if (request.contains("rolled")) {
            //TODO game started

        } else if (request.contains("got")) {
            String name = request.split("got")[0].trim();
            CardCombination cardCombinationToAdd = CardCombination.stringToCardCombination(request.split("got")[1].trim());
            Player player = gameDao.getGameById(id).getPlayerByName(name);
            List<CardCombination> cardCombinations = player.getCardCombinations();
            for (CardCombination cardCombination : cardCombinations)
                cardCombination.add(cardCombinationToAdd);

        } else if (request.contains("discarded")) {
            String name = request.split("discarded")[0].trim();
            CardCombination cardCombinationToAdd = CardCombination.stringToCardCombination(request.split("discarded")[1].trim());
            Player player = gameDao.getGameById(id).getPlayerByName(name);
            List<CardCombination> cardCombinations = player.getCardCombinations();
            for (CardCombination cardCombination : cardCombinations)
                cardCombination.subtract(cardCombinationToAdd);
            player.removeDuplicatesAndInvalids();

        } else if (request.contains("stole card from"))
        {
            String player1Name = request.split("stole card from")[0].trim();
            String player2Name = request.split("stole card from")[1].trim();

            Player player1 = gameDao.getGameById(id).getPlayerByName(player1Name);
            Player player2 = gameDao.getGameById(id).getPlayerByName(player2Name);

            steal(player1, player2);

        } else if (request.contains("traded"))
        {
            String player1Name = request.split("traded")[0].trim();
            String player1Trade = request.split("traded")[1].split("for")[0].trim();
            String player2Name = request.split("with")[1].trim();
            String player2Trade = request.split("for")[1].split("with")[0].trim();
            Player player1 = gameDao.getGameById(id).getPlayerByName(player1Name);
            Player player2 = gameDao.getGameById(id).getPlayerByName(player2Name);

            for (CardCombination combination : player1.getCardCombinations())
            {
                combination.subtract(CardCombination.stringToCardCombination(player1Trade));
                combination.add(CardCombination.stringToCardCombination(player2Trade));
            }
            for (CardCombination combination : player2.getCardCombinations())
            {
                combination.subtract(CardCombination.stringToCardCombination(player2Trade));
                combination.add(CardCombination.stringToCardCombination(player1Trade));
            }
            player1.removeDuplicatesAndInvalids();
            player2.removeDuplicatesAndInvalids();

        } else if (request.contains("gave bank"))
        {
            String name = request.split("gave bank")[0].trim();
            String gave = request.split("gave bank")[1].split("and took")[0].trim();
            String took = request.split("and took")[1].trim();
            Player player = gameDao.getGameById(id).getPlayerByName(name);

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                cardCombination.subtract(CardCombination.stringToCardCombination(gave));
                cardCombination.add(CardCombination.stringToCardCombination(took));
            }
            player.removeDuplicatesAndInvalids();

        } else if (request.contains("built"))
        {
            String name = request.split("built")[0].trim();
            Player player = gameDao.getGameById(id).getPlayerByName(name);

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                if (request.contains("road"))
                {
                    cardCombination.decrementResource("lumber");
                    cardCombination.decrementResource("brick");
                }
                else if (request.contains("settlement"))
                {
                    cardCombination.decrementResource("lumber");
                    cardCombination.decrementResource("brick");
                    cardCombination.decrementResource("wool");
                    cardCombination.decrementResource("grain");
                }
                else if (request.contains("city"))
                {
                    for (int i = 0; i < 2; i++)
                    {
                        cardCombination.decrementResource("grain");
                        cardCombination.decrementResource("ore");
                    }
                    cardCombination.decrementResource("ore");
                }
            }

            player.removeDuplicatesAndInvalids();

        } else if (request.contains("bought development card"))
        {
            String name = request.split("bought")[0].trim();
            Player player = gameDao.getGameById(id).getPlayerByName(name);

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                cardCombination.decrementResource("wool");
                cardCombination.decrementResource("grain");
                cardCombination.decrementResource("ore");
            }

            player.removeDuplicatesAndInvalids();

        }
        // Year of plenty
        else if (request.contains("took from bank"))
        {
            String name = request.split("took")[0].trim();
            String took = request.split("bank")[1].trim();
            Player player = gameDao.getGameById(id).getPlayerByName(name);

            for (CardCombination cardCombination : player.getCardCombinations())
                cardCombination.add(CardCombination.stringToCardCombination(took));

        } else if (request.contains("stole") && !request.contains("card"))
        {
            String name = request.split("stole")[0].trim();
            int amount = Integer.parseInt(request.split("stole")[1].trim().split(" ")[0]);
            String resource = request.split(" ")[3].trim();
            Player player = gameDao.getGameById(id).getPlayerByName(name);

            for (CardCombination cardCombination : player.getCardCombinations())
            {
                for (int i = 0; i < amount; i++)
                    cardCombination.incrementResource(resource);
            }

            for (Player playerFromList : gameDao.getGameById(id).getPlayers())
                if (!playerFromList.equals(player))
                {
                    for (CardCombination cardCombination : playerFromList.getCardCombinations())
                        while (cardCombination.getResource(resource) > 0)
                            cardCombination.decrementResource(resource);
                }

        } else if (request.contains("trophy"))
        {
            //TODO game ended
        }
    }
}
