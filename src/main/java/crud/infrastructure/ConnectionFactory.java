package crud.infrastructure;

import crud.exception.DAOException;

import java.sql.*;
import java.util.logging.Logger;

public class ConnectionFactory{

    private static final String DATABASE = "Itg_challenge";
    private static final String STR_CON = "jdbc:mysql://localhost:3303/" + DATABASE;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    protected static Connection getConnection () throws DAOException {

		Connection connection = null;

		String message = "Connection with the data base has been completed successfully";

		try {

			connection = DriverManager.getConnection(STR_CON, USER, PASSWORD);
			connection.setAutoCommit(false);

			System.out.println(message);

			return connection;

		} catch (SQLException ex) {

			message = "Error while establishing connection";

			ex.printStackTrace();

			throw new DAOException(message, ex);

		} finally {
			if (connection != null) {
				try {
					connection.close();
					System.out.println("Connection closed.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

    public static void closeConnection ( Connection connection) throws DAOException {
		
		String message =  "Database connection closed successfully.";
		
		try {
			if (null != connection) {
				connection.close();
				System.out.println(message);

			}
		} catch (Exception ex) {
			message = "There was an error closing the database connection.";

			throw new DAOException(message, ex);

		}
	}
	
	public static void closeConnectionAndStatement(Connection connection, Statement statement) throws DAOException {
		
		String message =  "Database statement closed successfully.";
		
		if (connection != null) {

			closeConnection(connection);
		}
		try {
			if (statement != null) {

				statement.close();

			}
		} catch (Exception ex) {

			message = "There was an error closing the database statement.";

			throw new DAOException(message, ex);

		}
	}
	
	public static void closeAll(Connection connection, Statement statement, ResultSet resultset) throws DAOException {
		
		String message =  "Database result set closed successfully.";
		
		closeConnectionAndStatement(connection, statement);
		
		try {
			if ( resultset !=null) {
				resultset.close();
			}
		} catch (Exception ex) {
			message = "There was an error closing the database result set.";
			throw new DAOException(message, ex);
		}
	}
}