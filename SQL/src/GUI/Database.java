package GUI;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import Controller.QuestionController;
import Model.Answer;
import Model.Category;
import Model.Question;

/**
 * This class is used to manage the database by the user. The 'Database' class 
 * shows the results obtained with Question Controller.
 * @author Artur Cieœlak
 *
 */
public class Database extends JFrame implements ActionListener,ItemListener{
	private static final long serialVersionUID = 1L;
	//Reference to 'MainMenu' class
	private MainMenu mainMenu;
	private QuestionController qController;
	private JPanel pane;
	private JButton refresh_btn,checkAnswer_btn,removeQst_btn,return_btn,edit_btn,editCtg_btn,newQst_btn,cancel_btn;
	//Table used to show the results
	private JTable table;
	private TableModel model;
	private JCheckBox id_chk,question_chk,catgory_chk;
	private JToolBar toolbar;
	private Choice category_list;
	private List<Category> categories;
	private JDialog loading_dialog;
	private JProgressBar loading_prg;
	private SwingWorker<Void, Void> worker;
	private final Dimension btn_dim=new Dimension(150,25);
	private final Color btn_color=new Color(205,230,230);
	
	public Database(MainMenu mainMenu){
		this.mainMenu=mainMenu;
		qController=new QuestionController();
		EventQueue.invokeLater(()->{
			initUI();
		});
	}
	/**Initialize all components. */
	private void initUI(){
		refresh_btn=new JButton("Refresh");
		refresh_btn.setMaximumSize(btn_dim);
		refresh_btn.addActionListener(this);
		
		checkAnswer_btn=new JButton("Check Answers");
		checkAnswer_btn.setMaximumSize(btn_dim);
		checkAnswer_btn.addActionListener(this);
		
		removeQst_btn=new JButton("Remove Question");
		removeQst_btn.setMaximumSize(btn_dim);
		removeQst_btn.addActionListener(this);

		return_btn=new JButton("Menu");
		return_btn.setMaximumSize(new Dimension(75,25));
		return_btn.setBackground(btn_color);
		return_btn.addActionListener(this);
		
		edit_btn=new JButton("Edit Question");
		edit_btn.setMaximumSize(btn_dim);
		edit_btn.setBackground(btn_color);
		edit_btn.addActionListener(this);
		
		editCtg_btn=new JButton("Edit Categories");
		editCtg_btn.setMaximumSize(btn_dim);
		editCtg_btn.setBackground(btn_color);
		editCtg_btn.addActionListener(this);
		
		newQst_btn=new JButton("New Question");
		newQst_btn.setMaximumSize(btn_dim);
		newQst_btn.setBackground(btn_color);
		newQst_btn.addActionListener(this);
		
		table=new JTable(model);
		
		id_chk=new JCheckBox("ID");
		id_chk.setSelected(false);
		id_chk.addItemListener(this);
		
		question_chk=new JCheckBox("Question");
		question_chk.setSelected(true);
		question_chk.addItemListener(this);
		
		catgory_chk=new JCheckBox("Category");
		catgory_chk.setSelected(true);
		catgory_chk.addItemListener(this);
		
		toolbar=new JToolBar();
		toolbar.add(return_btn);
		toolbar.addSeparator();
		toolbar.add(newQst_btn);
		toolbar.add(edit_btn);
		toolbar.add(editCtg_btn);
		toolbar.setSize(new Dimension(600,25));
		toolbar.setFloatable(false);

		category_list=new Choice();
		
		cancel_btn=new JButton("Cancel");
		cancel_btn.addActionListener(this);
		
		createLoadingDialog();
		createTable();
		updateCategories();

	}
	/**Creates an empty table with column names. */
	private void createTable(){
		Vector<String> columns=new Vector<>();
		columns.add("ID");
		columns.add("Question");
		columns.add("Category");

		model=new TableModel(columns);
		
		table=new JTable(model);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		
		table.getTableHeader().setResizingAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);
		hideColumn(1);
	}
	/**Creates a simple progress bar. */
	private void createLoadingDialog(){
		loading_dialog=new JDialog();
		JPanel dialog_pane=new JPanel(new GridLayout(2,1));
		
		loading_prg=new JProgressBar();
		loading_prg.setString("Loading...");
		loading_prg.setStringPainted(true);
		loading_prg.setIndeterminate(true);
		dialog_pane.add(loading_prg);
		
		dialog_pane.add(cancel_btn);
		loading_dialog.add(dialog_pane);
		
		loading_dialog.setTitle("Progress");
		loading_dialog.setSize(300,100);
		loading_dialog.setVisible(false);
		loading_dialog.setLocationRelativeTo(null);
		loading_dialog.setAlwaysOnTop(true);
		loading_dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	/**It creates layout with initialized components using 'Group Layout'.*/
	private void createLayout(){
		pane=(JPanel) getContentPane();
		GroupLayout gl=new GroupLayout(pane);

		pane.setLayout(gl);
		pane.setToolTipText("Baza pytañ");
		pane.add(toolbar);
		JScrollPane scr=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
				,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.add(scr);

		gl.setHorizontalGroup(gl.createParallelGroup()
														.addComponent(toolbar)
								.addGroup(gl.createSequentialGroup()
										.addGroup(gl.createParallelGroup()
												.addComponent(scr)
												.addComponent(category_list))
										.addGroup(gl.createParallelGroup()
														.addComponent(refresh_btn)
														.addComponent(id_chk)
														.addComponent(question_chk)
														.addComponent(catgory_chk)
														.addComponent(checkAnswer_btn)
														.addComponent(removeQst_btn))));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
														.addComponent(toolbar)
								.addGroup(gl.createParallelGroup()
										.addGroup(gl.createSequentialGroup()
												.addComponent(scr)
												.addComponent(category_list))
								.addGroup(gl.createSequentialGroup()
														.addComponent(refresh_btn)
														.addComponent(id_chk)
														.addComponent(question_chk)
														.addComponent(catgory_chk)
														.addComponent(checkAnswer_btn)
														.addComponent(removeQst_btn))));	
		
		pack();
		setTitle("Database");
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**Simple method which creates messages with correct answers of selected quesion.
	 * @param q Selected question
	 */
	private void messageDialog(Question q){
		List<Answer> answers=new LinkedList<>();
		answers=qController.selectCorrectAns(q);
		if(true){
			JOptionPane.showMessageDialog(getContentPane(),answers.toArray(),"Answers",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**It creates confirm dialog shows before deleting a question. 
	 * @return 'True' if the user confirms his choice. 
	 */
	private boolean confirmDialog(){
		int i=0;
		i=JOptionPane.showConfirmDialog(getContentPane(), "This questions will be remove permanently.\n"+
				"Are you sure?","Warning",JOptionPane.YES_NO_OPTION);
		if(i==JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
	/**This method adds questions to the table model using 'SwingWorker'. */
	private void repaintView(){
		loading_dialog.setVisible(true);
		worker=new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				
				int index=category_list.getSelectedIndex();
				if(index==0){
					model.addList(qController.selectQuestions());
				}else{
					Category category=categories.get(index-1);
					model.addList(qController.selectQuestions(category));
				}
				return null;
			}
			@Override
			protected void done() {
				loading_dialog.setVisible(false);
			}
		};
		worker.execute();
	}
	/**It updates categorioes using 'SwingWorker'. */
	public void updateCategories(){
		loading_dialog.setVisible(true);
		category_list.removeAll();
		worker=new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				categories=qController.selectCategories();
				category_list.add("All categories");
				for(Category cat:categories){
					category_list.add(cat.toString());
				}
				return null;
			}
			@Override
			protected void done() {
				loading_dialog.setVisible(false);
				if(isCancelled()){
					Database.this.dispose();
					mainMenu.setVisible(true);
				}else if(categories==null){
					JOptionPane.showMessageDialog(getContentPane(), "Connection problem",
							"Error",JOptionPane.ERROR_MESSAGE);
					Database.this.dispose();
					mainMenu.setVisible(true);
				}else{
					createLayout();
				}
			}
		};
		worker.execute();
	}
	/**This method remove selected questions from the database. */
	private void removeQuestion(){
		int[] index=table.getSelectedRows();
		if(index.length!=0 && confirmDialog()){
			for(int i:index){
				qController.deleteQuestion(model.getQuestionAt(i));
			}
			repaintView();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==return_btn){
			this.dispose();
			mainMenu.setVisible(true);
		}else if(source==refresh_btn){
			repaintView();
		}else if(source==checkAnswer_btn){
			int index=table.getSelectedRow();
			if(index>=0){
				messageDialog(model.getQuestionAt(index));
			}	
		}else if(source==edit_btn){
			int index=table.getSelectedRow();
			if(index>=0){
				new EditDatabase(model.getQuestionAt(index));
			}
		}else if(source==removeQst_btn){
			removeQuestion();
		}else if(source==newQst_btn){
			if(categories.isEmpty()){
				JOptionPane.showMessageDialog(this, "Add categories","Warning",JOptionPane.WARNING_MESSAGE);
			}else{
				new NewQuestion();
			}
		}else if(source==editCtg_btn){
			EditCategories editCategories=new EditCategories(this);
			editCategories.setVisible(true);
		}else if(source==cancel_btn){
			worker.cancel(true);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e){
		Object source=e.getSource();
		
		
		if(e.getStateChange()==ItemEvent.DESELECTED){
			if(source==id_chk){hideColumn(1);
			}else if(source==question_chk){hideColumn(2);
			}else if(source==catgory_chk){hideColumn(3);}
		}else{
			if(source==id_chk){showColumn(1);
			}else if(source==question_chk){showColumn(2);
			}else if(source==catgory_chk){showColumn(3);}
			}		
		}
	/**It hide selected column. 
	 * @param column Index of selected column
	 */
	private void hideColumn(int column){
		table.getColumnModel().getColumn(column).setMinWidth(0);
		table.getColumnModel().getColumn(column).setMaxWidth(0);
		table.getColumnModel().getColumn(column).setPreferredWidth(0);
	}
	/**It show selected column. 
	 * @param column Index of selected column
	 */
	private void showColumn(int column){
		table.getColumnModel().getColumn(column).setMinWidth(200);
		table.getColumnModel().getColumn(column).setMaxWidth(500);
		table.getColumnModel().getColumn(column).setPreferredWidth(200);
	}
	/**An inner class extends AbstractTableModel. */
	public class TableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		private List<Question> list=new LinkedList<>();;
		Vector<String> colNames=new Vector<>();
		
		public TableModel(){};
		public TableModel(Vector<String> colNames){
			this.colNames.add("Nr");
			this.colNames.addAll(colNames);
		}
		public TableModel(List<Question> list,Vector<String> colNames){
			this.list=list;
			this.colNames.add("Nr");
			this.colNames.addAll(colNames);
		}
		
		@Override
		public int getColumnCount() {
			return colNames.size();
		}

		@Override
		public int getRowCount() {
			return list.size();
		}
		/**Adds new list with questions to table model. 
		 * @param list A list with new questions. 
		 */
		public void addList(List<Question> list){
			this.list=list;
			fireTableDataChanged();
		}
		/**Gets question at selected position. 
		 * @param index Index of selected question. 
		 * @return A single question. 
		 */
		public Question getQuestionAt(int index){
			return list.get(index);
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			Question question=list.get(row);
			switch(col){
			case 0:
				return row+1;
			case 1:
				return question.getId();
			case 2:
				return question.getQuestion();
			case 3:
				return question.getCategory();
			}
			return "";
		}
		@Override
		public String getColumnName(int col){
			return colNames.get(col);
		}
	}
}
