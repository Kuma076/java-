package managersystem;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Facilityevent {

    public void showFacilitiesData() {
        JFrame facilityFrame = new JFrame("全部设施");
        facilityFrame.setSize(500, 400);
        facilityFrame.setLocationRelativeTo(null);
        facilityFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        facilityFrame.add(new JScrollPane(panel), BorderLayout.CENTER);

        List<Facility> facilityList = getFacilitiesData();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 1, 0);
        gbc.anchor = GridBagConstraints.NORTH;

        int row = 0;
        for (Facility facility : facilityList) {
            gbc.gridy = row++;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JPanel facilityPanel = new JPanel(new BorderLayout());
            facilityPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            facilityPanel.setBackground(Color.WHITE);
            facilityPanel.setPreferredSize(new Dimension(panel.getWidth(), 30));

            JPanel infoPanel = new JPanel(new GridLayout(1, 2));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel("设施: " + facility.getName());
            JLabel statusLabel = new JLabel();

            if (facility.getSwitch() == 0) {
                statusLabel.setText("已关闭");
                statusLabel.setForeground(Color.GRAY);
            } else {
                if (facility.getStaffCount() == 0) {
                    statusLabel.setText("无人值守");
                    statusLabel.setForeground(Color.RED);
                } else {
                    statusLabel.setText("员工数量: " + facility.getStaffCount());
                }
            }

            infoPanel.add(nameLabel);
            infoPanel.add(statusLabel);

            facilityPanel.add(infoPanel, BorderLayout.CENTER);

            panel.add(facilityPanel, gbc);
        }

        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        panel.revalidate();
        panel.repaint();

        facilityFrame.setVisible(true);
    }

    public void facilitykannri() {
        JFrame manageFrame = new JFrame("管理设施");
        manageFrame.setSize(300, 420);
        manageFrame.setLocationRelativeTo(null);
        manageFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        manageFrame.add(new JScrollPane(panel), BorderLayout.CENTER);

        List<Facility> facilityList = getFacilitiesData();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 1, 0);
        gbc.anchor = GridBagConstraints.NORTH;

        int row = 0;
        for (Facility facility : facilityList) {
            gbc.gridy = row++;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JPanel facilityPanel = new JPanel(new BorderLayout());
            facilityPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            facilityPanel.setBackground(Color.WHITE);
            facilityPanel.setPreferredSize(new Dimension(panel.getWidth(), 30));

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(facility.getName());
            nameLabel.setHorizontalAlignment(JLabel.LEFT);

            String[] options = {"关闭", "开放"};
            JComboBox<String> switchBox = new JComboBox<>(options);
            switchBox.setSelectedIndex(facility.getSwitch() == 0 ? 0 : 1);
            switchBox.setPreferredSize(new Dimension(100, 25)); // 设置多选框的宽度和高度
            switchBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedIndex = switchBox.getSelectedIndex();
                    updateFacilitySwitch(facility.getId(), selectedIndex);
                }
            });

            infoPanel.add(nameLabel, BorderLayout.CENTER);
            infoPanel.add(switchBox, BorderLayout.EAST);

            facilityPanel.add(infoPanel, BorderLayout.CENTER);

            panel.add(facilityPanel, gbc);
        }

        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFacilityStatus();
                JOptionPane.showMessageDialog(manageFrame, "设施状态已保存", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);

        manageFrame.add(buttonPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();

        manageFrame.setVisible(true);
    }

    private void saveFacilityStatus() {
        List<Facility> closedFacilities = getClosedFacilities();
        for (Facility facility : closedFacilities) {
            List<Integer> staffIds = getStaffIdsByFacilityId(facility.getId());
            for (int staffId : staffIds) {
                updateStaffFacilityId(staffId, 1);
            }
        }
    }

    private List<Facility> getFacilitiesData() {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";
        String query = "SELECT f.id, f.name, f.switch, COUNT(s.staff_id) AS staff_count " +
                "FROM facility f LEFT JOIN schedule s ON f.id = s.facility_id " +
                "WHERE f.id <> 1 " +
                "GROUP BY f.id, f.name, f.switch";

        List<Facility> facilityList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int facilitySwitch = rs.getInt("switch");
                int staffCount = rs.getInt("staff_count");

                Facility facility = new Facility(id, name, facilitySwitch, staffCount);
                facilityList.add(facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facilityList;
    }

    private void updateFacilitySwitch(int id, int newSwitch) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "UPDATE facility SET switch = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newSwitch);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Facility> getClosedFacilities() {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "SELECT id, name FROM facility WHERE switch = 0";

        List<Facility> closedFacilities = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                closedFacilities.add(new Facility(id, name, 0, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return closedFacilities;
    }

    private List<Integer> getStaffIdsByFacilityId(int facilityId) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "SELECT staff_id FROM schedule WHERE facility_id = ?";

        List<Integer> staffIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, facilityId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                staffIds.add(rs.getInt("staff_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffIds;
    }

    private void updateStaffFacilityId(int staffId, int newFacilityId) {
        String url = "jdbc:mysql://localhost:3306/schedulesystem?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String user = "root";
        String pass = "031012";

        String query = "UPDATE schedule SET facility_id = ? WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newFacilityId);
            stmt.setInt(2, staffId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
