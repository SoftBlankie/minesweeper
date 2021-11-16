import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.JButton;

public class Tile extends Variables implements MouseListener {
	
    /* ----------------------------------- CONSTRUCTOR ---------------------------------------------*/
    public Tile(Board board) {
    	// Set up for each tile
    	button = new JButton();
    	button.addActionListener(listener -> {this.checkTile();});
    	button.addMouseListener(this);
    	button.setPreferredSize(new Dimension(50,50));
    	this.board = board;
    	clicked = false;
    }
    /* ------------------------------------- METHODS -----------------------------------------------*/
    
        // Get button
        public JButton getButton() {
        	return button;
        }
  
        // Set tileState
        public void setTileState(int tileState) {
        	this.tileState = tileState;
        }
        
        // Get tileState
        public int getTileState() {
            return tileState;
        }
        
        // If tile isEmpty
        public boolean isEmpty() {
        	return !isClicked() && tileState == 0;
        }
        
        // If tile isNotEmpty
        public boolean isNotEmpty() {
        	return tileState != 0 && tileState != -1;
        }
        
        // Get checked
        public boolean isClicked() {
            return clicked;
        }
        
        // Set Id
        public void setId(int id) {
        	this.id = id;
        }
        
        // Get Id (Id of each tile)
        public int getId() {
        	return id;
        }
        
        // Next tileState
        public void increment() {
        	tileState++;
        }
       
        // Reveals tile
        public void reveal() {
        	displayInt();
        	button.setEnabled(false);
        }
        
        /* ---------------------------- CHECKING AND DISPLAYING --------------------------------------*/
        
		// Checks if tile is empty, a mine, or flagged
        public void checkTile() {
        	
        	// Disallows tile to be revealed
            button.setEnabled(false);
        	displayInt();
            // Doesn't run over the tile again
            clicked = true;
            
            if (tileState == 0) {
                board.chainReaction();
            } else if (tileState == -1) {
            	//Reveal all tiles
            	board.gameOver();
            }
        }
        
        // Display minesNearby or mine
        public void displayInt() {
        	if (tileState == -1) {
                button.setText("\u2600");
                button.setBackground(Color.RED);
            } else if (tileState != 0) {
                // Use getInt and insert minesNearby in empty tile
            	button.setEnabled(false);
            	button.setText(String.valueOf(tileState));
            } 
        		
        }
  
        // Flags tile with "|>" when right clicked
        @Override
        public void mouseClicked(MouseEvent e){
        	if(e.getButton() == 1) {
        		if(firstClick == true) {
        			timer.start();
        			firstClick = false;
        		}
        		
        		if(!button.isEnabled()) {
	        		if(tileState > 0) {
	        			displayInt();
	        		}
	        		else if(tileState == 0) {
	        			button.setText("");
	        		}
        		}
        		
        	}
        	
        	if (e.getButton() == 3 && button.isEnabled()) {
        		if(!flagged) {
        			button.setText("|>");
        		} else {
        			button.setText("");
        		}
        		
        		flagged = !flagged;
        	}
        }
        
        /* ------------------------------------ GAME OVER --------------------------------------------*/       
       
        // Reset all tile variables
        public void reset() {
            setTileState(0);
            button.setText("");
            button.setBackground(null);
            clicked = false;
            flagged = false;
            button.setEnabled(!clicked);
        }

		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
}