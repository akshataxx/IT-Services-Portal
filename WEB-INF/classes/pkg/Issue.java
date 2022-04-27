package pkg;

public class Issue{
	//variable declaration
	private int IssueId;
	private String IssueDescription;
	private String CategoryName;
	private String SubcategoryName; 
	private String ITComment;
	private String UserComment;
	private String IssueStatus; 
	private LocalDateTime DateRaised = new LocalDateTime();
	private LocalDateTime DateResolved = new LocalDateTime();
	
	//Setter Function
	public void setIssueId(int issueid){
		IssueId = issueid;
	}
	public void setIssueDescription(String issuedesc){
		IssueDescription = issuedesc;
	}
	public void setCategoryName(String categoryname){
		CategoryName = categoryname;
	}
	public void setSubCategoryName(String subcategoryname){
		SubcategoryName = subcategoryname;
	}
	public void setITComment(String itcomment){
		ITComment = itcomment;
	}
	public void setUserComment(String usercomment){
		UserComment = usercomment;
	}
	public void setIssueStatus(String issuestatus){
		IssueStatus = issuestatus;
	}
	public void setDateRaised(LocalDateTime ldtraised){
		DateRaised = ldtraised;
	}
	public void getDateResolved(LocalDateTime ldtresolved){
		DateResolved=ldtresolved ;
	}
	
	//Getter Function
	public int getIssueId(){
		return IssueId;
	}
	public String getIssueDescription(){
		return IssueDescription;
	}
	public String getCategoryName(){
		return CategoryName;
	}
	public String getSubCategoryNaem(){
		return SubcategoryName;
	}
	public String getITComment(){
		return ITComment;
	}
	public String getUserComment(){
		return UserComment;
	}
	public String getIssueStatus(){
		return IssueStatus;
	}
	public LocalDateTime getDateRaised(){
		return DateRaised();
	}
	public LocalDateTime getDateResolved(){
		return DateResolved();
	}
}	