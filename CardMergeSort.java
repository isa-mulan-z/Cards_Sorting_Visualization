import java.util.*;

/**
 *  Use the merge sort algorithm to sort the card pile
 *
 *  @author Mulangma “Isabella" Zhu
 *  @version 28 February 2017
 */
public class CardMergeSort {

    /**
     * Keeps track of moves when sorting
     */
    public static SortRecorder record = new SortRecorder();

    /**
     * Uses Merge Sort to sort cards
     *
     * @param unsorted The unsorted pile of cards
     * @return The new sorted pile of cards
     */
    public static CardPile MergeSort(CardPile unsorted) {
        // register the starting configuration with the recorder
        record.add(unsorted);

        CardPile sorted = new CardPile(2,2);
        // place each card of unsorted pile into a new singleton pile,
        // and store all the singleton piles in a linked list of CardPiles called pilesList
        LinkedList<CardPile> pilesList = new LinkedList<>();
        for (int i = 0; i < unsorted.size(); i++){
            CardPile singletonpile = new CardPile(2,2);
            singletonpile.addLast(unsorted.get(i));
            pilesList.addLast(singletonpile);
        }

        // keep merging and sorting the first two piles into one,
        // and add to the last part of the pilesList
        while (pilesList.size() > 1){
            CardPile pile1 = pilesList.removeFirst();
            CardPile pile2 = pilesList.removeFirst();
            sorted = Merge(pile1,pile2);
            pilesList.addLast(sorted);
            // register the new state with the recorder
            record.next();
            for (int i = 0; i < pilesList.size(); i++){
                CardPile mergingpile = pilesList.get(i);
                record.add(mergingpile);
            }
        }
        return sorted;
    }

    /**
     *
     * @param pile1 one of the pile needed to merge
     * @param pile2 another pile needed to merge
     * @return a new pile that is merged and sorted
     */
    public static CardPile Merge(CardPile pile1, CardPile pile2) {
        CardPile newpile = new CardPile(2, 2);
        // compare the first card of each pile, and add the smaller one to the newpile
        while (pile1.size() >0 && pile2.size() >0) {
            // if the first card of pile1 is smaller than that of pile2
            if (pile1.getFirst().compareTo(pile2.getFirst())<0) {
                newpile.addLast(pile1.removeFirst());
            } else {
                newpile.addLast(pile2.removeFirst());
            }
        }
        // if one pile becomes empty, the while loop break, but there might be piles left
        // in the other pile, and thus append piles to newpile
        newpile.append(pile1);
        newpile.append(pile2);
        return newpile;
    }

    /** Starts the program running */
    public static void main(String args[]){
        // set up the deck of cards
        Card.loadImages(record);
        CardPile cards = new CardPile(Card.newDeck(),2,2);
        for (Card c: cards) {
            c.flipCard();
        }

        // work with a smaller number of cards
        cards = cards.split(cards.get(39));

        // mix up the cards
        Collections.shuffle(cards);

        // in your program, this would be a call to a sorting algorithm
        cards = MergeSort(cards);

        // output (un)sorted result:
        System.out.println(cards);

        // make window appear showing the record
        record.display("Card Sort Demo");
    }
}