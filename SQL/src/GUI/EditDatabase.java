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
import Model.Question;
/**
 * This class is used to edit questions. 
 * @author Artur Cieœlak
 *
 */
public class EditDatabase extends JFrame implements ActionListener{
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
	private List<JButton> delete_CorAns_Buttons,delete_IncAns_Buttons;
	private JButton save_btn,add_corAns_btn,add_incAns_btn,delete_btn;
	private JLabel label_ID,label_Category;
	private JTextArea q_text;
	
	private Question q;
	private QuestionController qController;
	private TitledBorder correctAns_Border,incorrectAns_Border;
	private Dimension dim;
	
	public EditDatabase(Question q){
		this.q=q;
		qController=new QuestionController();
		correct_Ans=qController.selectCorrectAns(q);
		incorrect_Ans=qController.selectIncorrectAns(q);
		
		EventQueue.invokeLater(()->{
			initUI();
		});
	}
	/**Initialize all components. */
	private void initUI(){
		
		save_btn=new JButton("Save");
		save_btn.addActionListener(this);
		
		add_corAns_btn=new JButton("Add correct answer");
		add_corAns_btn.addActionListener(this);
		add_incAns_btn=new JButton("Add incorrect answer");
		add_incAns_btn.addActionListener(this);
		
		delete_btn=new JButton("Delete");
		delete_btn.setToolTipText("Select answers to remove");
		delete_btn.addActionListener(this);
		
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
		
		label_Category=new JLabel(q.getCategory());
		label_Category.setBorder(new TitledBorder("Category"));
		
		dim=new Dimension(200,100);
		
		q_text=new JTextArea(q.getQuestion());
		q_text.setEditable(true);
		q_text.setLineWrap(true);
		q_text.setBorder(new TitledBorder("Question"));
		
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
		setTitle("Edit Question");
		setSize(600, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	/**It creates layout with initialized components using 'Group Layout'.*/
	private void createLayout(){
		editPane=new JPanel();
		GroupLayout gl_editUI=new GroupLayout(editPane);
		editPane.setLayout(gl_editUI);
		JScrollPane scr=new JScrollPane(editPane);
		
		gl_editUI.setHorizontalGroup(gl_editUI.createParallelGroup()
							.addGroup(gl_editUI.createSequentialGroup()
														.addComponent(label_ID,75,75,75)
														.addGap(50)
														.addComponent(add_corAns_btn)
														.addComponent(add_incAns_btn)
														.addComponent(delete_btn)
														.addGap(10)
														.addComponent(save_btn)
														.addGap(10))
													.addGap(10)
													.addComponent(label_Category,500,500,600)
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
														.addComponent(add_corAns_btn)
														.addComponent(add_incAns_btn)
														.addComponent(delete_btn)
														.addComponent(save_btn))	
													.addGap(10)
													.addComponent(label_Category)
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
	/**Creates new layout with updated answers. */
	private void repaintView(){
		
		for(JTextArea jta:correct_Ans_JTA){
			jta.setText(null);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
		
		for(int i=0;i<correct_Ans.size();i++){
			String answer=correct_Ans.get(i).toString();
			correct_Ans_JTA.get(i).setText(answer);
			correct_Ans_JTA.get(i).setEditable(true);
			correct_Ans_JTA.get(i).setBackground(Color.WHITE);
			};
			
		for(JTextArea jta:incorrect_Ans_JTA){
			jta.setText(null);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
		}
			
		for(int i=0;i<incorrect_Ans.size();i++){
			String answer=incorrect_Ans.get(i).toString();
			incorrect_Ans_JTA.get(i).setText(answer);
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
		String question=q_text.getText().trim();
		String text;
		Answer answer;
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
			}else{
				answer.setAnswer(text);
			}
			
			if(answer.getId()==0){
				qController.insertCorrectAnswer(answer);
			}
		}
		qController.updateCorrectAnswers(correct_Ans);
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
			}else{
				answer.setAnswer(text);
			}
			
			if(answer.getId()==0){
				qController.insertIncorrectAnswer(answer);
			}
		}
		/*Conditions for correct question. */
		if(question.length()==0){
			messageDialog("Please enter a question",JOptionPane.WARNING_MESSAGE);
			return;
		}else{
			q.setQuestion(question);
			qController.updateQuestion(q);
		}
		qController.updateIncorrectAnswers(incorrect_Ans);
		
		/*Update list of answers.*/
		correct_Ans=qController.selectCorrectAns(q);
		incorrect_Ans=qController.selectIncorrectAns(q);

		messageDialog("Question successfully updated!", JOptionPane.INFORMATION_MESSAGE);
	}
	/**Adds new correct answer. */
	private void addCorrectAnswer(){
		int size=correct_Ans.size();
		if(size<3){
			Answer new_answer=new Answer(q.getId(),"New answer...");
			correct_Ans.add(new_answer);
			repaintView();
		}else{
			messageDialog("You can add only 3 correct answers",JOptionPane.WARNING_MESSAGE);
		}
	}
	/**Adds new incorrect answer. */
	private void addIncorrectAnswer(){
		int size=incorrect_Ans.size();
		if(size<3){
			Answer new_answer=new Answer(q.getId(),"New answer...");
			incorrect_Ans.add(new_answer);
			repaintView();
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
	/**It creates a confirm dialog. 
	 * @return 'True' if the user confirms his choice. 
	 */
	private boolean confirmDialog(){
		int i=0;
		i=JOptionPane.showConfirmDialog(getContentPane(), "This answer will be remove permanently.\n"+
				"Are you sure?","Warning",JOptionPane.YES_NO_OPTION);
		if(i==JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * It deletes a correct answer from the database. 
	 * @param index Index of selected answer. 
	 */
	private void deleteCorrectAnswer(int index){
		Answer answer=correct_Ans.get(index);
		if(index<correct_Ans.size()){
			if(answer.getId()!=0 && confirmDialog()){
				qController.deleteCorrectAnswer(correct_Ans.get(index));
				correct_Ans.remove(index);
			}else if(answer.getId()==0){
				correct_Ans.remove(index);
			}
				repaintView();
		}
	}
	/**
	 * It deletes an incorrect answer from the database. 
	 * @param index Index of selected answer. 
	 */
	private void deleteIncorrectAnswer(int index){
		Answer answer=incorrect_Ans.get(index);
		if(index<incorrect_Ans.size()){
			if(answer.getId()!=0 && confirmDialog()){
				qController.deleteIncorrectAnswer(answer);
				incorrect_Ans.remove(index);
			}else if(answer.getId()==0){
				incorrect_Ans.remove(index);
			}
				repaintView();
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
		if(source==save_btn){
			readData();
		}else if(source==add_corAns_btn){
			addCorrectAnswer();
		}else if(source==add_incAns_btn){
			addIncorrectAnswer();
		}else if(source==delete_btn){
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
