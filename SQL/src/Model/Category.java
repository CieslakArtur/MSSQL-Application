package Model;
/**
 * Category as mapped to 'Category' table.
 * @author Artur Cieœlak
 *
 */
public class Category {
	/*ID of a single category in the database*/
	private int id;
	private String category;
	
	public Category(){};
	public Category(String category){
		this.category=category;
	}
	public Category(int id,String category){
		this.id=id;
		this.category=category;
	}
	
	public int getId(){
		return id;
	}
	public String getCategory(){
		return category;
	}
	public void setId(int id){
		this.id=id;
	}
	public void setCategory(String category){
		this.category=category;
	}
	
	@Override
	public String toString(){
		return category;
	}
}
