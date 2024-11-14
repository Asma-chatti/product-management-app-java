package categorie;

public class Categories {
private int id;
private String name;
public Categories() {}
public Categories( String name) {
	super();
	this.name = name;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return this.name;
}
@Override
public String toString() {
	return this.name;
}
public void setName(String name) {
	this.name = name;
}
}
