import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 *  This class implements a graphical canvas in which card 
 *  piles are placed.  It will also contain a nested listener class
 *  to respond to and handle mouse events.
 *
 *  The canvas is large enough to contain five rows of cards.
 *  Each row has its associated fixed CardPile.  When initialized,
 *  all the cards are in the top pile and the others are empty.
 *
 *  CardTable should implement the following behavior:
 *  - When the user doubleclicks on a card, that card and all those
 *    on top of it on the pile should be flipped over
 *  - When the user drags a card, that card and all those on top of it
 *    on the pile should be removed from the pile they are on and
 *    follow the mouse around.
 *  - When the user releases the mouse while dragging a pile of cards,
 *    the pile should be inserted into some fixed pile according to
 *    where the mouse was released. 
 *  
 *  @author Mulangma "Isabella" Zhu, Ha Cao
 *  @version CSC 212, 21 February 2017
 */
public class CardTable extends JComponent {
	/** Gives the number of piles available */
	public static final int NPILE = 5;

	/** gives the width of the canvas */
	public static final int WIDTH = 800;

	/** gives the height of the canvas */
	public static final int HEIGHT = 500;

	/** Storage for each of the piles available */
	CardPile pile[] = new CardPile[NPILE];

	/** Storage for pile that is in motion */
	CardPile movingPile = null; // Set the default value

	/** Records card under last mouse press */
	Card cardUnderMouse;

	/** Records index of pile under last mouse press */
	CardPile pileUnderMouse;

	/** Initialize a table with a deck of cards in the first slot */
	public CardTable() {
		pile[0] = new CardPile(Card.newDeck(),2,2);
		pile[1] = new CardPile(2,102);
		pile[2] = new CardPile(2,202);
		pile[3] = new CardPile(2,302);
		pile[4] = new CardPile(2,402);
		ListIterator<Card> mark;

		// Register the listeners
		addMouseListener(new MouseMotionListener());
		addMouseMotionListener(new MouseMotionListener());

		// Add code here to turn/flip over all the cards
		// FILLED ALREADY		
		for (mark = pile[0].listIterator(); mark.hasNext();) {
			Card card = mark.next();
			card.flipCard();
		}

		// Sample card movements
		// Uncomment these one at a time to see what they do - DONE
		pile[1].addLast(pile[0].removeLast());
		pile[1].addLast(pile[0].removeLast());
		pile[1].addFirst(pile[0].removeFirst());

		// Now add your card movements for stage 1 here
		// FILLED ALREADY
		pile[2].addLast(pile[1].removeLast());
		pile[3].addLast(pile[1].removeLast());
		pile[4].addFirst(pile[1].removeFirst());		

		// Once you have written the split() method in CardPile 
		// you can uncomment and test the line below
		//pile[2].addAll(pile[0].split(pile[0].get(26)));

		// Next try other uses of split.
		// Then try out the various insert methods.
		// You should test out all the methods of CardGame that move cards
		// and make sure that they all work as intended.
		// FILLED ALREADY
		// Test insert a card after a null mark
		pile[1].insertAfter(pile[0].remove(25), null); 
		// Test insert a card before another card
		pile[2].insertBefore(pile[0].get(5), pile[2].get(0));
		// Test split a pile of cards and insert that pile after a particular card
		CardPile insert1 = pile[0].split(pile[0].get(26));
		pile[3].insertAfter(insert1, pile[3].get(0));
		// Test split a pile of cards and insert that pile before a particular card
		CardPile insert2 = pile[0].split(pile[0].get(5));
		pile[4].insertBefore(insert2, pile[4].get(0));	
	}

	/**
	 *  Returns the requested card pile
	 *
	 *  @param i  The index of the pile requested
	 *  @return   The requested pile, or null if the pile is empty
	 */
	public CardPile getPile(int i) {
		CardPile pile;
		if ((i >= 0)&&(i < NPILE)) {
			pile = this.pile[i];
		} else {
			pile = null;
		}
		return pile;
	}

	/**
	 *  Attaches the specified cards to the specified pile.
	 *  The location of the pile is set to a fixed location.
	 *
	 *  @param i  ID of the pile to use
	 *  @param pile  Cards to put there
	 */
	public void setPile(int i, CardPile pile) {
		if ((i >= 0)&&(i < NPILE)) {
			pile.setX(2);
			pile.setY(2+100*i);
			this.pile[i] = pile;
		}
	}

	/**
	 *  Draws the table and the cards upon it
	 *
	 *  @param g The graphics object to draw into
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.green.darker().darker());
		g.fillRect(0,0,WIDTH,HEIGHT);
		g.setColor(Color.black);
		for (int i = 0; i < pile.length; i++) {
			g.drawRect(2,2+100*i,72,96);
			pile[i].draw(g);
		}
		if (movingPile != null) {
			movingPile.draw(g);
		}
	}

	/**
	 *  The component will look bad if it is sized smaller than this
	 *
	 *  @return The minimum dimension
	 */
	public Dimension getMinimumSize() {
		return new Dimension(WIDTH,HEIGHT);
	}

