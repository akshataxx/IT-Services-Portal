package model.application.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionFactory implements ConnectionFactory {

    /**
     *     <Resource name="jdbc/issuedata"
     *         auth="Container"
     *         type="javax.sql.DataSource"
     *         maxActive="100"
     *     	maxIdle="30"
     *     	maxWait="10000"
     *         username="jdbcUserseng2050"
     * 		password="mypassword"
     *         driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
     * 		url="jdbc:sqlserver://Localhost\SQLEXP:1433;databaseName=SENG2050"/>
     */


    /**
     *
     * This class is, so I can quickly test stuff on IDE and not swap between tomcat 50000 times
     */

    private final Connection connection;

    public TestConnectionFactory(String name, String username, String password) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        this.connection = DriverManager.getConnection("jdbc:sqlserver://" + name + ":1433" + ";user=" + username + ";password=" + password + ";"+"encrypt=true;trustServerCertificate=true;");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }
}
