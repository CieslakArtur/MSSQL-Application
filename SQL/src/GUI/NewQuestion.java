package GUI;

import java.awt.Choice;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import Controller.QuestionController;
import Model.Answer;
import Model.Category;
import Model.Question;
/**
 * This class is used to add new questions. 
 * @author Artur Cieœlak
 */
public class NewQuestion extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	/*Index of the first answer*/
	private final int FIRST=0;
	/*Index of the second answer*/
	private final int SECOND=1;
	/*Index of the third answer*/
	private final int THIRD=2;
	
	private JPanel editPane;
	private List<JTextArea> correct_Ans_JTA,incorrect_Ans_JTA;
	private List<Answer> correct_Ans,incorrect_Ans;
	private List<Category> categories;
	private List<JButton> delete_CorAns_Buttons,delete_IncAns_Buttons;
	private JButton save_Button,add_corAns_Button,add_incAns_Button,delete_Button;
	private JLabel label_ID,label_Category;
	private JTextArea q_text;
	private Choice category_list;
	
	private Question q;
	private QuestionController qController;
	private TitledBorder correctAns_Border,incorrectAns_Border;
	private Dimension dim;
	
	public NewQuestion(){
		q=new Question();
		qController=new QuestionController();
		correct_Ans=new LinkedList<>();
		incorrect_Ans=new LinkedList<>();
		categories=qController.selectCategories();
		
		EventQueue.invokeLater(()->{
			initUI();
		});
	}
	/**Initialize all components. */
	private void initUI(){
		
		save_Button=new JButton("Save");
		save_Button.addActionListener(this);
		
		add_corAns_Button=new JButton("Add correct answer");
		add_corAns_Button.addActionListener(this);
		add_incAns_Button=new JButton("Add incorrect answer");
		add_incAns_Button.addActionListener(this);
		
		delete_Button=new JButton("Delete");
		delete_Button.setToolTipText("Select answers to remove");
		delete_Button.addActionListener(this);
		
		delete_CorAns_Buttons=new LinkedList<>();
		for(int i=0;i<3;i++){
			JButton delete=new JButton("Delete");
			delete.setVisible(false);
			delete.addActionListener(this);
			delete_CorAns_Buttons.add(delete);
			}
		delete_IncAns_Buttons=new LinkedList<>();
		for(int i=0;i<3;i++){
			JButton delete=new JButton("Delete");
			delete.setVisible(false);
			delete.addActionListener(this);
			delete_IncAns_Buttons.add(delete);
			}

		label_ID=new JLabel(Integer.toString(q.getId()));
		label_ID.setBorder(new TitledBorder("ID number"));
		
		label_Category=new JLabel("Category");
		category_list=new Choice();
		for(Category cat:categories){
			category_list.add(cat.toString());
		}
		dim=new Dimension(200,100);
		
		q_text=new JTextArea(q.getQuestion());
		q_text.setEditable(true);
		q_text.setLineWrap(true);
		q_text.setBorder(new TitledBorder("Question"));
		q_text.setMinimumSize(dim);
		
		correctAns_Border=new TitledBorder("Correct Answer");
		incorrectAns_Border=new TitledBorder("Incorrect Answer");
		
		correct_Ans_JTA=new LinkedList<>();
		for(int i=0;i<3;i++){correct_Ans_JTA.add(new JTextArea());}
		
		for(JTextArea jta:correct_Ans_JTA){
			jta.setBorder(correctAns_Border);
			jta.setMinimumSize(dim);
			jta.setLineWrap(true);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
		
		incorrect_Ans_JTA=new LinkedList<>();
		for(int i=0;i<3;i++){incorrect_Ans_JTA.add(new JTextArea());}
		
		for(JTextArea jta:incorrect_Ans_JTA){
			jta.setBorder(incorrectAns_Border);
			jta.setMinimumSize(dim);
			jta.setLineWrap(true);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
	
		repaintView();
		createLayout();
		setTitle("New Question");
		setSize(600, 700);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	/**Creates layout with initialized components using 'Group Layout'.*/
	private void createLayout(){
		editPane=new JPanel();
		GroupLayout gl_editUI=new GroupLayout(editPane);
		editPane.setLayout(gl_editUI);
		JScrollPane scr=new JScrollPane(editPane);
		
		gl_editUI.setHorizontalGroup(gl_editUI.createParallelGroup()
							.addGroup(gl_editUI.createSequentialGroup()
														.addComponent(label_ID,75,75,75)
														.addGap(50)
														.addComponent(add_corAns_Button)
														.addComponent(add_incAns_Button)
														.addComponent(delete_Button)
														.addGap(10)
														.addComponent(save_Button)
														.addGap(10))
													.addGap(10)
													.addComponent(label_Category)
													.addComponent(category_list,500,500,600)
													.addGap(10)
													.addComponent(q_text)
							.addGroup(gl_editUI.createSequentialGroup()
									.addGroup(gl_editUI.createParallelGroup()
											.addComponent(correct_Ans_JTA.get(FIRST))
											.addComponent(delete_CorAns_Buttons.get(FIRST))
											.addComponent(correct_Ans_JTA.get(SECOND))
											.addComponent(delete_CorAns_Buttons.get(SECOND))
											.addComponent(correct_Ans_JTA.get(THIRD))
											.addComponent(delete_CorAns_Buttons.get(THIRD)))
									.addGroup(gl_editUI.createParallelGroup()
											.addComponent(incorrect_Ans_JTA.get(FIRST))
											.addComponent(delete_IncAns_Buttons.get(FIRST))
											.addComponent(incorrect_Ans_JTA.get(SECOND))
											.addComponent(delete_IncAns_Buttons.get(SECOND))
											.addComponent(incorrect_Ans_JTA.get(THIRD))
											.addComponent(delete_IncAns_Buttons.get(THIRD)))));
		
		gl_editUI.setVerticalGroup(gl_editUI.createSequentialGroup()
							.addGroup(gl_editUI.createParallelGroup(GroupLayout.Alignment.CENTER)
														.addComponent(label_ID)
														.addComponent(add_corAns_Button)
														.addComponent(add_incAns_Button)
														.addComponent(delete_Button)
														.addComponent(save_Button))	
													.addGap(10)
													.addComponent(label_Category)
													.addComponent(category_list)
													.addGap(10)
													.addComponent(q_text)
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(correct_Ans_JTA.get(FIRST))
														.addComponent(incorrect_Ans_JTA.get(FIRST)))
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(delete_CorAns_Buttons.get(FIRST))
														.addComponent(delete_IncAns_Buttons.get(FIRST)))
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(correct_Ans_JTA.get(SECOND))
														.addComponent(incorrect_Ans_JTA.get(SECOND)))
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(delete_CorAns_Buttons.get(SECOND))
														.addComponent(delete_IncAns_Buttons.get(SECOND)))
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(correct_Ans_JTA.get(THIRD))
														.addComponent(incorrect_Ans_JTA.get(THIRD)))
							.addGroup(gl_editUI.createParallelGroup()
														.addComponent(delete_CorAns_Buttons.get(THIRD))
														.addComponent(delete_IncAns_Buttons.get(THIRD))));
		add(scr);
		pack();
	}
	/**Creates new layout with new answers. */
	private void repaintView(){
		
		for(JTextArea jta:correct_Ans_JTA){
			jta.setText(null);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
		
		for(int i=0;i<correct_Ans.size();i++){
			correct_Ans_JTA.get(i).setText(correct_Ans.get(i).getAnswer());
			correct_Ans_JTA.get(i).setEditable(true);
			correct_Ans_JTA.get(i).setBackground(Color.WHITE);
			};
			
		for(JTextArea jta:incorrect_Ans_JTA){
			jta.setText(null);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
			
		for(int i=0;i<incorrect_Ans.size();i++){
			incorrect_Ans_JTA.get(i).setText(incorrect_Ans.get(i).getAnswer());
			incorrect_Ans_JTA.get(i).setEditable(true);
			incorrect_Ans_JTA.get(i).setBackground(Color.WHITE);
		};
		
		for(int i=0;i<3;i++){
			delete_CorAns_Buttons.get(i).setVisible(false);
			delete_IncAns_Buttons.get(i).setVisible(false);
		}
	}
	/**Checks text in fields and insert data to the database. */
	private void readData(){
		String text;
		Answer answer;
		/*Conditions for question. */
		String question=q_text.getText().trim();
		int category_id=categories.get(category_list.getSelectedIndex()).getId();
		if(question.length()==0){
			messageDialog("Please enter a question",JOptionPane.WARNING_MESSAGE);
			return;
		}else if(correct_Ans.size()==0){
			messageDialog("Insert at least one correct answer",JOptionPane.WARNING_MESSAGE);
			return;
		}else if(incorrect_Ans.size()==0){
			messageDialog("Insert at least one incorrect answer",JOptionPane.WARNING_MESSAGE);
			return;
		}else{
			q.setQuestion(question);
			q.setCatgoryId(category_id);

		}
		/*Conditions for correct answers. */
		for(int i=0;i<correct_Ans.size();i++){
			text=correct_Ans_JTA.get(i).getText();
			answer=correct_Ans.get(i);
			if(text.length()==0){
				messageDialog("Fill all the blank fields",JOptionPane.WARNING_MESSAGE);
				return;
			}else if(!checkText(text)){
				messageDialog("Incorrect characters: \\ ' ;", JOptionPane.WARNING_MESSAGE);
				return;
			}else if(correct_Ans.size()==0){
				messageDialog("Insert at least one correct answer", JOptionPane.WARNING_MESSAGE);
				return;
			}else{
				answer.setAnswer(text);
			}
		}
		/*Conditions for incorrect answers. */
		for(int i=0;i<incorrect_Ans.size();i++){
			text=incorrect_Ans_JTA.get(i).getText();
			answer=incorrect_Ans.get(i);
			if(text.length()==0){
				messageDialog("Fill all the blank fields",JOptionPane.WARNING_MESSAGE);
				return;
			}else if(!checkText(text)){
				messageDialog("Incorrect characters: \\ ' ;", JOptionPane.WARNING_MESSAGE);
				return;
			}else if(incorrect_Ans.size()==0){
				messageDialog("Insert at least one incorrect answer", JOptionPane.WARNING_MESSAGE);
				return;
			}else{
				answer.setAnswer(text);
			}
		}

		pushData();
		this.dispose();
	}
	/*Inserts data to the database. */
	private void pushData(){
		qController.insertQuestion(q);
		int q_id=q.getId();
		
		if(q_id!=0){
			for(Answer answer:correct_Ans){
				answer.setQuestionId(q_id);
				qController.insertCorrectAnswer(answer);
			}
			for(Answer answer:incorrect_Ans){
				answer.setQuestionId(q_id);
				qController.insertIncorrectAnswer(answer);
			}	
			messageDialog("Question successfully inserted!", JOptionPane.INFORMATION_MESSAGE);
		}else{
			messageDialog("Question cannot be inserted", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**Adds new correct answer. */
	private void addCorrectAnswer(){
		int size=correct_Ans.size();
		if(size<3){
			correct_Ans.add(new Answer());
			JTextArea jta=correct_Ans_JTA.get(size);
			jta.setText("New Correct Answer");
			jta.setEditable(true);
			jta.setBackground(Color.WHITE);
			//repaintView();
		}else{
			messageDialog("You can add only 3 correct answers",JOptionPane.WARNING_MESSAGE);
		}
	}
	/**Adds new incorrect answer. */
	private void addIncorrectAnswer(){
		int size=incorrect_Ans.size();
		if(size<3){
			incorrect_Ans.add(new Answer());
			JTextArea jta=incorrect_Ans_JTA.get(size);
			jta.setText("New Incorrect Answer");
			jta.setEditable(true);
			jta.setBackground(Color.WHITE);
			//repaintView();
		}else{
			messageDialog("You can add only 3 incorrect answers",JOptionPane.WARNING_MESSAGE);
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
	/**
	 * It deletes a correct answer from the database. 
	 * @param index Index of selected answer. 
	 */
	private void deleteCorrectAnswer(int index){
		readAnswers();
		correct_Ans.remove(index);
		repaintView();
	}
	/**
	 * It deletes a incorrect answer from the database. 
	 * @param index Index of selected answer. 
	 */
	private void deleteIncorrectAnswer(int index){
		readAnswers();
		incorrect_Ans.remove(index);
		repaintView();
	}
	/**Updates answers in JTextArea fields. */
	private void readAnswers(){
		for(int i=0;i<correct_Ans.size();i++){
			correct_Ans.get(i).setAnswer(correct_Ans_JTA.get(i).getText());
		}
		for(int i=0;i<incorrect_Ans.size();i++){
			incorrect_Ans.get(i).setAnswer(incorrect_Ans_JTA.get(i).getText());
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source=e.getSource();
		if(source==save_Button){
			readData();
		}else if(source==add_corAns_Button){
			addCorrectAnswer();
		}else if(source==add_incAns_Button){
			addIncorrectAnswer();
		}else if(source==delete_Button){
			for(int i=0;i<correct_Ans.size();i++){
				delete_CorAns_Buttons.get(i).setVisible(true);
			}
			for(int i=0;i<incorrect_Ans.size();i++){
				delete_IncAns_Buttons.get(i).setVisible(true);
			}
		}else if(source==delete_CorAns_Buttons.get(FIRST)){
			deleteCorrectAnswer(FIRST);
		}else if(source==delete_CorAns_Buttons.get(SECOND)){
			deleteCorrectAnswer(SECOND);
		}else if(source==delete_CorAns_Buttons.get(THIRD)){
			deleteCorrectAnswer(THIRD);
		}else if(source==delete_IncAns_Buttons.get(FIRST)){
			deleteIncorrectAnswer(FIRST);
		}else if(source==delete_IncAns_Buttons.get(SECOND)){
			deleteIncorrectAnswer(SECOND);
		}else if(source==delete_IncAns_Buttons.get(THIRD)){
			deleteIncorrectAnswer(THIRD);
		}
	}
}
