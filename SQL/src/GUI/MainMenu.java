package GUI;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * A class to display Main Menu of the application. 
 * @author Artur Cieœlak
 *
 */
public class MainMenu extends JFrame{
	private static final long serialVersionUID = 1L;
	public MainMenu(){
		EventQueue.invokeLater(()->{
			initUI();
		});
	}
	
	/**Initialize all components and creates layout. */
	private void initUI(){
		JPanel menu=new JPanel();
		menu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		menu.setLayout(new GridLayout(3,1,5,5));

		
		JButton database_btn=new JButton("Database");
		database_btn.addActionListener((ActionEvent e)->{
			new Database(this);
			this.setVisible(false);
		});
		JButton test_btn=new JButton("Test");
		test_btn.addActionListener((ActionEvent e)->{
			new Test(this);
			this.setVisible(false);
		});
		JButton exit_btn=new JButton("Exit");
		exit_btn.addActionListener((ActionEvent e)->{
			System.exit(0);
		});
		
		menu.add(database_btn);
		menu.add(test_btn);
		menu.add(exit_btn);

		add(menu);
		pack();
		setTitle("Test");
		setSize(300,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
