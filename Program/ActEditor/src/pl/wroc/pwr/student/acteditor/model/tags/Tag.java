package pl.wroc.pwr.student.acteditor.model.tags;

public interface Tag {
	public String[] getTags(String[] structure);
	public String getTagFromLine(String eachLine);
	public String whoAmI();
}