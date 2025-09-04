package database.query;

import java.sql.*;

public class Procedure {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ap";
        String user = "root";
        String password = "";


        try (Connection conn = DriverManager.getConnection(url, user, password);
             CallableStatement stmt = conn.prepareCall("CALL KiemTraHoaDon()")) { // <-- sửa đây

            boolean hasResultSet = stmt.execute();

            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData meta = rs.getMetaData();
                int columns = meta.getColumnCount();

                for (int i = 1; i <= columns; i++) {
                    System.out.print(meta.getColumnLabel(i) + "\t");
                }
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= columns; i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println();
                }
                rs.close();
            } else {
                System.out.println("Procedure không trả kết quả result set.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
