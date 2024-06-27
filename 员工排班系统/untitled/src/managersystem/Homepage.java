package managersystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Homepage {

    public Homepage() {
        JFrame frame = new JFrame("主页");
        frame.setSize(555, 270);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // 创建按钮和图标
        JButton staffButton = new JButton("员工排班");
        JButton fireButton = new JButton("开除员工");
        JButton facilitiesButton = new JButton("设施一览");
        JButton cfacilityButton = new JButton("管理设施");

        JLabel staffLabel = new JLabel(new ImageIcon(getClass().getResource("/picture/全部员工.jpg")));
        JLabel fireLabel = new JLabel(new ImageIcon(getClass().getResource("/picture/开除员工.jpg")));
        JLabel facilitiesLabel = new JLabel(new ImageIcon(getClass().getResource("/picture/设施一览.jpg")));
        JLabel cfacilityLabel = new JLabel(new ImageIcon(getClass().getResource("/picture/管理设施.jpg")));

        // 设置按钮和图标位置及大小
        staffLabel.setBounds(50, 50, 85, 85);
        staffButton.setBounds(50, 145, 85, 30);

        fireLabel.setBounds(175, 50, 85, 85);
        fireButton.setBounds(175, 145, 85, 30);

        facilitiesLabel.setBounds(300, 50, 85, 85);
        facilitiesButton.setBounds(300, 145, 85, 30);

        cfacilityLabel.setBounds(425, 50, 85, 85);
        cfacilityButton.setBounds(425, 145, 85, 30);

        // 设置按钮动作监听器
        Staffevent staffevent = new Staffevent();
        Facilityevent facilityevent = new Facilityevent();

        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                staffevent.showStaffData();
            }
        });

        facilitiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facilityevent.showFacilitiesData();
            }
        });

        fireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                staffevent.fireEmployee();
            }
        });

        cfacilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facilityevent.facilitykannri();
            }
        });

        // 将按钮和图标添加到主面板
        mainPanel.add(staffLabel);
        mainPanel.add(staffButton);
        mainPanel.add(fireLabel);
        mainPanel.add(fireButton);
        mainPanel.add(facilitiesLabel);
        mainPanel.add(facilitiesButton);
        mainPanel.add(cfacilityLabel);
        mainPanel.add(cfacilityButton);
        mainPanel.setBackground(Color.WHITE);

        // 将主面板添加到框架
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}