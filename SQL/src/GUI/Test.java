package GUI;

import java.awt.CardLayout;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import Controller.QuestionController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import Model.Answer;
import Model.Category;
import Model.Question;

/**
 * Class used to create tests from a selected category. 
 * @author Artur Cieœlak
 */
public class Test extends JFrame{
	private static final long serialVersionUID = 1L;
	/*Number of questions.*/
	private int counter=1;
	private List<Question> questions;
	private List<TestQuestion> test_questions;
	private QuestionController qController;
	private Choice category_list,number_list;
	private JButton start_btn,back_btn,next_btn,prev_btn,end_btn,menu_btn,cancel_btn;
	private List<Category> categories;
	private List<JCheckBox> check_list;
	private JPanel cards,testPanel;
	private CardLayout card_layout;
	private JLabel score_label;
	private MainMenu mainMenu;
	private JProgressBar test_prg,loading_prg;
	private int score,current_time,max_time;
	private Timer timer;
	private SwingWorker<Void, Void> worker;
	private JDialog loading_dialog,setting_dialog;
	
	public Test(MainMenu mainMenu){
		this.mainMenu=mainMenu;
		qController=new QuestionController();
		test_questions=new LinkedList<>();
		check_list=new LinkedList<>();
		EventQueue.invokeLater(()->{
			initUI();
		});
	}
	/**Initialize all components. */
	private void initUI(){
		start_btn=new JButton("Start");
		start_btn.addActionListener((ActionEvent e)->{
			if(categories.isEmpty()){
				JOptionPane.showMessageDialog(setting_dialog,"Add categories","Warning",JOptionPane.WARNING_MESSAGE);
			}else{
				setting_dialog.setVisible(false);
				loadQuestions();
			}
		});
		
		next_btn=new JButton("Next");
		next_btn.addActionListener((ActionEvent e)->{
			card_layout.next(cards);
		});
		prev_btn=new JButton("Previous");
		prev_btn.addActionListener((ActionEvent e)->{
			card_layout.previous(cards);
		});
		
		end_btn=new JButton("Finish Test");
		end_btn.addActionListener((e)->{
			timer.stop();
			checkAnswers();
		});
		
		menu_btn=new JButton("Main Menu");
		menu_btn.setVisible(false);
		menu_btn.addActionListener((e)->{
			this.dispose();
			mainMenu.setVisible(true);
		});
		
		back_btn=new JButton("Back");
		back_btn.addActionListener((e)->{
			mainMenu.setVisible(true);
			this.dispose();
		});
		
		cancel_btn=new JButton("Cancel");
		cancel_btn.addActionListener((e)->{
			worker.cancel(true);
			this.dispose();
			mainMenu.setVisible(true);
		});
		
		score_label=new JLabel();
		score_label.setVisible(false);
		
		number_list=new Choice();
		number_list.add("10");
		number_list.add("20");
		number_list.add("30");
		category_list=new Choice();

		createLoadingDialog();
		/*Simple worker which loading categories in setting menu. */
		worker=new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				loading_dialog.setVisible(true);
				categories=qController.selectCategories();
				for(Category c:categories){
					category_list.add(c.toString());
				}
				return null;
			}
			@Override
			protected void done() {
				loading_dialog.setVisible(false);
				if(isCancelled()){
					mainMenu.setVisible(true);
					Test.this.dispose();
				}else if(categories==null){
					JOptionPane.showMessageDialog(getContentPane(), "Connection problem",
							"Error",JOptionPane.ERROR_MESSAGE);
					Test.this.dispose();
					mainMenu.setVisible(true);
				}else{
					createSettingLayout();
				}
			}
		};
		worker.execute();
	}
	/**Creates simple progress bar. */
	private void createLoadingDialog(){
		loading_dialog=new JDialog();
		JPanel pane=new JPanel(new GridLayout(2,1));
		
		loading_prg=new JProgressBar();
		loading_prg.setString("Loading...");
		loading_prg.setStringPainted(true);
		loading_prg.setIndeterminate(true);
		pane.add(loading_prg);
		
		pane.add(cancel_btn);
		loading_dialog.add(pane);
		
		loading_dialog.setTitle("Progress");
		loading_dialog.setSize(300,100);
		loading_dialog.setVisible(false);
		loading_dialog.setLocationRelativeTo(null);
		loading_dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	/**Creates layout with test settings. */
	private void createSettingLayout(){
		setting_dialog=new JDialog(this, "Test settings");

		JPanel pane=new JPanel(new GridLayout(6,1));
		pane.add(new JLabel("Category"));
		pane.add(category_list);
		pane.add(new JLabel("Number of questions"));
		pane.add(number_list);
		pane.add(start_btn);
		pane.add(back_btn);
		
		setting_dialog.add(pane);
		setting_dialog.setVisible(true);
		setting_dialog.setSize(300,300);
		setting_dialog.setLocationRelativeTo(null);
		setting_dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	/**Generate random questions from selected category and sets time. */
	private void loadQuestions(){
		loading_dialog.setVisible(true);
		worker=new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				Category category=categories.get(category_list.getSelectedIndex());
				int number=Integer.parseInt(number_list.getSelectedItem());
				questions=qController.selectRandomQuestions(number, category);
				max_time=number*15000;
				return null;
			}
			@Override
			protected void done() {
				if(questions.size()==0){
					loading_dialog.setVisible(false);
					JOptionPane.showMessageDialog(setting_dialog,"Add questions to selected category or\n"+
							"change category", "Warning",JOptionPane.WARNING_MESSAGE);
					setting_dialog.setVisible(true);
				}else{
					setting_dialog.dispose();
					createCardLayout();
					startTime();
					loading_dialog.dispose();
				}
			}
		};
		worker.execute();
	}
	/**Creates layout with initialized components using 'Group Layout'.*/
	private void createCardLayout(){
		
		testPanel=new JPanel();
		GroupLayout gl=new GroupLayout(testPanel);
		testPanel.setLayout(gl);
		
		card_layout=new CardLayout();
		cards=new JPanel(card_layout);
		/*Create new instatnces of 'TestQuestion' class. */
		for(Question q:questions){
			test_questions.add(new TestQuestion(q));
		}
		
		test_prg=new JProgressBar();
		
		gl.setHorizontalGroup(gl.createParallelGroup()
								.addComponent(test_prg)
								.addComponent(cards)
								.addGroup(gl.createSequentialGroup()
										.addComponent(prev_btn)
										.addComponent(next_btn)
										.addComponent(end_btn)
										.addComponent(menu_btn)
										.addGap(20)
										.addComponent(score_label)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
								.addComponent(test_prg)
								.addComponent(cards)
								.addGroup(gl.createParallelGroup()
										.addComponent(prev_btn)
										.addComponent(next_btn)
										.addComponent(end_btn)
										.addComponent(menu_btn)
										.addGap(20)
										.addComponent(score_label)));
		add(testPanel);
		pack();
		setTitle("Test");
		setSize(600, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	/**Starts the timer and sets current time in the progress bar. */
	private void startTime(){
		SimpleDateFormat df=new SimpleDateFormat("mm:ss");
		current_time=0;
		
		test_prg.setValue(0);
		test_prg.setMaximum(max_time);
		test_prg.setString("");
		test_prg.setStringPainted(true);
		test_prg.setString(df.format(max_time));
		
		ActionListener task=new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				current_time+=timer.getDelay();
				
				test_prg.setValue(current_time);
				test_prg.setString(df.format(max_time-current_time));
				
				if(current_time>=max_time){
					timer.stop();
					checkAnswers();
					}
			}
		};
		
		timer=new Timer(1000,task);
		timer.start();
	}
	/**Checks the answers and display the score. */
	private void checkAnswers(){
		
		score=0;
		for(TestQuestion q:test_questions){
			score+=q.getScore();
		}
		
		int size=questions.size();
		double percent=(double)(score*100)/size;
		DecimalFormat df=new DecimalFormat("##.#");
		score_label.setText("Your result:  "+score+"/"+size+" ("+df.format(percent)+"%)");
		score_label.setVisible(true);
		
		end_btn.setVisible(false);
		menu_btn.setVisible(true);
	}
	/**
	 * An inner class represents a question as a single card of a Card Layout. 
	 * @author Artur Cieœlak
	 *
	 */
	private class TestQuestion{
		private int id;
		private JPanel card;
		private JScrollPane scr_pane;
		private JTextArea jta;
		private Question question;
		private List<Answer> correct_Ans,incorrect_Ans;
		private List<JCheckBox> check_CorAns,check_IncAns;
		private List<JTextArea> Answers_JTA;
		private final Dimension dim=new Dimension(50,10);
		
		public TestQuestion(Question question){
			this.question=question;
			id=counter++;
			correct_Ans=qController.selectCorrectAns(question);
			incorrect_Ans=qController.selectIncorrectAns(question);
			check_CorAns=new LinkedList<>();
			check_IncAns=new LinkedList<>();
			Answers_JTA=new LinkedList<>();
			EventQueue.invokeLater(()->{
				createCard();
			});
		}
		/**Creates a single card with question and answers. */
		private void createCard(){
			card=new JPanel();
			GroupLayout gl=new GroupLayout(card);
			card.setLayout(gl);
			scr_pane=new JScrollPane(card,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			jta=new JTextArea(question.toString());
			jta.setMinimumSize(dim);
			jta.setEditable(false);
			jta.setBackground(getContentPane().getBackground());
			jta.setBorder(new TitledBorder("Question "+id));
			/*Set new horizontal and vertical group of a Group Layout. */
			Group horizontal_gl=gl.createParallelGroup().addComponent(jta);
			Group vertical_gl=gl.createSequentialGroup().addComponent(jta);
			/*Initialize new JCheckBoxes and JTextAreas with correct answers. */
			for(Answer a:correct_Ans){
				JTextArea jta=new JTextArea(a.toString());
				jta.setEditable(false);
				jta.setBackground(card.getBackground());
				jta.setLineWrap(true);
				jta.setMinimumSize(dim);
				Answers_JTA.add(jta);
				check_CorAns.add(new JCheckBox());
			}
			/*Initialize new JCheckBoxes and JTextAreas with incorrect answers. */
			for(Answer a:incorrect_Ans){
				JTextArea jta=new JTextArea(a.toString());
				jta.setEditable(false);
				jta.setBackground(card.getBackground());
				jta.setLineWrap(true);
				jta.setMinimumSize(dim);
				Answers_JTA.add(jta);
				check_IncAns.add(new JCheckBox());
			}
			/*Clear and add all checkboxes to a new checklist*/
			check_list.clear();
			check_list.addAll(check_CorAns);
			check_list.addAll(check_IncAns);
			
			Random r=new Random();		
			/*Drawing answers.*/
			while(!check_list.isEmpty()){
				int index=r.nextInt(check_list.size());
				System.out.println("index="+index);
				/*Adds components to horizontal and vertical groups. */
				horizontal_gl.addGroup(gl.createSequentialGroup()
										.addComponent(check_list.get(index))
										.addComponent(Answers_JTA.get(index)));
				vertical_gl.addGroup(gl.createParallelGroup()
									.addComponent(check_list.get(index))
									.addComponent(Answers_JTA.get(index)));
				check_list.remove(index);
				Answers_JTA.remove(index);
			}
			
			gl.setHorizontalGroup(horizontal_gl);
			gl.setVerticalGroup(vertical_gl);
			/*Adds created card to 'cards' container. */
			cards.add(scr_pane);
		}
		/**Check user answers and get score. 
		 * @return 1 if all the answers are correct.
		 */
		private int getScore(){
			boolean b=true;
			JCheckBox chk=new JCheckBox();
			/*'False' if at least one correct answer is not selected. */
			for(int i=0;i<check_CorAns.size();i++){
				chk=check_CorAns.get(i);
				chk.setEnabled(false);
				if(chk.isSelected()){
					chk.setBackground(Color.GREEN);
				}else{
					chk.setBackground(Color.RED);
					b=false;
				}
			}
			/*'False' if at least one incorrect answer is selected. */
			for(int i=0;i<check_IncAns.size();i++){
				chk=check_IncAns.get(i);
				chk.setEnabled(false);
				if(check_IncAns.get(i).isSelected()){
					chk.setBackground(Color.RED);
					b=false;
				}else{
					chk.setBackground(Color.GREEN);
				}
			}
			
			if(b){
				return 1;
			}else{
				return 0;
			}
		}
	}
}
