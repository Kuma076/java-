package managersystem;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Schedulecontrol {

    public void arrangeschedule(Staff staff) {
        JFrame infoFrame = new JFrame("排班信息 - " + staff.getRealName());
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(230, 372);
        infoFrame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 0); // 添加间距

        String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        Object[][] data = getScheduleData(staff);

        JCheckBox[] workCheckBoxes = new JCheckBox[7];
        JCheckBox[] restCheckBoxes = new JCheckBox[7];

        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 行 3 列，列之间间距为 10
            dayPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK)); // 添加底部黑线

            JLabel dayLabel = new JLabel(days[i]);
            dayPanel.add(dayLabel);

            workCheckBoxes[i] = new JCheckBox("上班", "上班".equals(data[i][0]));
            restCheckBoxes[i] = new JCheckBox("休息", "休息".equals(data[i][0]));

            // 默认选中
            workCheckBoxes[i].setSelected("上班".equals(data[i][0]));
            restCheckBoxes[i].setSelected("休息".equals(data[i][0]));

            // 添加勾选框的互斥逻辑
            final int index = i;
            workCheckBoxes[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (workCheckBoxes[index].isSelected()) {
                        restCheckBoxes[index].setSelected(false);
                    }
                }
            });

            restCheckBoxes[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (restCheckBoxes[index].isSelected()) {
                        workCheckBoxes[index].setSelected(false);
                    }
                }
            });

            dayPanel.add(workCheckBoxes[i]);
            dayPanel.add(restCheckBoxes[i]);

            gbc.gridy = i;
            panel.add(dayPanel, gbc);
        }

        // 获取员工当前设施
        int currentFacilityId = getCurrentFacilityId(staff);

        // 添加状态选择框
        JPanel facilityPanel = new JPanel();
        facilityPanel.setBackground(Color.WHITE);
        JLabel facilityLabel = new JLabel("地点：");
        JComboBox<String> facilityComboBox = new JComboBox<>();

        Map<String, Integer> facilityMap = getFacilityData();
        for (String facilityName : facilityMap.keySet()) {
            facilityComboBox.addItem(facilityName);
        }

        // 设置状态多选框的默认选项
        for (Map.Entry<String, Integer> entry : facilityMap.entrySet()) {
            if (entry.getValue() == currentFacilityId) {
                facilityComboBox.setSelectedItem(entry.getKey());
                break;
            }
        }

        facilityPanel.add(facilityLabel);
        facilityPanel.add(facilityComboBox);

        // 将状态选择框添加到mainPanel的顶部
        mainPanel.add(facilityPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedFacilityId = facilityMap.get(facilityComboBox.getSelectedItem().toString());

                // 更新 data 数组
                for (int i = 0; i < 7; i++) {
                    data[i][0] = workCheckBoxes[i].isSelected() ? "上班" : "休息";
                }

                updateSchedule(staff, selectedFacilityId, data);
                int option = JOptionPane.showConfirmDialog(infoFrame, "排班信息已更新", "提示", JOptionPane.DEFAULT_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    infoFrame.dispose();
                }
            }
        });

        // 创建一个新的JPanel来存放保存按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);

        // 将保存按钮的面板添加到mainPanel的底部
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        infoFrame.add(mainPanel);
        infoFrame.setVisible(true);
    }

    private Object[][] getScheduleData(Staff staff) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM schedule WHERE staff_id = ?";

        Object[][] data = new Object[7][1];

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, staff.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < 7; i++) {
                    data[i][0] = rs.getInt(i + 1) == 1 ? "上班" : "休息";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private Map<String, Integer> getFacilityData() {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT id, name FROM facility WHERE switch != 0";

        Map<String, Integer> facilityMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                facilityMap.put(name, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facilityMap;
    }

    private int getCurrentFacilityId(Staff staff) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT facility_id FROM schedule WHERE staff_id = ?";
        int facilityId = -1;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, staff.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                facilityId = rs.getInt("facility_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facilityId;
    }

    private void updateSchedule(Staff staff, int facilityId, Object[][] data) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "UPDATE schedule SET facility_id = ?, monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ?, sunday = ? WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, facilityId);

            for (int i = 0; i < 7; i++) {
                stmt.setInt(i + 2, "上班".equals(data[i][0]) ? 1 : 0);
            }

            stmt.setInt(9, staff.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
