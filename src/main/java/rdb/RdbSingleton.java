package rdb;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import static constancts.Constant.*;

public class RdbSingleton {
    private static Logger LOGGER = LogManager.getLogger(RdbSingleton.class);
    private static RdbSingleton instance;
    private String rdbDriverClassName;

    private String rdbUrl;

    private String userId;

    private String password;

    private boolean hasOpenResultSet;

    private RdbSingleton() {}

    public static synchronized RdbSingleton getInstance() {
       if (instance != null) {
           return instance;
       } else {
           instance = new RdbSingleton();
           return instance;
       }
    }

    public void setupDatabaseConnection(ServletConfig config) {
        try {
            this.rdbDriverClassName = config
                    .getInitParameter(rdbDriverClass.getValue());
            this.rdbUrl = config.getInitParameter(rdbURL.getValue());
            this.userId = config.getInitParameter(rdbUserId.getValue());
            this.password = config
                    .getInitParameter(rdbPassword.getValue());
        } catch (Throwable throwable) {
            LOGGER.fatal("Unable to setup the database", throwable);
            throw new IllegalStateException(
                    "Unable to setup the database connection", throwable);
        }
    }

    public Connection getConnection() {
        validateConfiguration();

        try {
            Class.forName(rdbDriverClassName);
            return DriverManager.getConnection(rdbUrl, userId, password);
        } catch (Throwable throwable) {
            LOGGER.error("Unable to obtain a connection to the database: " + rdbUrl,
                    throwable);
            throw new RuntimeException(
                    "Unable to obtain a connection to the database: " + rdbUrl, throwable);
        }
    }

    private void validateConfiguration() {
        StringBuffer errors = new StringBuffer();

        if (rdbDriverClassName == null) {
            errors.append("The RDB Driver Class Name has not been set. ");
        }

        if (rdbUrl == null) {
            errors.append("The RDB Url has not been set. ");
        }

        if (userId == null) {
            errors.append("The RDB User Id has not been set. ");
        }

        if (password == null) {
            errors.append("The RDB Password has not been set. ");
        }

        if (hasOpenResultSet) {
            errors.append("There is an active result set. "
                            + "Make sure to call the cleanUp(ResultSet) method "
                            + "to release the prior result set before running "
                            + "another query");
        }

        if (errors.length() > 0) {
            throw new IllegalStateException(errors.toString());
        }
    }

    public void cleanUp(final ResultSet resultSet) {
        Connection connection = null;
        Statement statement = null;

        try {
            statement = resultSet.getStatement();
            connection = statement.getConnection();
        } catch (Throwable throwable) {
            LOGGER.error("Failed to cleanup result set statement and connection",
                    throwable);
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
            hasOpenResultSet = false;
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Throwable throwable) {
                LOGGER.warn("Failed to close result set", throwable);
            }
        }
    }

    private void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable throwable) {
                LOGGER.warn("Unable to close the connection", throwable);
            }
        }
    }

    private void closeStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Throwable throwable) {
                LOGGER.warn("Unable to close the statement", throwable);
            }
        }
    }

    public void initializeDbData(String filePath) {
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from articles");
            if (!rs.next()) {
                String file = new String(Files.readAllBytes(Paths.get(filePath)));
                String[] articles = file.split("\n\n");
                for (String article : articles) {
                    if (!article.isEmpty()) {
                        String title = article.split("\n\t")[0];
                        String content = article.split("\n\t")[1];
                        String author = RandomStringUtils.random(10, true, false);
                    insertData(title, content,author);
                    }
                }
            }
            cleanUp(rs);
        } catch (Exception e) {
            System.out.println("Exception thrown initializing data: " + e);
        }
    }

    private void insertData(final String title, final String content, final String author) {
        String sql = "insert into articles (title, content, author)"
                + " values (?,?,?)";
        try {
            Connection conn = getConnection();
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setString(1, title);
            stmnt.setString(2, content);
            stmnt.setString(3, author);

            stmnt.execute();
        }
        catch (Throwable throwable) {
            LOGGER.error("Unable to create a statement", throwable);
        }
    }

    private void testReadDatabase() {
        try {
            System.out.println("Run a database query");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Forbes?allowPublicKeyRetrieval=true&useSSL=false", "ForbesApp", "Forbes");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                System.out.println("Title: " + rs.getString( "title") + "     Author: " + rs.getString("author"));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.error("Database problem", throwable);
        }
    }

    public static void main(String[] args) {
        RdbSingleton rdbSingleton = RdbSingleton.getInstance();
        rdbSingleton.testReadDatabase();
    }

}
