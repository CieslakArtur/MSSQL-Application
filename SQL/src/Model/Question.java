package Model;
/**
 * Question model. An instance of this class represents a single question in the database. 
 * @author Artur Cieœlak
 */
public class Question {
	/*ID of a single question in the array*/
	private int id;
	/*ID of a single question in the database*/
	private int category_id;
	private String category,question;
	
	public int getId(){
		return id;
	}
	public int getCategoryId(){
		return category_id;
	}
	public String getCategory(){
		return category;
	}
	public String getQuestion(){
		return question;
	}
	
	public void setId(int id){
		this.id=id;
	}
	public void setCatgoryId(int id){
		this.category_id=id;
	}
	public void setCategory(String category){
		this.category=category;
	}
	public void setQuestion(String question){
		this.question=question;
	}
	
	public Question(){};
	public Question(int id,int category_id,String question){
		this.id=id;
		this.category_id=category_id;
		this.question=question;
	}
	public Question(int id,String category,String question){
		this.id=id;
		this.category=category;
		this.question=question;
	}
	
	public String[] toArray(){
		String[] str={Integer.toString(id),question,category};
		return str;
	}
	
	@Override
	public String toString(){
		return question;
	}
}
