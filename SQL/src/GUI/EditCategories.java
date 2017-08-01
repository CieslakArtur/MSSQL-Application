package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import Controller.QuestionController;
import Model.Category;
/**
 * This class is used to add and remove categories. 
 * @author Artur Cieœlak
 *
 */
public class EditCategories extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private List<JButton> save_buttons,edit_buttons,delete_buttons;
	private List<Category> categories;
	private List<JTextArea> categories_JTA;
	private Database database;
	private JPanel editPane;
	private JToolBar toolbar;
	private JButton addCategory_btn;
	private QuestionController qController;
	private GroupLayout gl;
	private Group h_group,v_group;
	
	public EditCategories(Database database){
		this.database=database;
		qController=new QuestionController();
		edit_buttons=new LinkedList<>();
		save_buttons=new LinkedList<>();
		delete_buttons=new LinkedList<>();
		categories_JTA=new LinkedList<>();
		categories=qController.selectCategories();
		EventQueue.invokeLater(()->{
			initUI();
		});
		database.setEnabled(false);
	}
	
	@Override
	public void dispose(){
		database.updateCategories(); 
		database.setEnabled(true);
		super.dispose();
	}
	/**Initialize all components. */
	private void initUI(){
		
		for(int i=0;i<categories.size();i++){
			JButton edit=new JButton("Edit");
			edit.setMinimumSize(new Dimension(75,30));
			edit.addActionListener(this);
			edit_buttons.add(edit);
			
			JButton save=new JButton("Save");
			save.setMinimumSize(new Dimension(75,30));
			save.setVisible(false);
			save.addActionListener(this);
			save_buttons.add(save);
			
			JButton delete=new JButton("Delete");
			delete.setMinimumSize(new Dimension(75,30));
			delete.addActionListener(this);
			delete_buttons.add(delete);
			
			JTextArea jta=new JTextArea(categories.get(i).toString());
			jta.setBorder(new TitledBorder("Category "+Integer.toString(i+1)));
			jta.setBackground(getContentPane().getBackground());
			jta.setEditable(false);
			categories_JTA.add(jta);
		}

		toolbar=new JToolBar();
		toolbar.setFloatable(false);
		addCategory_btn=new JButton("New category");
		addCategory_btn.addActionListener(this);
		
		toolbar.add(addCategory_btn);
		
		createLayout();
		setTitle("Edit Categories");
		setSize(600,600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	/**It creates container and sets layout. */
	private void createLayout(){
		editPane=new JPanel();
		gl=new GroupLayout(editPane);
		editPane.setLayout(gl);
		JScrollPane scr=new JScrollPane(editPane);
		
		createGroupLayout();
		add(scr);
		pack();
	}
	/**It creates Group Layout. */
	private void createGroupLayout(){
		h_group=gl.createParallelGroup().addComponent(toolbar);
		
		for(int i=0;i<categories.size();i++){
			h_group.addGroup(gl.createSequentialGroup()
					.addComponent(categories_JTA.get(i))
					.addGroup(gl.createParallelGroup()
							.addComponent(edit_buttons.get(i))
							.addComponent(save_buttons.get(i))
							.addComponent(delete_buttons.get(i))));
		}
		
		v_group=gl.createSequentialGroup().addComponent(toolbar);
		
		for(int i=0;i<categories.size();i++){
			v_group.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(categories_JTA.get(i))
					.addGroup(gl.createSequentialGroup()
							.addComponent(edit_buttons.get(i))
							.addComponent(save_buttons.get(i))
							.addComponent(delete_buttons.get(i)))).addGap(10);
		}
		
		gl.setHorizontalGroup(h_group);
		gl.setVerticalGroup(v_group);
	}
	/**This method checks inserted categories and save them to the database. 
	 * @param indexOfCategory Index of selected category. 
	 */
	private void saveCategory(int indexOfCategory){
		String text=categories_JTA.get(indexOfCategory).getText().trim();
		Category category=categories.get(indexOfCategory);
		if(text.length()==0){
			messageDialog("Fill all the blank fields",JOptionPane.WARNING_MESSAGE);
			return;
		}else if(!checkText(text)){
			messageDialog("Incorrect characters: \\ ' ;", JOptionPane.WARNING_MESSAGE);
			return;
		}else if(!text.equals(category.getCategory())){
			category.setCategory(text);
			if(category.getId()==0){
				qController.insertCategory(category);
				messageDialog("Category inserted successfully", JOptionPane.INFORMATION_MESSAGE);
			}else{
				qController.updateCategory(category);
				messageDialog("Category updated successfully", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		categories_JTA.get(indexOfCategory).setEditable(false);
		categories_JTA.get(indexOfCategory).setBackground(getContentPane().getBackground());
		edit_buttons.get(indexOfCategory).setVisible(true);
		save_buttons.get(indexOfCategory).setVisible(false);
	}
	/**
	 * Deletes selected category.
	 * @param indexOfCategory Index of selected category.
	 */
	private void deleteCategory(int indexOfCategory){
		Category category=categories.get(indexOfCategory);
		int size=qController.selectQuestions(category).size();
		if(size>0){
			messageDialog("Remove all questions from category", JOptionPane.WARNING_MESSAGE);
			return;
		}else if(confirmDialog()){
			qController.deleteCategory(category);

			delete_buttons.get(indexOfCategory).setVisible(false);
			delete_buttons.remove(indexOfCategory);

			save_buttons.get(indexOfCategory).setVisible(false);
			save_buttons.remove(indexOfCategory);
			
			edit_buttons.get(indexOfCategory).setVisible(false);
			edit_buttons.remove(indexOfCategory);
			
			categories_JTA.get(indexOfCategory).setVisible(false);
			categories_JTA.remove(indexOfCategory);
			
			categories.remove(indexOfCategory);
			createGroupLayout();
			messageDialog("Category removed successfully", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**Adds category to the layout. */
	private void addCategory(){
		Category category=new Category("New category...");
		categories.add(category);
		
		JButton edit=new JButton("Edit");
		edit.setMinimumSize(new Dimension(75,30));
		edit.setVisible(false);
		edit.addActionListener(this);
		edit_buttons.add(edit);
		
		JButton save=new JButton("Save");
		save.setMinimumSize(new Dimension(75,30));
		save.addActionListener(this);
		save_buttons.add(save);
		
		JButton delete=new JButton("Delete");
		delete.setMinimumSize(new Dimension(75,30));
		delete.addActionListener(this);
		delete_buttons.add(delete);
		
		JTextArea jta=new JTextArea();
		jta.setBorder(new TitledBorder("Category "+categories.size()));
		jta.setBackground(Color.WHITE);
		jta.setEditable(true);
		categories_JTA.add(jta);
		
		createGroupLayout();
	}
	/**It creates a confirm dialog. 
	 * @return 'True' if the user confirms his choice. 
	 */
	private boolean confirmDialog(){
		int i=0;
		i=JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete category?",
										"Warning",JOptionPane.YES_NO_OPTION);
		if(i==JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * This method checks inserted text using regular expressions.
	 * @param text Text inserted by the user. 
	 * @return Returns true if text is correct. 
	 */
	private boolean checkText(String text){
		String regex = "[\\\\';]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if(m.find()){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * Creates a message dialog.
	 * @param text Text to display.  
	 * @param messageType Type of displayed message. 
	 */
	private void messageDialog(String text,int messageType){
		JOptionPane.showMessageDialog(editPane,text,"Message",messageType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source=e.getSource();
		for(int i=0;i<categories.size();i++){
			if(source==edit_buttons.get(i)){
				edit_buttons.get(i).setVisible(false);
				save_buttons.get(i).setVisible(true);
				categories_JTA.get(i).setEditable(true);
				categories_JTA.get(i).setBackground(Color.WHITE);
			}else if(source==save_buttons.get(i)){
				saveCategory(i);
			}else if(source==delete_buttons.get(i)){
				deleteCategory(i);
			}
		}
		if(source==addCategory_btn){
			addCategory();
		}

	}
}
