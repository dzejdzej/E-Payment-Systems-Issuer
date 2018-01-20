package issuer.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id; 


@Entity
public class CardHolder {

	@Id
	@GeneratedValue
	private int id;
	
	private String name; 
	
	public CardHolder() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
