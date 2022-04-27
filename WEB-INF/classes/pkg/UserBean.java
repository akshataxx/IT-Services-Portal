package pkg;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBean implements Serializable {

	private String fname;
	private String surname;
	private String email;
	private String username;
	private String password;
	private int contactno;
	private String role;
	private boolean status = false;
	
	public UserBean() {
	}

	public UserBean(String Fname, String Surname, String Email, String userName, String Password, int ContactNo, String Role) {
		this.fname = Fname;
		this.surname = Surname;
		this.email = Email;
		this.username = userName;
		this.password = Password;
		this.contactno = ContactNo;
		this.role = Role;
		this.status = false;
	}
	public void setStatus(boolean Status) {
		this.status = Status;
	}
	public void setFname(String f) {
		this.fname = f;
	}
	public void setSurname(String l) {
		this.surname = l;
	}
	public void setEmail(String e) {
		this.email = e;
	}
	public void setUserName(String u) {
		this.username = u;
	}
	public void setPassword(String p) {
		this.password = p;
	}	
	public void setContactNo(int c) {
		this.contactno = c;
	}
	public void setRole(String r) {
		this.role = r;
	}
	public boolean getStatus() {
		return status;
	}
	public String getFname() {
		return fname;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public String getUserName() {
		return username;
	}
	public String getPassword() {
		return password;
	}	
	public int getContactNo() {
		return contactno;
	}
	public String getRole() {
		return role;
	}
	public void login(String userName, String password) {
		try {
			String query = "SELECT * FROM userdtls Where [username]=? AND [passwrd]=? ";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userName);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				status = true;
				
				this.setFname(result.getString("fname"));
				this.setSurname(result.getString("surname"));
				this.setEmail(result.getString("email"));
				this.setContactNo(Integer.valueOf(result.getString("contactno")));
				this.setRole(result.getString("userrole"));
				System.out.println("From while loop" + userName + password + status+ result.getString("userrole"));//DEBUG CODE
			}

			result.close();
			statement.close();
			connection.close();
		}

		catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}