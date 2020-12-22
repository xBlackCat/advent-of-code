package org.xblackcat.adventofcode.year2020;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 22: Crab Combat ---
 * It only takes a few hours of sailing the ocean on a raft for boredom to sink in. Fortunately, you brought a small deck of space cards! You'd like to play a game of Combat, and there's even an opponent available: a small crab that climbed aboard your raft before you left.
 * <p>
 * Fortunately, it doesn't take long to teach the crab the rules.
 * <p>
 * Before the game starts, split the cards so each player has their own deck (your puzzle input). Then, the game consists of a series of rounds: both players draw their top card, and the player with the higher-valued card wins the round. The winner keeps both cards, placing them on the bottom of their own deck so that the winner's card is above the other card. If this causes a player to have all of the cards, they win, and the game ends.
 * <p>
 * For example, consider the following starting decks:
 * <pre>
 * Player 1:
 * 9
 * 2
 * 6
 * 3
 * 1
 *
 * Player 2:
 * 5
 * 8
 * 4
 * 7
 * 10</pre>
 * This arrangement means that player 1's deck contains 5 cards, with 9 on top and 1 on the bottom; player 2's deck also contains 5 cards, with 5 on top and 10 on the bottom.
 * <p>
 * The first round begins with both players drawing the top card of their decks: 9 and 5. Player 1 has the higher card, so both cards move to the bottom of player 1's deck such that 9 is above 5. In total, it takes 29 rounds before a player has all of the cards:
 * <pre>
 * -- Round 1 --
 * Player 1's deck: 9, 2, 6, 3, 1
 * Player 2's deck: 5, 8, 4, 7, 10
 * Player 1 plays: 9
 * Player 2 plays: 5
 * Player 1 wins the round!
 *
 * -- Round 2 --
 * Player 1's deck: 2, 6, 3, 1, 9, 5
 * Player 2's deck: 8, 4, 7, 10
 * Player 1 plays: 2
 * Player 2 plays: 8
 * Player 2 wins the round!
 *
 * -- Round 3 --
 * Player 1's deck: 6, 3, 1, 9, 5
 * Player 2's deck: 4, 7, 10, 8, 2
 * Player 1 plays: 6
 * Player 2 plays: 4
 * Player 1 wins the round!
 *
 * -- Round 4 --
 * Player 1's deck: 3, 1, 9, 5, 6, 4
 * Player 2's deck: 7, 10, 8, 2
 * Player 1 plays: 3
 * Player 2 plays: 7
 * Player 2 wins the round!
 *
 * -- Round 5 --
 * Player 1's deck: 1, 9, 5, 6, 4
 * Player 2's deck: 10, 8, 2, 7, 3
 * Player 1 plays: 1
 * Player 2 plays: 10
 * Player 2 wins the round!
 *
 * ...several more rounds pass...
 *
 * -- Round 27 --
 * Player 1's deck: 5, 4, 1
 * Player 2's deck: 8, 9, 7, 3, 2, 10, 6
 * Player 1 plays: 5
 * Player 2 plays: 8
 * Player 2 wins the round!
 *
 * -- Round 28 --
 * Player 1's deck: 4, 1
 * Player 2's deck: 9, 7, 3, 2, 10, 6, 8, 5
 * Player 1 plays: 4
 * Player 2 plays: 9
 * Player 2 wins the round!
 *
 * -- Round 29 --
 * Player 1's deck: 1
 * Player 2's deck: 7, 3, 2, 10, 6, 8, 5, 9, 4
 * Player 1 plays: 1
 * Player 2 plays: 7
 * Player 2 wins the round!
 *
 * == Post-game results ==
 * Player 1's deck:
 * Player 2's deck: 3, 2, 10, 6, 8, 5, 9, 4, 7, 1
 * </pre>
 * Once the game ends, you can calculate the winning player's score. The bottom card in their deck is worth the value of the card multiplied by 1, the second-from-the-bottom card is worth the value of the card multiplied by 2, and so on. With 10 cards, the top card is worth the value on the card multiplied by 10. In this example, the winning player's score is:
 * <pre>
 *    3 * 10
 * +  2 *  9
 * + 10 *  8
 * +  6 *  7
 * +  8 *  6
 * +  5 *  5
 * +  9 *  4
 * +  4 *  3
 * +  7 *  2
 * +  1 *  1
 * = 306</pre>
 * So, once the game ends, the winning player's score is 306.
 * <p>
 * Play the small crab in a game of Combat using the two decks you just dealt. What is the winning player's score?
 * <p>
 * --- Part Two ---
 * You lost to the small crab! Fortunately, crabs aren't very good at recursion. To defend your honor as a Raft Captain, you challenge the small crab to a game of Recursive Combat.
 * <p>
 * Recursive Combat still starts by splitting the cards into two decks (you offer to play with the same starting decks as before - it's only fair). Then, the game consists of a series of rounds with a few changes:
 * <ul>
 * <li>Before either player deals a card, if there was a previous round in this game that had exactly the same cards in the same order in the same players' decks, the game instantly ends in a win for player 1. Previous rounds from other games are not considered. (This prevents infinite games of Recursive Combat, which everyone agrees is a bad idea.)</li>
 * <li>Otherwise, this round's cards must be in a new configuration; the players begin the round by each drawing the top card of their deck as normal.</li>
 * <li>If both players have at least as many cards remaining in their deck as the value of the card they just drew, the winner of the round is determined by playing a new game of Recursive Combat (see below).</li>
 * <li>Otherwise, at least one player must not have enough cards left in their deck to recurse; the winner of the round is the player with the higher-value card.</li>
 * <li>As in regular Combat, the winner of the round (even if they won the round by winning a sub-game) takes the two cards dealt at the beginning of the round and places them on the bottom of their own deck (again so that the winner's card is above the other card). Note that the winner's card might be the lower-valued of the two cards if they won the round due to winning a sub-game. If collecting cards by winning the round causes a player to have all of the cards, they win, and the game ends.</li>
 * </ul>
 * Here is an example of a small game that would loop forever without the infinite game prevention rule:
 * <pre>
 * Player 1:
 * 43
 * 19
 *
 * Player 2:
 * 2
 * 29
 * 14</pre>
 * During a round of Recursive Combat, if both players have at least as many cards in their own decks as the number on the card they just dealt, the winner of the round is determined by recursing into a sub-game of Recursive Combat. (For example, if player 1 draws the 3 card, and player 2 draws the 7 card, this would occur if player 1 has at least 3 cards left and player 2 has at least 7 cards left, not counting the 3 and 7 cards that were drawn.)
 * <p>
 * To play a sub-game of Recursive Combat, each player creates a new deck by making a copy of the next cards in their deck (the quantity of cards copied is equal to the number on the card they drew to trigger the sub-game). During this sub-game, the game that triggered it is on hold and completely unaffected; no cards are removed from players' decks to form the sub-game. (For example, if player 1 drew the 3 card, their deck in the sub-game would be copies of the next three cards in their deck.)
 * <p>
 * Here is a complete example of gameplay, where Game 1 is the primary game of Recursive Combat:
 * <pre>
 * === Game 1 ===
 *
 * -- Round 1 (Game 1) --
 * Player 1's deck: 9, 2, 6, 3, 1
 * Player 2's deck: 5, 8, 4, 7, 10
 * Player 1 plays: 9
 * Player 2 plays: 5
 * Player 1 wins round 1 of game 1!
 *
 * -- Round 2 (Game 1) --
 * Player 1's deck: 2, 6, 3, 1, 9, 5
 * Player 2's deck: 8, 4, 7, 10
 * Player 1 plays: 2
 * Player 2 plays: 8
 * Player 2 wins round 2 of game 1!
 *
 * -- Round 3 (Game 1) --
 * Player 1's deck: 6, 3, 1, 9, 5
 * Player 2's deck: 4, 7, 10, 8, 2
 * Player 1 plays: 6
 * Player 2 plays: 4
 * Player 1 wins round 3 of game 1!
 *
 * -- Round 4 (Game 1) --
 * Player 1's deck: 3, 1, 9, 5, 6, 4
 * Player 2's deck: 7, 10, 8, 2
 * Player 1 plays: 3
 * Player 2 plays: 7
 * Player 2 wins round 4 of game 1!
 *
 * -- Round 5 (Game 1) --
 * Player 1's deck: 1, 9, 5, 6, 4
 * Player 2's deck: 10, 8, 2, 7, 3
 * Player 1 plays: 1
 * Player 2 plays: 10
 * Player 2 wins round 5 of game 1!
 *
 * -- Round 6 (Game 1) --
 * Player 1's deck: 9, 5, 6, 4
 * Player 2's deck: 8, 2, 7, 3, 10, 1
 * Player 1 plays: 9
 * Player 2 plays: 8
 * Player 1 wins round 6 of game 1!
 *
 * -- Round 7 (Game 1) --
 * Player 1's deck: 5, 6, 4, 9, 8
 * Player 2's deck: 2, 7, 3, 10, 1
 * Player 1 plays: 5
 * Player 2 plays: 2
 * Player 1 wins round 7 of game 1!
 *
 * -- Round 8 (Game 1) --
 * Player 1's deck: 6, 4, 9, 8, 5, 2
 * Player 2's deck: 7, 3, 10, 1
 * Player 1 plays: 6
 * Player 2 plays: 7
 * Player 2 wins round 8 of game 1!
 *
 * -- Round 9 (Game 1) --
 * Player 1's deck: 4, 9, 8, 5, 2
 * Player 2's deck: 3, 10, 1, 7, 6
 * Player 1 plays: 4
 * Player 2 plays: 3
 * Playing a sub-game to determine the winner...
 *
 * === Game 2 ===
 *
 * -- Round 1 (Game 2) --
 * Player 1's deck: 9, 8, 5, 2
 * Player 2's deck: 10, 1, 7
 * Player 1 plays: 9
 * Player 2 plays: 10
 * Player 2 wins round 1 of game 2!
 *
 * -- Round 2 (Game 2) --
 * Player 1's deck: 8, 5, 2
 * Player 2's deck: 1, 7, 10, 9
 * Player 1 plays: 8
 * Player 2 plays: 1
 * Player 1 wins round 2 of game 2!
 *
 * -- Round 3 (Game 2) --
 * Player 1's deck: 5, 2, 8, 1
 * Player 2's deck: 7, 10, 9
 * Player 1 plays: 5
 * Player 2 plays: 7
 * Player 2 wins round 3 of game 2!
 *
 * -- Round 4 (Game 2) --
 * Player 1's deck: 2, 8, 1
 * Player 2's deck: 10, 9, 7, 5
 * Player 1 plays: 2
 * Player 2 plays: 10
 * Player 2 wins round 4 of game 2!
 *
 * -- Round 5 (Game 2) --
 * Player 1's deck: 8, 1
 * Player 2's deck: 9, 7, 5, 10, 2
 * Player 1 plays: 8
 * Player 2 plays: 9
 * Player 2 wins round 5 of game 2!
 *
 * -- Round 6 (Game 2) --
 * Player 1's deck: 1
 * Player 2's deck: 7, 5, 10, 2, 9, 8
 * Player 1 plays: 1
 * Player 2 plays: 7
 * Player 2 wins round 6 of game 2!
 * The winner of game 2 is player 2!
 *
 * ...anyway, back to game 1.
 * Player 2 wins round 9 of game 1!
 *
 * -- Round 10 (Game 1) --
 * Player 1's deck: 9, 8, 5, 2
 * Player 2's deck: 10, 1, 7, 6, 3, 4
 * Player 1 plays: 9
 * Player 2 plays: 10
 * Player 2 wins round 10 of game 1!
 *
 * -- Round 11 (Game 1) --
 * Player 1's deck: 8, 5, 2
 * Player 2's deck: 1, 7, 6, 3, 4, 10, 9
 * Player 1 plays: 8
 * Player 2 plays: 1
 * Player 1 wins round 11 of game 1!
 *
 * -- Round 12 (Game 1) --
 * Player 1's deck: 5, 2, 8, 1
 * Player 2's deck: 7, 6, 3, 4, 10, 9
 * Player 1 plays: 5
 * Player 2 plays: 7
 * Player 2 wins round 12 of game 1!
 *
 * -- Round 13 (Game 1) --
 * Player 1's deck: 2, 8, 1
 * Player 2's deck: 6, 3, 4, 10, 9, 7, 5
 * Player 1 plays: 2
 * Player 2 plays: 6
 * Playing a sub-game to determine the winner...
 *
 * === Game 3 ===
 *
 * -- Round 1 (Game 3) --
 * Player 1's deck: 8, 1
 * Player 2's deck: 3, 4, 10, 9, 7, 5
 * Player 1 plays: 8
 * Player 2 plays: 3
 * Player 1 wins round 1 of game 3!
 *
 * -- Round 2 (Game 3) --
 * Player 1's deck: 1, 8, 3
 * Player 2's deck: 4, 10, 9, 7, 5
 * Player 1 plays: 1
 * Player 2 plays: 4
 * Playing a sub-game to determine the winner...
 *
 * === Game 4 ===
 *
 * -- Round 1 (Game 4) --
 * Player 1's deck: 8
 * Player 2's deck: 10, 9, 7, 5
 * Player 1 plays: 8
 * Player 2 plays: 10
 * Player 2 wins round 1 of game 4!
 * The winner of game 4 is player 2!
 *
 * ...anyway, back to game 3.
 * Player 2 wins round 2 of game 3!
 *
 * -- Round 3 (Game 3) --
 * Player 1's deck: 8, 3
 * Player 2's deck: 10, 9, 7, 5, 4, 1
 * Player 1 plays: 8
 * Player 2 plays: 10
 * Player 2 wins round 3 of game 3!
 *
 * -- Round 4 (Game 3) --
 * Player 1's deck: 3
 * Player 2's deck: 9, 7, 5, 4, 1, 10, 8
 * Player 1 plays: 3
 * Player 2 plays: 9
 * Player 2 wins round 4 of game 3!
 * The winner of game 3 is player 2!
 *
 * ...anyway, back to game 1.
 * Player 2 wins round 13 of game 1!
 *
 * -- Round 14 (Game 1) --
 * Player 1's deck: 8, 1
 * Player 2's deck: 3, 4, 10, 9, 7, 5, 6, 2
 * Player 1 plays: 8
 * Player 2 plays: 3
 * Player 1 wins round 14 of game 1!
 *
 * -- Round 15 (Game 1) --
 * Player 1's deck: 1, 8, 3
 * Player 2's deck: 4, 10, 9, 7, 5, 6, 2
 * Player 1 plays: 1
 * Player 2 plays: 4
 * Playing a sub-game to determine the winner...
 *
 * === Game 5 ===
 *
 * -- Round 1 (Game 5) --
 * Player 1's deck: 8
 * Player 2's deck: 10, 9, 7, 5
 * Player 1 plays: 8
 * Player 2 plays: 10
 * Player 2 wins round 1 of game 5!
 * The winner of game 5 is player 2!
 *
 * ...anyway, back to game 1.
 * Player 2 wins round 15 of game 1!
 *
 * -- Round 16 (Game 1) --
 * Player 1's deck: 8, 3
 * Player 2's deck: 10, 9, 7, 5, 6, 2, 4, 1
 * Player 1 plays: 8
 * Player 2 plays: 10
 * Player 2 wins round 16 of game 1!
 *
 * -- Round 17 (Game 1) --
 * Player 1's deck: 3
 * Player 2's deck: 9, 7, 5, 6, 2, 4, 1, 10, 8
 * Player 1 plays: 3
 * Player 2 plays: 9
 * Player 2 wins round 17 of game 1!
 * The winner of game 1 is player 2!
 *
 *
 * == Post-game results ==
 * Player 1's deck:
 * Player 2's deck: 7, 5, 6, 2, 4, 1, 10, 8, 9, 3</pre>
 * After the game, the winning player's score is calculated from the cards they have in their original deck using the same rules as regular Combat. In the above game, the winning player's score is 291.
 * <p>
 * Defend your honor as Raft Captain by playing the small crab in a game of Recursive Combat using the same two decks as before. What is the winning player's score?
 */
public class Day22 {
    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    private static void part1() throws IOException {
        final TIntList[] decks = readDecks("/year2020/day22.txt");

        TIntList deck1 = decks[0];
        TIntList deck2 = decks[1];

        int round = playGame(deck1, deck2);

        TIntList winner;
        if (deck1.isEmpty()) {
            System.out.println("PLayer 2 wins!");
            winner = deck2;
        } else {
            System.out.println("PLayer 1 wins!");
            winner = deck1;
        }
        winner.reverse();
        int[] array = winner.toArray();
        int score = 0;
        for (int i = 0; i < array.length; i++) {
            int card = array[i];
            score += (i + 1) * card;
        }
        System.out.println("Last round: " + round + ", score = " + score);
    }

    private static void part2() throws IOException {
        final TIntList[] decks = readDecks("/year2020/day22.txt");

        TIntList deck1 = decks[0];
        TIntList deck2 = decks[1];
        final RecursiveGame game = new RecursiveGame();
        game.playGame(deck1, deck2);

        TIntList winner = deck1.isEmpty() ? deck2 : deck1;
        winner.reverse();
        int[] array = winner.toArray();
        int score = 0;
        for (int i = 0; i < array.length; i++) {
            int card = array[i];
            score += (i + 1) * card;
        }
        System.out.println("Winner score = " + score);
    }

    private static int playGame(TIntList deck1, TIntList deck2) {
        int round = 0;
        while (!deck1.isEmpty() && !deck2.isEmpty()) {
            round++;
            System.out.println("--- Round " + round + " ---");
            final int card1 = deck1.removeAt(0);
            final int card2 = deck2.removeAt(0);
            System.out.println("player 1: " + card1 + ", player 2: " + card2);
            if (card1 > card2) {
                System.out.println("Player 1 wins!");
                deck1.add(card1);
                deck1.add(card2);
            } else if (card1 < card2) {
                System.out.println("Player 2 wins!");
                deck2.add(card2);
                deck2.add(card1);
            }
        }
        return round;
    }

    private static TIntList[] readDecks(String name) throws IOException {
        List<TIntList> decks = new ArrayList<>();
        TIntList cards = new TIntArrayList();
        try (
                final InputStream stream = Day17.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    if (!cards.isEmpty()) {
                        decks.add(new TIntArrayList(cards));
                    }
                    continue;
                }
                if (line.startsWith("Player")) {
                    cards.clear();
                    continue;
                }
                cards.add(Integer.parseInt(line));
            }
        }
        if (!cards.isEmpty()) {
            decks.add(new TIntArrayList(cards));
        }
        return decks.toArray(new TIntList[0]);
    }

    private static class RecursiveGame {
        private int gameId = 1;

        public void playGame(TIntList deck1, TIntList deck2) {
            playRecursiveGame(deck1, deck2, gameId++);
        }

        private void playRecursiveGame(TIntList deck1, TIntList deck2, int gameNo) {
            List<TIntList> player1Rounds = new ArrayList<>();
            List<TIntList> player2Rounds = new ArrayList<>();
            int round = 0;
            while (!deck1.isEmpty() && !deck2.isEmpty()) {
                if (player1Rounds.contains(deck1) || player2Rounds.contains(deck2)) {
                    System.out.println("--- Recursive prevent rule! Player 1 wins (Game " + gameNo + ") ---");

                    deck1.addAll(deck2);
                    deck2.clear();
                    continue;
                }

                player1Rounds.add(new TIntArrayList(deck1));
                player2Rounds.add(new TIntArrayList(deck2));
                round++;
                System.out.println("--- Round " + round + "(Game " + gameNo + ") ---");
                final int card1 = deck1.removeAt(0);
                final int card2 = deck2.removeAt(0);

                if (card1 <= deck1.size() && card2 <= deck2.size()) {
                    final TIntList rDeck1 = deck1.subList(0, card1);
                    final TIntList rDeck2 = deck2.subList(0, card2);
                    final int subGame = gameId++;
                    playRecursiveGame(rDeck1, rDeck2, subGame);
                    if (rDeck1.isEmpty()) {
                        System.out.println("Player 2 wins game #" + subGame);
                        deck2.add(card2);
                        deck2.add(card1);
                    } else {
                        System.out.println("Player 1 wins game #" + subGame);
                        deck1.add(card1);
                        deck1.add(card2);
                    }
                } else {
                    System.out.println("player 1: " + card1 + ", player 2: " + card2);
                    if (card1 > card2) {
                        System.out.println("Player 1 wins!");
                        deck1.add(card1);
                        deck1.add(card2);
                    } else if (card1 < card2) {
                        System.out.println("Player 2 wins!");
                        deck2.add(card2);
                        deck2.add(card1);
                    }
                }
            }

            if (deck1.isEmpty()) {
                System.out.println("PLayer 2 wins game " + gameNo + "!");
            } else {
                System.out.println("PLayer 1 wins game " + gameNo + "!");
            }
        }
    }
}
