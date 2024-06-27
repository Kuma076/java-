package staffsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;



public class Staffschedule {

    public Staffschedule(Staff staff) {
        JFrame frame = new JFrame("员工排班详情");
        frame.setSize(650, 164);
        frame.setLocation(550, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(null);
        JLabel infoLabel = new JLabel("姓名: " + staff.getRealName() + "  电话号码: " + staff.getPhoneNumber());
        infoLabel.setBounds(50, 11, 600, 30);
        panel.add(infoLabel);
        JButton editInfoButton = new JButton("修改信息");
        editInfoButton.setBounds(480, 39, 90, 22);
        panel.add(editInfoButton);

        editInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changestaff(staff, infoLabel);
            }
        });
        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.setBounds(480, 14, 90, 23);
        panel.add(changePasswordButton);

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword(staff.getPhoneNumber());
            }
        });

        // 工作地点名称
        String facilityName = getFacilityName(staff.getId());
        JLabel facilityLabel = new JLabel("工作地点: " + facilityName);
        facilityLabel.setBounds(50, 36, 300, 30);
        panel.add(facilityLabel);

        // 显示排班信息
        Object[][] scheduleData = getScheduleData(staff.getId());
        String[] columnNames = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};

        JTable table = new JTable(scheduleData, columnNames);
        table.setRowHeight(40);
        table.setEnabled(false);
        table.setDefaultRenderer(Object.class, new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 65, 550, 100);
        panel.add(scrollPane);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private Object[][] getScheduleData(int staffId) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM schedule WHERE staff_id = ?";
        Object[][] data = new Object[1][7];

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 0; i < 7; i++) {
                        data[0][i] = rs.getInt(i + 1) == 1 ? "上班" : "休息";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String getFacilityName(int staffId) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT f.id, f.name FROM facility f JOIN schedule s ON f.id = s.facility_id WHERE s.staff_id = ?";
        String facilityName = "未安排";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int facilityId = rs.getInt("id");
                    if (facilityId != 1) {
                        facilityName = rs.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilityName;
    }

    private void changestaff(Staff staff, JLabel infoLabel) {
        JFrame editFrame = new JFrame("修改个人信息");
        editFrame.setSize(300, 200);
        editFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);

        JLabel nameLabel = new JLabel("姓名:");
        nameLabel.setBounds(50, 30, 80, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField(staff.getRealName());
        nameField.setBounds(130, 30, 120, 25);
        panel.add(nameField);

        JLabel phoneLabel = new JLabel("电话号码:");
        phoneLabel.setBounds(50, 70, 80, 25);
        panel.add(phoneLabel);

        JTextField phoneField = new JTextField(staff.getPhoneNumber());
        phoneField.setBounds(130, 70, 120, 25);
        panel.add(phoneField);

        JButton saveButton = new JButton("保存");
        saveButton.setBounds(100, 120, 80, 30);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = nameField.getText().trim();
                String newPhoneNumber = phoneField.getText().trim();

                if (newName.isEmpty() || newPhoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(editFrame, "姓名和电话号码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    updateStaffInfo(staff.getId(), newName, newPhoneNumber);
                    infoLabel.setText("姓名: " + newName + "  电话号码: " + newPhoneNumber);
                    editFrame.dispose();
                }
            }
        });

        editFrame.add(panel);
        editFrame.setVisible(true);
    }

    private void updateStaffInfo(int staffId, String newName, String newPhoneNumber) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "UPDATE staffdata SET realname = ?, phonenumber = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setString(2, newPhoneNumber);
            stmt.setInt(3, staffId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void changePassword(String currentPhoneNumber) {
        JFrame passwordFrame = new JFrame("修改密码");
        passwordFrame.setSize(300, 200);
        passwordFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);

        JLabel phoneLabel = new JLabel("电话号码:");
        phoneLabel.setBounds(50, 30, 80, 25);
        panel.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(130, 30, 120, 25);
        panel.add(phoneField);

        JLabel passwordLabel = new JLabel("新密码:");
        passwordLabel.setBounds(50, 70, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 70, 120, 25);
        panel.add(passwordField);

        JButton saveButton = new JButton("保存");
        saveButton.setBounds(100, 120, 80, 30);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredPhoneNumber = phoneField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();

                if (enteredPhoneNumber.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(passwordFrame, "电话号码和新密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (enteredPhoneNumber.equals(currentPhoneNumber)) {
                    updatePassword(currentPhoneNumber, newPassword);
                    passwordFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(passwordFrame, "电话号码不匹配", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        passwordFrame.add(panel);
        passwordFrame.setVisible(true);
    }

    private void updatePassword(String phoneNumber, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "UPDATE staffdata SET password = ? WHERE phonenumber = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, phoneNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 自定义单元格渲染器，用于设置排班状态的颜色
    static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if ("上班".equals(value)) {
                setForeground(Color.RED);
            } else {
                setForeground(Color.GREEN);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }
}
