import java.io.*;
import java.util.*;

/**
 *  Use the selection sort algorithm to sort the card pile
 *
 *  @author Mulangma “Isabella" Zhu
 *  @version 28 February 2017
 */
public class CardSelSort {

    /** Keeps track of moves when sorting */
    public static SortRecorder record = new SortRecorder();

    /**
     * Uses Selection Sort to sort cards
     *
     * @param pile The unsorted pile of cards
     * @return The pile of cards after sorting
     */
    public static CardPile SelSort(CardPile pile){
        // register the starting configuration with the recorder
        record.add(pile);

        Card small, card;
        // outer loop started from the sorted part
        for (int i = 0; i < pile.size(); i++){
            small = pile.get(i);
            // inner loop started from the unsorted part
            for (int j = i+1; j < pile.size(); j++){
                card = pile.get(j);
                if (small.compareTo(card)>0){
                    small = card;
                }
            }
            // move small to the sorted part, before the previous small
            pile.remove(small);
            pile.add(i,small);

            // register the new state with the recorder
            record.next();
            record.add(pile);
        }
        return pile;
    }

    /**
     * Starts the program running
     */
    public static void main(String args[]) {
        // set up the deck of cards
        Card.loadImages(record);
        CardPile cards = new CardPile(Card.newDeck(), 2, 2);
        for (Card c : cards) {
            c.flipCard();
        }

        // uncomment this to work with a smaller number of cards
        cards = cards.split(cards.get(39));

        // mix up the cards
        Collections.shuffle(cards);

        // in your program, this would be a call to a sorting algorithm
        cards = SelSort(cards);

        // output (un)sorted result:
        System.out.println(cards);

        // make window appear showing the record
        record.display("Card Sort Demo");
    }
}





