package lib;

import java.sql.*;

public class sqltest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String password = "031012";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // 查询示例
            String selectSQL = "SELECT * FROM logindata WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, "password123");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("User ID: " + rs.getInt("id"));
                        System.out.println("Username: " + rs.getString("username"));
                    }
                }
            }

            // 插入示例
            String insertSQL = "INSERT INTO logindata (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "newuser");
                pstmt.setString(2, "newpassword");
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows inserted: " + rowsAffected);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}/*`PreparedStatement` 是 Java 数据库连接（JDBC）API 中的重要类，用于执行预编译的 SQL 语句。它继承自 `Statement` 类，但具有更高的效率和安全性。以下是 `PreparedStatement` 类中常用的方法及其详解：

        ### 1. `prepareStatement()`
        这是一个在 `Connection` 类中定义的方法，用于创建一个 `PreparedStatement` 对象。

        ```java
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        ```

        ### 2. `setString(int parameterIndex, String value)`
        将指定的参数（第 `parameterIndex` 个问号占位符）设置为字符串值。

        ```java
        pstmt.setString(1, "admin");
        pstmt.setString(2, "password123");
        ```

        ### 3. `setInt(int parameterIndex, int value)`
        将指定的参数（第 `parameterIndex` 个问号占位符）设置为整数值。

        ```java
        pstmt.setInt(1, 100);
        ```

        ### 4. `setDouble(int parameterIndex, double value)`
        将指定的参数（第 `parameterIndex` 个问号占位符）设置为双精度浮点值。

        ```java
        pstmt.setDouble(1, 99.99);
        ```

        ### 5. `setBoolean(int parameterIndex, boolean value)`
        将指定的参数（第 `parameterIndex` 个问号占位符）设置为布尔值。

        ```java
        pstmt.setBoolean(1, true);
        ```

        ### 6. `executeQuery()`
        用于执行 SQL 查询语句（`SELECT`）。它返回一个 `ResultSet` 对象，该对象包含查询结果。

        ```java
        ResultSet rs = pstmt.executeQuery();
        ```

        ### 7. `executeUpdate()`
        用于执行 SQL 更新语句（`INSERT`、`UPDATE` 或 `DELETE`）。它返回一个整数，表示受影响的行数。

        ```java
        int rowsAffected = pstmt.executeUpdate();
        ```

        ### 8. `execute()`
        用于执行任何 SQL 语句。返回一个布尔值，指示查询是否生成了 `ResultSet` 对象。

        ```java
        boolean hasResultSet = pstmt.execute();
        ```

        ### 9. `close()`
        关闭 `PreparedStatement` 对象并释放其资源。执行完 `PreparedStatement` 后应始终调用此方法。

        ```java
        pstmt.close();
        ```

        ### 10. `clearParameters()`
        清除当前参数设置。这对于重复使用 `PreparedStatement` 对象执行不同的查询很有用。

        ```java
        pstmt.clearParameters();
        ```

        ### 11. `setDate(int parameterIndex, Date value)`
        将指定的参数（第 `parameterIndex` 个问号占位符）设置为 `java.sql.Date` 类型。

        ```java
        pstmt.setDate(1, java.sql.Date.valueOf("2023-05-26"));
        ```

        ### 使用示例
        以下是一个完整的示例，展示如何使用 `PreparedStatement` 进行数据库查询和更新：

        ```java
        import java.sql.*;*/

/*

        ### 详解 `PreparedStatement` 方法

        1. **`prepareStatement()`**：
        - **作用**：创建一个 `PreparedStatement` 对象，包含预编译的 SQL 语句。
        - **优点**：预编译语句可以重复执行，提高效率；防止 SQL 注入攻击。

        2. **`setString(int parameterIndex, String value)`**：
        - **作用**：设置 SQL 语句中的字符串参数。
        - **优点**：防止 SQL 注入，自动处理转义字符。

        3. **`setInt(int parameterIndex, int value)`**：
        - **作用**：设置 SQL 语句中的整数参数。
        - **适用场景**：用于设置整数类型的 SQL 参数。

        4. **`executeQuery()`**：
        - **作用**：执行查询语句并返回 `ResultSet` 对象。
        - **适用场景**：用于执行 `SELECT` 语句。

        5. **`executeUpdate()`**：
        - **作用**：执行更新语句并返回受影响的行数。
        - **适用场景**：用于执行 `INSERT`、`UPDATE`、`DELETE` 语句。

        6. **`close()`**：
        - **作用**：关闭 `PreparedStatement` 对象，释放资源。
        - **重要性**：防止资源泄露，建议在完成操作后始终调用。

        通过使用 `PreparedStatement`，可以提高数据库操作的效率和安全性，尤其是在处理用户输入时。上述示例展示了如何使用 `PreparedStatement` 进行常见的数据库操作，包括查询、插入和资源管理。*/