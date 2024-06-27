package staffsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Loginstaff {
    public Loginstaff() {
        JFrame frame = new JFrame("员工排班表");
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
        loginButton.setBounds(100, 150, 100, 30);
        panel.add(loginButton);

        JButton zcButton = new JButton("注册");
        zcButton.setBounds(280, 150, 100, 30);
        panel.add(zcButton);

        userText.setFont(font);
        passwordText.setFont(font);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入内容", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Staff staff = login(username, password);
                if (staff != null) {
                    // 登录成功的操作
                    JOptionPane.showMessageDialog(null, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    new Staffschedule(staff);
                } else {
                    JOptionPane.showMessageDialog(null, "登录失败，用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    passwordText.setText("");
                }
            }
        });

        zcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }

    private static Staff login(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT s.id, s.realname, s.phonenumber, f.name as facilityName " +
                "FROM staffdata s LEFT JOIN facility f ON s.id = f.id " +
                "WHERE s.name = ? AND s.password = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(rs.getInt("id"), rs.getString("realname"), rs.getString("phonenumber"), rs.getString("facilityName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void showRegisterDialog() {
        JDialog registerDialog = new JDialog((Frame) null, "注册新用户", true);
        registerDialog.setSize(420, 375);
        registerDialog.setLocationRelativeTo(null);
        registerDialog.setLayout(null);

        JLabel userLabel = new JLabel("账号:");
        userLabel.setBounds(75, 28, 125, 25);
        registerDialog.add(userLabel);

        JTextField userText = new JTextField(30);
        userText.setBounds(155, 28, 210, 25);
        registerDialog.add(userText);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(75, 68, 125, 25);
        registerDialog.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(30);
        passwordText.setBounds(155, 68, 210, 25);
        registerDialog.add(passwordText);

        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setBounds(75, 108, 125, 25);
        registerDialog.add(confirmPasswordLabel);

        JPasswordField confirmPasswordText = new JPasswordField(30);
        confirmPasswordText.setBounds(155, 108, 210, 25);
        registerDialog.add(confirmPasswordText);

        JLabel nameLabel = new JLabel("姓名:");
        nameLabel.setBounds(75, 148, 125, 25);
        registerDialog.add(nameLabel);

        JTextField nameText = new JTextField(30);
        nameText.setBounds(155, 148, 210, 25);
        registerDialog.add(nameText);

        JLabel phoneLabel = new JLabel("电话号码:");
        phoneLabel.setBounds(75, 188, 125, 25);
        registerDialog.add(phoneLabel);

        JTextField phoneText = new JTextField(30);
        phoneText.setBounds(155, 188, 210, 25);
        registerDialog.add(phoneText);

        JButton registerButton = new JButton("注册");
        registerButton.setBounds(155, 230, 100, 30);
        registerDialog.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                String confirmPassword = new String(confirmPasswordText.getPassword());
                String name = nameText.getText();
                String phoneNumber = phoneText.getText();

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "所有字段都不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (registerUser(username, password, name, phoneNumber)) {
                    JOptionPane.showMessageDialog(null, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败，用户名已被注册。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerDialog.setVisible(true);
    }

    private static boolean registerUser(String username, String password, String name, String phoneNumber) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // 检查用户名是否存在
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM staffdata WHERE name = ?")) {
                checkStmt.setString(1, username);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // 用户名已存在
                        return false;
                    }
                }
            }

            // 插入新用户
            try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO staffdata (name, password, realname, phonenumber) VALUES (?, ?, ?, ?)")) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, name);
                insertStmt.setString(4, phoneNumber);

                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new Loginstaff();
    }
}
