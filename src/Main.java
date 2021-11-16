import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) {
		
		// Initialize Board constructor
		SwingUtilities.invokeLater(() -> new Board().getBoard());

	}

}