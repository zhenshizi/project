package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (is != null) {
                prop.load(is);
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
                driver = prop.getProperty("db.driver");
                Class.forName(driver);
            } else {
                throw new RuntimeException("db.properties not found in classpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
