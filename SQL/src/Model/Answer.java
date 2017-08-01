package Model;
/**
 * Answer model. An instance of this class represents a single answer in the database. 
 * @author Artur Cieœlak
 *
 */
public class Answer {
	/*ID of a single answer in the array*/
	private int id;
	/*ID of a single answer in the database*/
	private int question_id;
	private String answer;
	
	public Answer(){};
	public Answer(int id,int question_id,String answer){
		this.id=id;
		this.question_id=question_id;
		this.answer=answer;
	}
	public Answer(int question_id,String answer){
		this.question_id=question_id;
		this.answer=answer;
	}
	public Answer(String answer){
		this.answer=answer;
	}
	
	public int getId(){
		return id;
	}
	public int getQuestionId(){
		return question_id;
	}
	public String getAnswer(){
		return answer;
	}
	public void setQuestionId(int id){
		this.question_id=id;
	}
	public void setAnswer(String answer){
		this.answer=answer;
	}
	
	@Override
	public String toString(){
		return answer;
	}
}
