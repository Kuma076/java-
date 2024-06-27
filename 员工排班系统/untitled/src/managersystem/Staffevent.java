package managersystem;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
public class Staffevent {

    public void showStaffData() {
        JFrame staffFrame = new JFrame("员工排班");
        staffFrame.setSize(600, 400);
        staffFrame.setLocationRelativeTo(null);
        staffFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        JButton sortButton = new JButton("最新↓");
        JButton displayButton = new JButton("刷新");
        JButton freeStaffButton = new JButton("空闲员工");
        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(sortButton);
        searchPanel.add(displayButton);
        searchPanel.add(freeStaffButton);

        staffFrame.add(searchPanel, BorderLayout.NORTH);
        staffFrame.add(new JScrollPane(panel), BorderLayout.CENTER);

        List<Staff> staffList = getStaffData();
        displayStaffData(panel, staffList);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                List<Staff> filteredStaff = filterStaffData(staffList, searchText);
                displayStaffData(panel, filteredStaff);
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Staff> sortedStaff = sortStaffData(staffList);
                displayStaffData(panel, sortedStaff);
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Staff> updatedStaffList = getStaffData();
                displayStaffData(panel, updatedStaffList);
            }
        });

        freeStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Staff> freeStaffList = getFreeStaffData();
                displayStaffData(panel, freeStaffList);
            }
        });

        staffFrame.setVisible(true);
    }

    public void fireEmployee() {
        JFrame fireFrame = new JFrame("开除员工");
        fireFrame.setSize(444, 230);
        fireFrame.setLocationRelativeTo(null);
        fireFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");

        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        fireFrame.add(searchPanel, BorderLayout.NORTH);
        fireFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        List<Staff> staffList = getStaffData();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                if (searchField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入内容", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<Staff> filteredStaff = new ArrayList<>();
                for (Staff staff : staffList) {
                    if (staff.getRealName().contains(searchText)) {
                        filteredStaff.add(staff);
                    }
                }
                panel.removeAll();
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 0, 1, 0);
                gbc.anchor = GridBagConstraints.NORTH;

                int row = 0;
                for (Staff staff : filteredStaff) {
                    gbc.gridy = row++;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    gbc.gridwidth = GridBagConstraints.REMAINDER;

                    JPanel staffPanel = new JPanel(new BorderLayout());
                    staffPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
                    staffPanel.setBackground(Color.WHITE);
                    staffPanel.setPreferredSize(new Dimension(panel.getWidth(), 30));

                    JPanel infoPanel = new JPanel(new GridLayout(1, 3));
                    infoPanel.setBackground(Color.WHITE);

                    JLabel nameLabel = new JLabel("姓名: " + staff.getRealName());
                    JLabel phoneLabel = new JLabel("电话: " + staff.getPhoneNumber());
                    JLabel facilityLabel = new JLabel("状态: " + staff.getFacilityName());

                    infoPanel.add(nameLabel);
                    infoPanel.add(phoneLabel);
                    infoPanel.add(facilityLabel);

                    JButton fireButton = new JButton("解雇");
                    fireButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int confirm = JOptionPane.showConfirmDialog(fireFrame, "确定要开除员工 " + staff.getRealName() + " 吗？", "确认开除", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                    JOptionPane.showMessageDialog(fireFrame, "员工 " + staff.getRealName() + " 已被开除");
                                    fireStaff(staff.getRealName());
                                    staffList.remove(staff);
                                    panel.remove(staffPanel);
                                    panel.revalidate();
                                    panel.repaint();
                            }
                        }
                    });

                    staffPanel.add(infoPanel, BorderLayout.CENTER);
                    staffPanel.add(fireButton, BorderLayout.EAST);

                    panel.add(staffPanel, gbc);
                }

                gbc.gridy = row;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                panel.add(Box.createGlue(), gbc);

                panel.revalidate();
                panel.repaint();
            }
        });

        fireFrame.setVisible(true);
    }

    private List<Staff> filterStaffData(List<Staff> staffList, String searchText) {
        List<Staff> filteredStaff = new ArrayList<>();
        for (Staff staff : staffList) {
            if (staff.getRealName().contains(searchText)) {
                filteredStaff.add(staff);
            }
        }
        return filteredStaff;
    }

    private List<Staff> sortStaffData(List<Staff> staffList) {
        List<Staff> sortedStaff = new ArrayList<>(staffList);
        sortedStaff.sort(Comparator.comparingInt(Staff::getId).reversed()); // 按照 id 从大到小排序
        return sortedStaff;
    }
    private List<Staff> getFreeStaffData() {
        List<Staff> freeStaffList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "SELECT s.id, s.realname, s.phonenumber, COALESCE(f.name, '空闲') AS facility_name " +
                "FROM staffdata s " +
                "LEFT JOIN schedule sch ON s.id = sch.staff_id " +
                "LEFT JOIN facility f ON sch.facility_id = f.id " +
                "WHERE sch.facility_id = 1";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String realName = rs.getString("realname");
                String phoneNumber = rs.getString("phonenumber");
                String facilityName = rs.getString("facility_name");
                freeStaffList.add(new Staff(id, realName, phoneNumber, facilityName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return freeStaffList;
    }

    private void displayStaffData(JPanel panel, List<Staff> staffList) {
        panel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 1, 0); // 间隔1
        gbc.anchor = GridBagConstraints.NORTH; // 顶格显示
        int row = 0;
        for (Staff staff : staffList) {
            gbc.gridy = row++;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JPanel staffPanel = new JPanel(new BorderLayout());
            staffPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK)); // 添加底部黑线
            staffPanel.setBackground(Color.WHITE);
            staffPanel.setPreferredSize(new Dimension(panel.getWidth(), 30)); // 固定高度30

            JPanel infoPanel = new JPanel(new GridLayout(1, 3));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel("姓名: " + staff.getRealName());
            JLabel phoneLabel = new JLabel("电话: " + staff.getPhoneNumber());
            JLabel facilityLabel = new JLabel("状态: 在" + staff.getFacilityName());

            infoPanel.add(nameLabel);
            infoPanel.add(phoneLabel);
            infoPanel.add(facilityLabel);

            Schedulecontrol schedulecontrol = new Schedulecontrol();
            JButton scheduleButton = new JButton("排班");
            scheduleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    schedulecontrol.arrangeschedule(staff); // 传递 staff 对象
                }
            });

            staffPanel.add(infoPanel, BorderLayout.CENTER);
            staffPanel.add(scheduleButton, BorderLayout.EAST);

            panel.add(staffPanel, gbc);
        }
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);
        panel.revalidate();
        panel.repaint();
    }

    private List<Staff> getStaffData() {
        List<Staff> staffList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "SELECT s.id, s.realname, s.phonenumber, COALESCE(f.name, '1') AS facility_name " +
                "FROM staffdata s " +
                "LEFT JOIN schedule sch ON s.id = sch.staff_id " +
                "LEFT JOIN facility f ON sch.facility_id = f.id";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String realName = rs.getString("realname");
                String phoneNumber = rs.getString("phonenumber");
                String facilityName = rs.getString("facility_name");
                staffList.add(new Staff(id, realName, phoneNumber, facilityName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffList;
    }

    private boolean fireStaff(String name) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM staffdata WHERE realname = ?")) {
            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
