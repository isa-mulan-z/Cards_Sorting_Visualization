import java.awt.*;
import java.util.*;

/**
 *  This class stores a collection of playing cards 
 *  in a linked list format.
 *
 *  @author Mulangma "Isabella" Zhu and Ha Cao
 *  @version CSC 212, Feb. 2017
 */
public class CardPile extends LinkedList<Card> {
	/** Location of the pile of cards on the table */
	private int x,y;

	/** Offset between cards in the pile */
	private int offsetX = 12, offsetY = 0;

	/** Constructor initializes location of empty pile */
	public CardPile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/** Constructor puts array of cards into pile */
	public CardPile(Card[] cards, int x, int y) {
		super(Arrays.asList(cards));
		this.x = x;
		this.y = y;
	}

	/** Copy constructor */
	public CardPile(CardPile pile) {
		super(pile);
		this.x = pile.x;
		this.y = pile.y;
	}

	/** Accessor for x coordinate of pile */
	public int getX() {
		return x;
	}

	/** Accessor for y coordinate of pile */
	public int getY() {
		return y;
	}

	/** Manipulator for x coordinate of pile */
	public void setX(int x) {
		this.x = x;
	}

	/** Manipulator for y coordinate of pile */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 *  Insert a card node before the specified marker
	 *
	 *  @param card  The card to insert
	 *  @param mark  New card goes before this one
	 */
	public void insertBefore(Card card, Card mark) {
		ListIterator<Card> position = listIterator(size());
		if (size()==0) {
			position.add(card); // Add the card into the empty list
		}
		else {
			while (position.hasPrevious()&&(position.previous()!=mark)) {
				// Keep going until we find our card
			}
			position.add(card);  // Add the card before a specified card in the list
		}
	}

	/**
	 *  Insert a card node after the specified marker
	 *
	 *  @param card  The card to insert
	 *  @param mark  New card goes after this one
	 */
	public void insertAfter(Card card, Card mark) {
		// FILLED ALREADY
		ListIterator<Card> position = listIterator();
		if (size()==0) {
			position.add(card); // Add the card into the empty list
		} 
		else {		
			while (position.hasNext()&&(position.next()!=mark)) {
				// Keep going until we find our card
			}
			position.add(card);  // Add the card after a specified card in the list
		}
	}

	/**
	 *  Inserts a one pile into another, leaving the inserted pile empty
	 *
	 *  @param insert  list to insert
	 *  @param mark  insert before this card
	 */
	public void insertBefore(CardPile insert, Card mark) {
		ListIterator<Card> position = listIterator(size());
		if (size()==0) {
			while (insert.size()>0) {
				position.add(insert.removeFirst()); // Add the card into the empty list
			}
		}
		else {
			// Empty loop to find the position to insert at
			while (position.hasPrevious()&&(position.previous()!=mark)) {
				// Nothing to do here
			}
			// Now move cards one at a time
			while (insert.size()>0) {
				position.add(insert.removeFirst());
			}
		}
	}

	/**
	 *  Inserts a one pile into another, leaving the inserted pile empty
	 *
	 *  @param insert list to insert
	 *  @param mark insert after this card
	 */
	public void insertAfter(CardPile insert, Card mark) {
		// FILLED ALREADY
		ListIterator<Card> position = listIterator();
		if (size()==0) {
			while (insert.size()>0) {
				position.add(insert.removeFirst()); // Add the card into the empty list
			}
		}
		else {
			// Empty loop to find the position to insert at
			while (position.hasNext()&&(position.next()!=mark)) {
				// nothing to do here
			}
			// Now move cards one at a time
			while (insert.size()>0) {
				position.add(insert.removeFirst());
			}		
		}
	}

	/**
	 *  Moves every element after the mark into a new pile
	 *  If mark is null, entire pile is moved
	 *  The location of the new pile will be (0,0)
	 *
	 *  @param mark  elements including and after this are moved
	 *  @return the suffix pile
	 */
	public CardPile split(Card mark) {
		// FILLED ALREADY
		CardPile suffix = new CardPile(0,0);
		ListIterator<Card> position = listIterator();
		// If statement to check if the list is empty or not
		if (size()==0) {
			return suffix; // Return empty suffix if the list is empty
		} 
		else {
			// If mark is null, move the whole pile
			if (mark==null) {
				while (position.hasNext()) {
					suffix.add(position.next());
					position.remove();
				}
				return suffix;
			}
			// If mark is a particular card, split from mark towards the end
			else {
				while (position.hasNext()&&(position.next()!=mark)) {
					// Nothing to do here
				}
				position.previous(); // Move the iterator to the place right before the mark
				while (position.hasNext()) {
					suffix.add(position.next());
					position.remove();
				}
				return suffix;
			}
		} 
	}

	/**
	 *  Appends the provided suffix onto this list.
	 *  If the suffix list is empty, nothing happens.
	 *  If this list is empty, the suffix list takes its place.
	 *
	 *  @param suffix list to append and empty
	 */
	public void append(CardPile suffix) {
		addAll(size(),suffix);
	}

	/**
	 *  Draws the pile at its location on the table.
	 *
	 *  @param g  Graphics object to draw into
	 */
	public void draw(Graphics g) {
		int cx = this.x, cy = this.y;
		for (Card card: this) {
			if (card.getIsFaceUp()) {
				g.drawImage(card.getFrontSide(),cx,cy,72,96,null);
			} else {
				g.drawImage(Card.getBackSide(),cx,cy,72,96,null);
			}
			cx += offsetX;
			cy += offsetY;
		}
	}

	/**
	 *  Determine if the specified click falls upon a card in this pile.
	 *  If so, return the node holding that card.
	 *
	 *  @param x  Coordinate of mouse click
	 *  @param y  Coordinate of mouse click
	 *  @return  Clicked Card, or null
	 */
	public Card locateCard(int x, int y) {
		Card result = null;
		int cx = this.x, cy = this.y;
		for (Card card: this) {
			if ((x >= cx)&&(x <= cx+72)&&(y >= cy)&&(y < cy+96)) {
				result = card;
			}
			cx += offsetX;
			cy += offsetY;
		}
		return result;
	}

	/**
	 *  Prints a representation of a CardPile
	 */
	public void print() {
		if (size()==0) {
			System.out.println("Empty pile.");
		} else {
			for (Card card: this){
				System.out.print(card+", ");
			}
			System.out.println("");
		}
	}
} // end of CardPile