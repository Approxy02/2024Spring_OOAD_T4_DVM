package org.DVM.UI;

import javax.swing.*;
import java.awt.*;

public class Example extends JFrame {

    public Example() {
        setTitle("Distributed Vending Machine");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel itemGridPanel = new JPanel();
        itemGridPanel.setLayout(new GridLayout(4, 5));
        String[] itemNames = {
                "콜라(01)", "사이다(02)", "녹차(03)", "홍차(04)", "밀크티(05)",
                "탄산수(06)", "보리차(07)", "캔커피(08)", "물(09)", "에너지드링크(10)",
                "유자차(11)", "식혜(12)", "아이스티(13)", "딸기주스(14)", "오렌지주스(15)",
                "포도주스(16)", "이온음료(17)", "아메리카노(18)", "핫초코(19)", "카페라떼(20)"
        };

        for (String itemName : itemNames) {
            JLabel itemLabel = new JLabel(itemName, JLabel.CENTER);
            itemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemGridPanel.add(itemLabel);
        }
        add(itemGridPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton categoryButton = new JButton("종류");
        JTextField quantityField = new JTextField("수량(0~99)");
        JButton purchaseButton = new JButton("구매하기");
        JButton verificationCodeButton = new JButton("인증코드");
        JButton prePaymentCodeButton = new JButton("선결제 인증코드");

        inputPanel.add(categoryButton);
        inputPanel.add(quantityField);
        inputPanel.add(purchaseButton);
        inputPanel.add(verificationCodeButton);
        inputPanel.add(new JLabel()); // 빈 칸
        inputPanel.add(new JLabel()); // 빈 칸
        inputPanel.add(new JLabel()); // 빈 칸
        inputPanel.add(prePaymentCodeButton);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Example::new);
    }
}
