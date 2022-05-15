package pkg;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime; 
import java.sql.*;
import java.util.*;

public class Report implements Serializable { 
	
	public ArrayList<Issue> getResolved(){
		ArrayList<Issue> IssueRecSet = new ArrayList<Issue>();
		try {
			String query = "SELECT * FROM [LAST7DAYS]";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Issue currentissue = new Issue();
				
				currentissue.setIssueId(result.getInt(1));
				currentissue.setIssueDescription(result.getString(2));
				currentissue.setCategoryName(result.getString(3));
				currentissue.setSubCategoryName(result.getString(4));
				currentissue.setDateRaised(result.getTimestamp(5).toLocalDateTime());
				currentissue.setITComment(result.getString(6));
				currentissue.setUserComment(result.getString(7));
				currentissue.setDateResolved(result.getTimestamp(8).toLocalDateTime());
				currentissue.setIssueStatus(result.getString(9));
			
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
		System.out.println("From Report.java" + IssueRecSet.size()); //DEBUG CODE
		return IssueRecSet;
	}
	
	public ArrayList<Issue> getUnResolved(String category){
		ArrayList<Issue> unResolvedRecSet = new ArrayList<Issue>();
		try {
			String query = "select * from [UNRESOLVEDINCIDENTS] WHERE [CategoryName]=?";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, category);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Issue currentissue = new Issue();
				
				currentissue.setIssueId(result.getInt(1));
				currentissue.setIssueDescription(result.getString(2));
				currentissue.setCategoryName(result.getString(3));
				currentissue.setSubCategoryName(result.getString(4));
				currentissue.setDateRaised(result.getTimestamp(5).toLocalDateTime());
				currentissue.setITComment(result.getString(6));
				currentissue.setUserComment(result.getString(7));
				currentissue.setDateResolved(result.getTimestamp(8).toLocalDateTime());
				currentissue.setIssueStatus(result.getString(9));
			
				unResolvedRecSet.add(currentissue);
			}
			result.close();
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return unResolvedRecSet;
			
	}

	public double getStressRate(){
		double stressRate=0.0;
		int unresolvedIssues = getNumOfUnresolvedIssues();
		int itStaff = getNumITStaff();
		stressRate = (unresolvedIssues/(itStaff*5));
		System.out.println("stressRate: " + stressRate);
		return stressRate;
	}
	private int getNumOfUnresolvedIssues(){
		int NumOfUnresolvedIssues=0;
		try {
			String query = "SELECT COUNT(*) FROM IssueDtls WHERE IssueStatus <> 'Resolved'";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			result.next();
			NumOfUnresolvedIssues=result.getInt(1);
			System.out.println( "NumOfUnresolvedIssues: " +NumOfUnresolvedIssues);
			result.close();
			statement.close();
			connection.close();
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return NumOfUnresolvedIssues;
	}
	
	private int getNumITStaff(){
		int numITStaff=0;
		try {
			String query = "SELECT COUNT(*) FROM ITSTAFF WHERE DateOfLeaving IS NULL";
			Connection connection = ConfigBean.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			result.next();
			numITStaff=result.getInt(1);
			System.out.println( "numITStaff: " +numITStaff);
			result.close();
			statement.close();
			connection.close();
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return numITStaff;
	}
}