package Controller;

import java.sql.BatchUpdateException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import Model.Answer;
import Model.Category;
import Model.Question;
/**
 * The Database controller class. Every communication with the database is accomplished 
 * through this controller.
 * @author Artur Cieœlak
 */
public class QuestionController {
	private static final String host = "127.0.0.1";
 	private static final String port = "1433";
 	private static final String database = "Questions";
 	private static final String user = "Artur";
 	private static final String password = "sa";

	public static final String DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DB_URL="jdbc:sqlserver://"+host+"\\SQLEXPRESS:"+port+";databaseName="+
            					database;
	
	private Connection conn=null;
	private PreparedStatement p_stat=null;
	private Statement stat=null;
	
	public QuestionController(){
		try{
			Class.forName(QuestionController.DRIVER);
		}catch(ClassNotFoundException e){
			e.printStackTrace();

		}
	}
	/**The method creates a connection to the database */
	private void connect(){
		try{
			conn=DriverManager.getConnection(DB_URL,user,password);
			stat=conn.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Gets list of all questions in the database. 
	 * @return A list of objects containing questions  
	 */
	public List<Question> selectQuestions(){
		connect();
		List<Question> questionList=new LinkedList<>();
		try{
			ResultSet result=stat.executeQuery("SELECT Question_ID,Question,Category_name "+
											   "FROM dbo.Categories AS C JOIN  dbo.Questions AS Q "+
											   "ON C.Category_ID=Q.Category_ID"
											   + " ORDER BY Category_name,Question;");
			int id;
			String question,cat;
			while(result.next()){
				id=result.getInt("Question_ID");
				question=result.getString("Question");
				cat=result.getString("Category_name");

				questionList.add(new Question(id,cat,question));
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			closeConnection();
		}
		return questionList;
	}
	/**
	 * Gets list of all questions in selected category.  
	 * @param category An object with category 
	 * @return A list of questions 
	 */
	public List<Question> selectQuestions(Category category){
		connect();
		List<Question> questionList=new LinkedList<>();
		try{
			ResultSet result=stat.executeQuery("SELECT Question_ID,Question,Category_name "+
											   "FROM dbo.Categories AS C JOIN  dbo.Questions AS Q "+
											   "ON C.Category_ID=Q.Category_ID "+
											   "WHERE C.Category_ID="+category.getId()+
											   " ORDER BY Category_name,Question;");
			int id;
			String question,cat;
			while(result.next()){
				id=result.getInt("Question_ID");
				question=result.getString("Question");
				cat=result.getString("Category_name");

				questionList.add(new Question(id,cat,question));
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			closeConnection();
		}
		return questionList;
	}
	/**
	 * Gets list of correct answers to the select question. 
	 * @param q Selected question
	 * @return A list of answers 
	 */
	public List<Answer> selectCorrectAns(Question q){
		connect();
		List<Answer> answerList=new LinkedList<>();
		try{
			ResultSet result=stat.executeQuery("SELECT TOP 3 * "+
												"FROM dbo.Correct_Answers "+
												"WHERE Question_ID="+q.getId()+";");
			int id;
			String answer;
			while(result.next()){
				id=result.getInt("Correct_Answer_ID");
				answer=result.getString("Correct_Answer");
				answerList.add(new Answer(id,q.getId(),answer));
			}
		}catch(SQLException e){
			e.printStackTrace();	
		}finally{
			closeConnection();
		}
		return answerList;
	}
	/**
	 * Gets list of incorrect answers to the select question. 
	 * @param q Selected question
	 * @return A list of answers 
	 */
	public List<Answer> selectIncorrectAns(Question q){
		connect();
		List<Answer> answerList=new LinkedList<>();
		try{
			ResultSet result=stat.executeQuery("SELECT TOP 3 * "+
												"FROM dbo.Incorrect_Answers "+
												"WHERE Question_ID="+q.getId()+";");
			int id;
			String answer;
			while(result.next()){
				id=result.getInt("Incorrect_Answer_ID");
				answer=result.getString("Incorrect_Answer");
				answerList.add(new Answer(id,q.getId(),answer));
			}
		}catch(SQLException e){
			e.printStackTrace();	
		}finally{
			closeConnection();
		}
		return answerList;
	}
	/**
	 * Gets list of all categories in the database. 
	 * @return A list of categories 
	 */
	public List<Category> selectCategories(){
		connect();
		List<Category> categoryList=new LinkedList<>();
		try{
			ResultSet result=stat.executeQuery("SELECT * "+
												"FROM dbo.Categories;");
			int id;
			String category;
			while(result.next()){
				id=result.getInt("Category_ID");
				category=result.getString("Category_name");
				categoryList.add(new Category(id,category));
			}
		}catch(SQLException e){
			e.printStackTrace();	
		}finally{
			closeConnection();
		}
		return categoryList;
	}
	/**
	 * Updates selected question. 
	 * @param q  Selected question
	 */
	public void updateQuestion(Question q){
		connect();
		String q_Text=q.getQuestion();
		int id=q.getId();
		try{
			String sql="UPDATE dbo.Questions "
					+"SET Question='"+q_Text+"' "
					+ "WHERE Question_ID="+id;
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Updates correct answers. 
	 * @param ans A list of answers 
	 */
	public void updateCorrectAnswers(List<Answer> ans){
		connect();
		try{
			conn.setAutoCommit(false);
			for(Answer answer:ans){
				String ans_Text=answer.getAnswer();
				int id=answer.getId();
				String sql="UPDATE dbo.Correct_Answers "
						+"SET Correct_Answer='"+ans_Text+"' "
						+ "WHERE Correct_Answer_ID="+id;
				stat.addBatch(sql);
			}
			//int[] updateCounts=stat.executeBatch();
			conn.commit();
		}catch(BatchUpdateException b){
			b.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();	
		}finally{
			closeConnection();
		}
	}
	/**
	 * Updates incorrect answers. 
	 * @param ans A list of answers 
	 */
	public void updateIncorrectAnswers(List<Answer> ans){
		connect();
		try{
			conn.setAutoCommit(false);
			for(Answer answer:ans){
				String ans_Text=answer.getAnswer();
				int id=answer.getId();
				String sql="UPDATE dbo.Incorrect_Answers "
						+"SET Incorrect_Answer='"+ans_Text+"' "
						+ "WHERE Incorrect_Answer_ID="+id;
				stat.addBatch(sql);
			}
			//int[] updateCounts=stat.executeBatch();
			conn.commit();
		}catch(BatchUpdateException b){
			b.printStackTrace();	
		}catch(SQLException e){
			e.printStackTrace();	
		}finally{
			closeConnection();
		}
	}
	/**
	 * Inserts a question to the database. 
	 * @param question Inserted question 
	 */
	public void insertQuestion(Question question){
		connect();
		try{
			String sql="INSERT INTO dbo.Questions VALUES(?,?);";
			p_stat=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			p_stat.setInt(1,question.getCategoryId());
			p_stat.setString(2, question.getQuestion());
			p_stat.executeUpdate();
			
			ResultSet generatedKeys=p_stat.getGeneratedKeys();
			if(generatedKeys.next()){
				question.setId(generatedKeys.getInt(1));
				System.out.println(question.getId());
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(p_stat!=null){
					p_stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection();
		}
	}
	/**
	 * Inserts correct answer to the database. 
	 * @param answer Inserted answer 
	 */
	public void insertCorrectAnswer(Answer answer){
		connect();
		try{
			String sql="INSERT INTO dbo.Correct_Answers VALUES(?,?);";
			p_stat=conn.prepareStatement(sql);
			p_stat.setInt(1,answer.getQuestionId());
			p_stat.setString(2, answer.getAnswer());
			p_stat.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(p_stat!=null){
					p_stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection();
		}
	}
	/**
	 * Inserts incorrect answer to the database. 
	 * @param answer Inserted answer 
	 */
	public void insertIncorrectAnswer(Answer answer){
		connect();
		PreparedStatement p_stat=null;
		try{
			String sql="INSERT INTO dbo.Incorrect_Answers VALUES(?,?);";
			p_stat=conn.prepareStatement(sql);
			p_stat.setInt(1,answer.getQuestionId());
			p_stat.setString(2, answer.getAnswer());
			p_stat.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(p_stat!=null){
					p_stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection();
		}
	}
	/**
	 * Deletes correct answer from the database.
	 * @param answer An answer to delete
	 */
	public void deleteCorrectAnswer(Answer answer){
		connect();
		int id=answer.getId();
		try{
			String sql="DELETE FROM dbo.Correct_Answers "+
						"WHERE Correct_Answer_ID="+id+";";
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Deletes incorrect answer from the database.
	 * @param answer An answer to delete
	 */
	public void deleteIncorrectAnswer(Answer answer){
		connect();
		int id=answer.getId();
		try{
			String sql="DELETE FROM dbo.Incorrect_Answers "+
						"WHERE Incorrect_Answer_ID="+id+";";
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Deletes a question from the database.
	 * @param question A question to delete
	 */
	public void deleteQuestion(Question question){
		connect();
		int id=question.getId();
		try{
			String sql="DELETE FROM dbo.Correct_Answers "+
					"WHERE Question_ID="+id+"; "+
					"DELETE FROM dbo.Incorrect_Answers "+
					"WHERE Question_ID="+id+"; "+
					"DELETE FROM dbo.Questions "+
					"WHERE Question_ID="+id+"; ";
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Inserts category to the database. 
	 * @param category Inserted category  
	 */
	public void insertCategory(Category category){
		connect();
		PreparedStatement p_stat=null;
		try{
			String sql="INSERT INTO dbo.Categories VALUES(?);";
			p_stat=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			p_stat.setString(1,category.getCategory());
			p_stat.executeUpdate();
			
			ResultSet generatedKeys=p_stat.getGeneratedKeys();
			if(generatedKeys.next()){
				category.setId(generatedKeys.getInt(1));
				System.out.println(category.getId());
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(p_stat!=null){
					p_stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection();
		}
	}
	/**
	 * Updates incorrect answers category. 
	 * @param category Updated category 
	 */
	public void updateCategory(Category category){
		connect();
		String text=category.getCategory();
		int id=category.getId();
		try{
			String sql="UPDATE dbo.Categories "
					+"SET Category_name='"+text+"' "
					+ "WHERE Category_ID="+id;
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Deletes a category from the database.
	 * @param category A category to delete
	 */
	public void deleteCategory(Category category){
		connect();
		int id=category.getId();
		try{
			String sql="DELETE FROM dbo.Categories "+
					"WHERE Category_ID="+id+"; ";
			stat.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	/**
	 * Gets list of random questions from selected category. 
	 * @param number Number of questions.
	 * @param category Category of questions
	 * @return A list of objects containing questions  
	 */
	public List<Question> selectRandomQuestions(int number,Category category){
		connect();
		List<Question> questionList=new LinkedList<>();
		int category_id=category.getId();
		try{
			String sql="SELECT TOP "+number+" * "+
						"FROM dbo.Questions WHERE Category_ID="+category_id+
						" ORDER BY NEWID();";
					   
			ResultSet result=stat.executeQuery(sql);
			int id;
			String question;
			while(result.next()){
				id=result.getInt("Question_ID");
				question=result.getString("Question");

				questionList.add(new Question(id,category_id,question));
			}
		}catch(SQLException e){
			System.err.println("Problem w pobraniu pytañ");
			e.printStackTrace();
			return null;
		}finally{
			closeConnection();
		}
		return questionList;
	}
	/**
	 * It close connection to the databse.
	 */
	public void closeConnection(){
		try {
			conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
