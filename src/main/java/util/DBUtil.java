package util;

import listener.DatabaseListener;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    private DBUtil() {
    }

    public static Connection getConnection(ServletContext context)
            throws SQLException {

        return DatabaseListener.getConnection(context);
    }
}