	/**
	 *  The component will look best at this size
	 *
	 *  @return The preferred dimension
	 */
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH,HEIGHT);
	}

	/**
	 *  For debugging.  Runs validation tests on all piles.
	 */
	public void validatePiles() {
		for (int i = 0; i < NPILE; i++) {
			System.out.print("Pile "+i+":  ");
			System.out.print("Location:  ("+pile[i].getX()+","+
					pile[i].getY()+");  Length:  ");
			System.out.print(pile[i].size()+";  Status:  ");
			System.out.println("Valid.");
		}
		System.out.print("Moving pile:  ");
		System.out.print("Location:  ("+movingPile.getX()+","+
				movingPile.getY()+");  Length:  ");
		System.out.print(movingPile.size()+";  Status:  ");
		System.out.println("Valid.");
	}

	/**
	 *  Locates the pile clicked on, if any.
	 *
	 *  @param x,y  Coordinates of mouse click
	 *  @return  CardPile  holding clicked card
	 */
	private CardPile locatePile(int x, int y) {
		int index = y/100;
		if (index < 0) {
			index = 0;
		} else if (index>=NPILE) {
			index = NPILE-1;
		}
		return pile[index];
	}

	/**
	 *  Locates the card clicked on, if any.
	 *
	 *  @param x,y  Coordinates of mouse click
	 *  @return  Card  holding clicked card
	 */
	public Card locateCard(int x, int y) {
		return locatePile(x,y).locateCard(x,y);
	}	
	
	// Add listeners, etc. for stage 2 here
	/** Mouse event handlers */
	private class MouseMotionListener extends MouseAdapter {
		/** 
		 * Double click event handler to flip over the clicked card
		 * and all those following it in the pile
		 * 
		 * @param e MouseEvent of clicking mouse
		 */
		public void mouseClicked(MouseEvent e){
			cardUnderMouse = locateCard(e.getX(),e.getY());
			// Run code only when the user double clicked on a card, not in blank space
			if ((cardUnderMouse!=null)&&(e.getClickCount()==2)) {
				ListIterator<Card> position = locatePile(e.getX(),e.getY()).listIterator();
				// Move the iterator along to find the cardUnderMouse
				while (position.hasNext()&&(position.next()!=cardUnderMouse)) {
					// Nothing to do here
				}
				// Move the iterator to the place right before the cardUnderMouse
				position.previous(); 
				// Now flip every card including and after the cardUnderMouse
				while (position.hasNext()) {
					position.next().flipCard();
					repaint();
				}	
				cardUnderMouse = null; // Reset the default value
			}
		}
		
		/** 
		 * Press event handler to set up pileUnderMouse and cardUnderMouse for future drag event
		 * 
		 * @param e MouseEvent of pressing mouse
		 */
		public void mousePressed(MouseEvent e) {
			pileUnderMouse = locatePile(e.getX(), e.getY());
			cardUnderMouse = locateCard(e.getX(), e.getY());
		}

		/** 
		 * Drag event handler to drag every card including and after 
		 * cardUnderMouse away from the pileUnderMouse
		 * 
		 * @param e MouseEvent of dragging mouse
		 */
		public void mouseDragged(MouseEvent e) {
			// If the user starts a new drag sequence, set up the movingPile
			if (movingPile == null) {
				movingPile = pileUnderMouse.split(cardUnderMouse);
			} 
			// If the user keeps dragging, move the movingPile with the mouse
			else {
				movingPile.setX(e.getX());
				movingPile.setY(e.getY());
				repaint();
			}
		}
		
		/** 
		 * Release event handler to stop moving the movingPile,
		 * and add the movingPile after a specified card or at the end of the appropriate pile
		 * 
		 * @param e MouseEvent of releasing mouse
		 */
		public void mouseReleased(MouseEvent e) {
			// If the user was dragging with some cards, find where the mouse is released
			if (movingPile != null) {
				cardUnderMouse = locateCard(e.getX(), e.getY());
				pileUnderMouse = locatePile(e.getX(), e.getY());				
				// If mouse is released on a specified card, insert the movingPile after that card
				if (cardUnderMouse != null) {
					pileUnderMouse.insertAfter(movingPile, cardUnderMouse);
					repaint();
				}
				// If mouse is not released on any specified card, insert the movingPile at the end of the appropriate pile
				else {
					while (movingPile.size()>0) {
						pileUnderMouse.addLast(movingPile.removeFirst());
						repaint();
					}					
				}
				movingPile = null; // Reset the default value
			}
		}
	}
}  // end of CardTable
