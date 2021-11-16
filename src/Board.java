import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Board extends Variables{

	/*--------------------------------- CONSTRUCTOR -------------------------------------*/
	public JFrame getBoard() {
		// Sets up window
		frame.add(addLevelPanel(), BorderLayout.WEST);
		frame.add(addResetPanel(), BorderLayout.EAST);
		frame.add(addTime(), BorderLayout.CENTER);
		frame.add(addTiles(), BorderLayout.SOUTH);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		init();
		return frame;
	}
	
	private void init() {
		
		timer = new Timer(1000, null);
		timer.addActionListener(listener -> {this.updateTime();});
		timer.start();
		plantMines();
		minesNearby();
	}
	/*----------------------------------- METHODS ---------------------------------------*/
	public JPanel addTiles() {
		JPanel panel = new JPanel(new GridLayout(HEIGHT, WIDTH));
		
		// Creates each tile through an array that correlates to the Tile constructor
		for(int a = 0; a < HEIGHT; a++) {
        	for(int b = 0; b < WIDTH; b++) {
        		tiles[a][b] = new Tile(this);
        		tiles[a][b].setId(getID());
        		panel.add(tiles[a][b].getButton());
        	}
        }	
		
		return panel;
	}
	
	// Get tile
	public Tile getTile(int id) {
		for(Tile[] a : tiles) {
			for (Tile b : a) {
				if(b.getId() == id)
					return b;
			}
		}
		return null;
	}
	
	// Get ID
	public int getID() {
		return tileID++;
	}
	
	/*----------------------------------- MINES ----------------------------------------*/
	
	// Generate mines
	public void plantMines() {
		List<Integer> loc = generateMines(MINE);
		for(int a : loc) {
			getTile(a).setTileState(-1);
		}
	}
	
	// Generate random location for mines
	public ArrayList<Integer> generateMines(int a) {
		ArrayList<Integer> loc = new ArrayList<Integer>();
		SecureRandom rn = new SecureRandom();
		for(int b = 0; b < a;) {
			int random = rn.nextInt(HEIGHT * WIDTH);
			if(!loc.contains(random)) {
				loc.add(random);
			} else {
				while(loc.contains(random)) {
					random = rn.nextInt(HEIGHT * WIDTH);
				}
				loc.add(random);
			}
			b++;
		}
		
		return loc;
	}
	
	/*------------------------------- SCANNING TILES ------------------------------------*/
	
	// Mines nearby and set values of each tile
	public void minesNearby() {
		// Scans through all tiles
		for(int a = 0; a < HEIGHT; a++) {
			for(int b = 0; b < WIDTH; b++) {
				if(tiles[a][b].getTileState() != -1) {
					// Checks perimeter of tile
					if(b >= 1 && tiles[a][b - 1].getTileState() == -1)
						tiles[a][b].increment();
                    if(b <= limit && tiles[a][b + 1].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a >= 1 && tiles[a - 1][b].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a <= limit && tiles[a + 1][b].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a >= 1 && b >= 1 && tiles[a - 1][b-1].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a <= limit && b <= limit && tiles[a + 1][b + 1].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a >= 1 && b <= limit && tiles[a - 1][b + 1].getTileState() == -1)
                    	tiles[a][b].increment();
                    if(a <= limit && b >= 1 && tiles[a + 1][b - 1].getTileState() == -1)
                    	tiles[a][b].increment();
				}	
			}
		}
	}
	
	public IntStream sidesOf(int value) {
	    return IntStream.rangeClosed(value - 1, value + 1).filter(
	            x -> x >= 0 && x < HEIGHT);
	}
	
	public Set<Tile> getSurroundingCells(int x, int y) {
	    Set<Tile> result = new HashSet<>();
	    sidesOf(x).forEach(a -> {
	        sidesOf(y).forEach(b -> result.add(tiles[a][b]));
	    });
	    result.remove(tiles[x][y]);
	    return result;
	}
	
	// Scans cluster of empty tiles
	public void chainReaction() {
	    IntStream.range(0, HEIGHT).forEach(x -> {
	        IntStream.range(0, HEIGHT).forEach(y -> {
	            if (tiles[x][y].isClicked()) {
	                getSurroundingCells(x, y).stream().filter(Tile::isEmpty)
	                    .forEach(Tile::checkTile);
	                
	                getSurroundingCells(x, y).stream().filter(Tile::isNotEmpty)
                	.forEach(Tile::displayInt);
	            }
	        });
	    });
	}
	
    /*----------------------------------- GAME OVER --------------------------------------*/
        // Display all tiles
    public void gameOver() {
    	timer.stop();
    	
    	for(Tile[] a : tiles) {
    		for(Tile b : a) {
    			b.reveal();
    		}
    	}
    }
    
    // Reset Menu
	public JPanel addResetPanel() {
	    JPanel panel = new JPanel();
	    JButton reset = new JButton("New Game");
	    reset.setPreferredSize(new Dimension(100, 40));
	    reset.addActionListener(listener -> reset());
	    panel.add(reset);
	    
	    return panel;
	}
	
	// Difficulty Menu
	public JPanel addLevelPanel() {
		
		JPanel panel1 = new JPanel();
	    JComboBox<String> level = new JComboBox<String>();
	     
	    level.addItem("Beginner");
	    level.addItem("Expert");
	    
	    level.addActionListener(new ActionListener() {
        
	    	public void actionPerformed(ActionEvent event) {
                
                JComboBox level = (JComboBox) event.getSource();

                Object selected = level.getSelectedItem();
                if(selected.toString().equals("Beginner")) {
                	MINE = 10;
                	reset();
                } else if(selected.toString().equals("Expert")) {
                	MINE = 20;
                	reset();
                }
                
	    	}
        });
	    
	    panel1.add(level);
	    
	    return panel1;
	}
	
	// Time Display
	public JPanel addTime() {
		
		panel2 = new JPanel();
		timeLabel = new JLabel();
		
		panel2.add(timeLabel);
		
		return panel2;
	}
	
	public void updateTime() {
		
		time++;
		
		timeLabel.setText(Integer.toString(time));
	}
	
	// Reset all tiles
	public void reset() {
		
		for(int a = 0; a < WIDTH; a++)
        	for(int b = 0; b < WIDTH; b++)
        		tiles[a][b].reset();
        		
		timer.stop();
		time = -1;
		updateTime();
	    init();
	}
    
	
	
}