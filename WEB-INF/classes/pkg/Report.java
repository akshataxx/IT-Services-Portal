package pkg;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime; 
import java.util.*;

public class Report implements Serializable { 
	
	public ArrayList<Issue> getResolved(){
		ArrayList<Issue> IssueRecSet = new ArrayList<Issue>();
		try {
			String query = "SELECT * FROM [LAST7DAYS]";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userName);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Issue currentissue = new Issue();
				currentissue.setIssueId(result.getInt(1));
				currentissue.setIssueDescription(result.getString(2));
				currentissue.setCategoryName(result.getString(3));
				currentissue.setSubCategoryName(result.getString(4));
				currentissue.setITComment(result.getString(5));
				currentissue.setUserComment(result.getString(6));
				currentissue.setIssueStatus(result.getString(7));
				currentissue.setDateRaised(result.getTimestamp(8));
				currentissue.setDateResolved(result.getTimestamp(8));
				IssueRecSet.add(currentissue);
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