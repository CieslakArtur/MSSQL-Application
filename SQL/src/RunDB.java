import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import GUI.MainMenu;

/**
 * The first method called. It contains the main() mthod.
 * @author Artur Cieœlak
 *
 */
public class RunDB {
	/*
	 * The main method. It creates instance of MainMenu class and set Windows native look. 
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		(new MainMenu()).setVisible(true);
	}
}
