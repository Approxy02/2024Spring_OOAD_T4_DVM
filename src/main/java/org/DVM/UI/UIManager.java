package org.DVM.UI;

import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Stock.Item;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class UIManager extends JFrame{
    private String UItype;
    private String errorMsg;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String mainDisplayString = null;
    private Item item = new Item("null", 0, 0, 0);
    private JLabel categoryValue;
    private JLabel quantityValue;

    public UIManager() {
//        this.UItype = UItype;
        setTitle("Distributed Vending Machine");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add all panels
        mainPanel.add(createMainPanel(), "MainPanel");
        mainPanel.add(createPaymentPanel1(), "PaymentPanel1");
        mainPanel.add(createPaymentPanel2(), "PaymentPanel2");
        mainPanel.add(createPrePaymentPanel1(), "PrePaymentPanel1");
        mainPanel.add(createPrePaymentPanel2(), "PrePaymentPanel2");
        mainPanel.add(createVerificationCodePanel(), "VerificationCodePanel");
        mainPanel.add(createLocationInfoPanel(), "LocationInfoPanel");
        mainPanel.add(createDispenseResultPanel(), "DispenseResultPanel");

        add(mainPanel);

        setVisible(true);
    }

    public void display(String UItype, ArrayList<Item> items, Item item, OtherDVM dvm, String vCode) {
        switch (UItype) {
            case "MainUI":
                mainUIdisplay(items);
                break;
            case "PaymentUI-1":
                payUI_1(item);
                break;
            case "PaymentUI-2":
                payUI_2(item);
                break;
            case "PrepaymentUI-1":
                prepayUI_1(item);
                break;
            case "PrepaymentUI-2":
                prepayUI_2(item);
                break;
            case "LocationInfoUI":
                locationInfoUI(item);
                break;
            case "VerificationCodeDisplayUI":
                vCodeUI(item);
                break;
            case "DispenseResultUI":
                dispenseUI(item);
                break;
            default:
                break;
        }
//        return "Out of switch statement";
    }


    private void mainUIdisplay(ArrayList<Item> items) {
        cardLayout.show(mainPanel, "MainPanel");
    }

    private void payUI_1(Item item) {
        System.out.println("payUI_1");
        this.item = item;
        categoryValue.setText(item.name()+"("+item.code()+")");
        quantityValue.setText(String.valueOf(item.quantity()));
        cardLayout.show(mainPanel, "PaymentPanel1");
    }

    private void payUI_2(Item item) {
    }

    private String prepayUI_1(Item item) {
        return "";
    }

    private void prepayUI_2(Item item) {
    }

    private void locationInfoUI(Item item) {
    }

    private void vCodeUI(Item item) {
    }

    private void dispenseUI(Item item) {
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Grid Panel
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
        panel.add(itemGridPanel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField categoryField = new JTextField("");
        addPlaceholderText(categoryField, "종류(코드입력)");
        setInputNumericOnly(categoryField);

        JTextField quantityField = new JTextField("");
        addPlaceholderText(quantityField, "수량(0~99)");
        setInputNumericOnly(quantityField);


        JButton purchaseButton = new JButton("구매하기");

        JTextField verificationCodeField = new JTextField("");
        addPlaceholderText(verificationCodeField, "인증코드");

        JButton prePaymentCodeButton = new JButton("선결제 인증코드");

        inputPanel.add(categoryField);
        inputPanel.add(quantityField);
        inputPanel.add(purchaseButton);
        inputPanel.add(new JLabel()); // 빈 칸
        inputPanel.add(verificationCodeField);
        inputPanel.add(prePaymentCodeButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        // Action Listeners
        purchaseButton.addActionListener(e -> {
            mainDisplayString = categoryField.getText();
            mainDisplayString += " " + quantityField.getText();
            synchronized (UIManager.this) {
                UIManager.this.notify(); // Notify waiting thread
            }
        });

        prePaymentCodeButton.addActionListener(e -> {
            // Pre-payment code action
            mainDisplayString = verificationCodeField.getText();
            if(mainDisplayString.length() != 10){
                JOptionPane.showMessageDialog(null, "인증코드는 10자리여야 합니다.");
            }
            else{
                synchronized (UIManager.this) {
                    UIManager.this.notify(); // Notify waiting thread
                }
            }
        });

        return panel;
    }

    private JPanel createPaymentPanel1() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        System.out.println("item: " + item );

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        String item_name = item.name()+"("+item.code()+")";
        System.out.println("item_name: " + item_name);
        categoryValue = new JLabel("item_name", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        quantityValue = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
        JTextField cardNumberField = new JTextField("카드번호");
        setInputNumericOnly(cardNumberField);

        JButton paymentButton = new JButton("결제");

        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(paymentButton);

        panel.add(paymentPanel, BorderLayout.SOUTH);

        paymentButton.addActionListener(e -> {
            // Handle payment
            paymentButton.setEnabled(false);
            cardNumberField.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "결제중...");
            // Simulate payment delay
//            Timer timer = new Timer(2000, evt -> {
//                paymentButton.setEnabled(true);
//                cardNumberField.setEnabled(true);
//                JOptionPane.showMessageDialog(this, "결제가 완료되었습니다!");
//                cardLayout.show(mainPanel, "PaymentPanel2");
//            });
//            timer.setRepeats(false);
//            timer.start();
            mainDisplayString = cardNumberField.getText();
            synchronized (UIManager.this) {
                UIManager.this.notify(); // Notify waiting thread
            }
        });

        return panel;
    }

    private JPanel createPaymentPanel2() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
        JButton receiveButton = new JButton("음료 받기");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(receiveButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        receiveButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPanel"));

        return panel;
    }

    private JPanel createPrePaymentPanel1() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(3, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(5, 5)", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);
        itemInfoPanel.add(locationLabel);
        itemInfoPanel.add(locationValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
        JTextField cardNumberField = new JTextField("카드번호");
        JButton paymentButton = new JButton("결제");

        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(paymentButton);

        panel.add(paymentPanel, BorderLayout.SOUTH);

        paymentButton.addActionListener(e -> {
            // Handle payment
            paymentButton.setEnabled(false);
            cardNumberField.setEnabled(false);
            JOptionPane.showMessageDialog(this, "결제중...");
            // Simulate payment delay
            Timer timer = new Timer(2000, evt -> {
                paymentButton.setEnabled(true);
                cardNumberField.setEnabled(true);
                JOptionPane.showMessageDialog(this, "결제가 완료되었습니다!");
                cardLayout.show(mainPanel, "PrePaymentPanel2");
            });
            timer.setRepeats(false);
            timer.start();
        });

        return panel;
    }

    private JPanel createPrePaymentPanel2() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(3, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(5, 5)", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);
        itemInfoPanel.add(locationLabel);
        itemInfoPanel.add(locationValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
        JButton receiveCodeButton = new JButton("인증 코드 받기");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(receiveCodeButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        receiveCodeButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPanel"));

        return panel;
    }

    private JPanel createVerificationCodePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("DVM 이름", JLabel.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel nameValue = new JLabel("Team5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(x, y)", JLabel.CENTER);

        JLabel codeLabel = new JLabel("인증코드", JLabel.CENTER);
        codeLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel codeValue = new JLabel("AD13254D33", JLabel.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(locationLabel);
        infoPanel.add(locationValue);
        infoPanel.add(codeLabel);
        infoPanel.add(codeValue);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        JButton backButton = new JButton("돌아가기");
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPanel"));

        return panel;
    }

    private JPanel createLocationInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("DVM 이름", JLabel.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel nameValue = new JLabel("Team5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(x, y)", JLabel.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(locationLabel);
        infoPanel.add(locationValue);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        JButton nextButton = new JButton("다음");
        buttonPanel.add(nextButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPanel"));

        return panel;
    }

    private JPanel createDispenseResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Distributed Vending Machine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("음료를 수령하세요!", JLabel.CENTER);
        JButton completeButton = new JButton("완료");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(completeButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        completeButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPanel"));

        return panel;
    }

    public synchronized String returnString(String s) {
        mainDisplayString = s;
        return mainDisplayString;
    }

    public synchronized String waitForInputString() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mainDisplayString;
    }

    public synchronized String waitForInput() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mainDisplayString;
    }

    private void setInputNumericOnly(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }

                if (string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }

                if (text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    private void addPlaceholderText(JTextField textField, String placeholderText) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholderText);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholderText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholderText);
                }
            }
        });
    }

}
