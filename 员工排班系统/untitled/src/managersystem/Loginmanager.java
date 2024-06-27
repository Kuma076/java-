package managersystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Loginmanager {

    public Loginmanager() {
        JFrame frame = new JFrame("员工管理");
        frame.setSize(500, 300);
        frame.setLocation(500, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 创建面板
        JPanel panel = new JPanel();
        placeComponents(panel);

        // 添加面板
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);
        Font font = new Font("宋体", Font.PLAIN, 20);

        JLabel userLabel = new JLabel("账号:", SwingConstants.CENTER);
        userLabel.setBounds(75, 28, 125, 52);
        panel.add(userLabel);

        JTextField userText = new JTextField(30);
        userText.setBounds(155, 36, 210, 35);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("密码:", SwingConstants.CENTER);
        passwordLabel.setBounds(75, 80, 125, 52);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(30);
        passwordText.setBounds(155, 88, 210, 35);
        panel.add(passwordText);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(185, 150, 125, 40);
        panel.add(loginButton);

        userText.setFont(font);
        passwordText.setFont(font);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (login(username, password)) {
                    // 登录成功的操作
                    JOptionPane.showMessageDialog(null, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    new Homepage();
                } else {
                    JOptionPane.showMessageDialog(null, "登录失败，用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    passwordText.setText("");
                }
            }
        });

    }

    private static boolean login(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM manager WHERE name = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // 如果结果集有记录，说明用户名和密码匹配
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        new Loginmanager();
    }
}