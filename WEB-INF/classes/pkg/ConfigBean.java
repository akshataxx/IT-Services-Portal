package pkg;

import javax.sql.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConfigBean {
        //Datastore object required to connect to the SQL database
    private static final DataSource dataSource = makeDataSource();
    
    //Creation of dataSource object for connecting SQL database using jdbc
    private static DataSource makeDataSource() {
        try{
            InitialContext ctx = new InitialContext();
            return (DataSource) ctx.lookup("java:/comp/env/jdbc/issuedata");
        } catch (NamingException e){
            throw new RuntimeException(e);
        }
    }
    
    //Returns a connection object for connecting to the SQL database
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}