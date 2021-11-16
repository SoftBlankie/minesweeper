import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class Variables {
	
	//---------------- Board ---------------//
		
		JFrame frame = new JFrame("Minesweeper");
		protected final int WIDTH = 8;
		protected final int HEIGHT = WIDTH;
		protected int limit = HEIGHT - 2;
		protected int MINE = 10;
		
		// Creates the board
		protected Board board;
	    // Creates the tiles
		protected Tile[][] tiles = new Tile[HEIGHT][WIDTH];
		// Tile ID
		protected int tileID = 0;
		
		JPanel panel2;
		JLabel timeLabel;
    
	//---------------- Tile ----------------//
    
		// When checked, skips over revealing
	    protected boolean clicked;
	    
	    protected boolean firstClick;
	    
	    // When flagged, flagged
	    protected boolean flagged;
	    
	    // -1 = mine	0 = empty, integer
	    protected int tileState;
	    
	    // Able to click on tile and reveal
	    protected JButton button;
	    
	    // Number of mines close by
	    protected int minesNearby;
	    
	    // Id of tile
	    protected int id;
	    
	  //---------------- Timer ----------------//
	    
	    protected Timer timer;
	    protected int time;
	    
}
