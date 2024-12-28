public class ConnectionFactory{

    private static final String STR_DRIVER = "";
    private static final String DATABASE = "";
    private static final String STR_CON = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    // private static Logger logger = Logger.getLogger(ConnectionFactory.class);

    protected static Connection getConnection () throws DAOException {

        Connection conn = null;

        String message = "Connection with the data base has been completed successfully";

        try {
            
            Class.forName(STR_DRIVER);
            connection = DriverManager.getConnection(STR_CON, USER, PASSWORD);
            connection.setAutoCommit(false);

            logger.info(message);

            return connection;

        } catch (ClassNotFoundException ex) {
            
            message = "Driver could not be found";
            logger.error(message, ex);

            throw new DAOException("Driver could not be found", ex);

        } catch(SQLException ex){

            message = "Error while establishing connection";
            logger.error(message, ex);

            throw new (DAOException(message, ex));

        }

    }

    public static void closeConnection(Connection connection) throws DAOException {
		
		String message =  "Database connection closed successfully.";
		
		try {
			if (null != connection) {
				connection.close();
				logger.info(message);

			}
		} catch (Exception ex) {
			message = "There was an error closing the database connection.";
			logger.error(message, ex);
			throw new DAOException(message, ex);

		}
	}
	
	public static void closeConnectionAndStatement(Connection connection, Statement statement) throws DAOException {
		
		String message =  "Database statement closed successfully.";
		
		if (null != connection) {

			closeConnection(connection);

		}
		try {
			if (null != statement) {

				statement.close();

			}
		} catch (Exception Exception) {

			message = "There was an error closing the database statement.";
			logger.error(message, ex);

			throw new DAOException(message, ex);

		}
	}
	
	public static void closeAll(Connection connection, Statement statement, ResultSet resultset) throws DAOException {
		
		String message =  "Database result set closed successfully.";
		
		closeConnectionAndStatement(connection, statement);
		
		try {
			if (null != resultset) {
				resultset.close();
			}
		} catch (Exception ex) {
			message = "There was an error closing the database result set.";
			logger.error(message, ex);
			throw new DAOException(message, ex);
		}
	}
}