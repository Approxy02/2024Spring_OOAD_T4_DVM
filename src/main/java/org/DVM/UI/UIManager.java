package org.DVM.UI;

import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Main;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;

public class UIManager extends JFrame {
    String title = "Distributed Vending Machine";

    //region <UI 객체 정의>

    private class UIPanel extends JPanel{
        public UIPanel(){
            super(new BorderLayout());

            // Title Panel
            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel(title, JLabel.CENTER);
            
            titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 18));
            titlePanel.add(titleLabel);

            this.add(titlePanel, BorderLayout.NORTH);
        }

        protected void processItems(ArrayList<Item> items) {}
        protected void processItem(Item item) {}
        protected void processDVM(OtherDVM dvm) {}
        protected void processVCode(String vCode) {}

        public final void processInput(ArrayList<Item> items, Item item, OtherDVM dvm, String vCode) {
            this.processItems(items);
            this.processItem(item);
            this.processDVM(dvm);
            this.processVCode(vCode);
        }
    }

    private class UITextField extends JTextField {
        private final Document document = super.getDocument();
        private final Document placeHolderDocument = new PlainDocument();

        private String placeHolder;

        @Override
        public String getText() {
            try {
                return this.document.getText(0, this.document.getLength());
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void setText(String text) {
            try {

                this.document.remove(0, this.document.getLength());
                this.document.insertString(0, text, null);

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        public String getPlaceHolder() { return this.placeHolder; }

        public void setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;

            try {

                this.placeHolderDocument.remove(0, this.placeHolderDocument.getLength());
                this.placeHolderDocument.insertString(0, placeHolder, null);

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        private void showPlaceHolder() {
            super.setDocument(this.placeHolderDocument);

            this.setForeground(Color.GRAY);
        }

        private void hidePlaceHolder() {
            super.setDocument(this.document);

            this.setForeground(Color.BLACK);
        }

        public UITextField() {
            this("", "");
        }

        public UITextField(String text) {
            this(text, "");
        }

        public UITextField(String text, String placeHolder) {
            setText(text); setPlaceHolder(placeHolder);

            if(this.document.getLength() == 0) {
                this.showPlaceHolder();
            }
            else{
                this.hidePlaceHolder();
            }

            this.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    hidePlaceHolder();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(document.getLength() == 0) showPlaceHolder();
                }
            });
        }

        public void setRegexFilter(String regex) {
            ((AbstractDocument)this.document).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) {
                        return;
                    }

                    if (string.matches(regex)) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) {
                        return;
                    }

                    if (text.matches(regex)) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                    super.remove(fb, offset, length);
                }
            });
        }

        public void setNumericOnly(){
            this.setRegexFilter("\\d+");
        }
    }

    //endregion

    //region <UI 객체 선언>

    private final CardLayout layout = new CardLayout();

    private final JPanel showPanel;

    private String mainDisplayString = null;

    private void showUI(String name){
        layout.show(showPanel, name);
    }

    private void process(String... args) {
        mainDisplayString = "";

        for (int i = 0; i < args.length; i++){
            mainDisplayString += args[i];

            if(i <= args.length - 1) mainDisplayString += " ";
        }

        synchronized (this) {
            this.notify(); // Notify waiting thread
        }
    }

    HashMap<String, UIPanel> UIPanels = new HashMap<String, UIPanel>();

    public UIManager() {
        setTitle(title);
        setLocationRelativeTo(null);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showPanel = new JPanel(layout);

        UIPanels.put("MainUI", new UIMain());
        UIPanels.put("PaymentUI_1", new UIPayment1());
        UIPanels.put("PaymentUI_2", new UIPayment2());
        UIPanels.put("PrepaymentUI_1", new UIPrePayment1());
        UIPanels.put("PrepaymentUI_2", new UIPrePayment2());
        UIPanels.put("LocationInfoUI", new UILocationInfo());
        UIPanels.put("VerificationCodeUI", new UIVerificationCode());
        UIPanels.put("DispenseResultUI", new UIDispenseResult());

        for (var entry : UIPanels.entrySet()){
            showPanel.add(entry.getValue(), entry.getKey());
        }

        add(showPanel);

        setVisible(true);
    }

    public void display(String UIType, ArrayList<Item> items, Item item, OtherDVM dvm, String vCode) {
        UIPanel panel = UIPanels.get(UIType);

        if(panel != null){
            panel.processInput(items, item, dvm, vCode);

            showUI(UIType);

            System.out.println(UIType);
        }
    }

    public synchronized String waitForInputString() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mainDisplayString;
    }

    public synchronized Item waitForInput() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item;
    }

    //endregion
 
    //region <UI 실제 구현>
    private Item item = new Item("null", 0, 0, 0);

    private class UIMain extends UIPanel {
        private final JPanel itemGridPanel = new JPanel();

        @Override
        protected void processItems(ArrayList<Item> items) {
            itemGridPanel.removeAll();

            Stock stock = new Stock();
            //실제 Stock이 들어오면 여기를 지워주세요
            items = stock.itemList();

            for (Item item : items) {
                JLabel itemLabel = new JLabel(String.format("%s(%02d)", item.name, item.code), JLabel.CENTER);

                itemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                itemGridPanel.add(itemLabel);
            }

            itemGridPanel.revalidate();
            itemGridPanel.repaint();
        }

        public UIMain(){
            super();

            itemGridPanel.setLayout(new GridLayout(4, 5));

            this.add(itemGridPanel, BorderLayout.CENTER);

            // Input Panel
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(2, 4, 10, 10));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            UITextField categoryField = new UITextField("", "종류(코드입력)");
            categoryField.setNumericOnly();

            UITextField quantityField = new UITextField("", "수량(0~99)");
            quantityField.setNumericOnly();

            JButton purchaseButton = new JButton("구매하기");

            UITextField verificationCodeField = new UITextField("", "인증코드");

            JButton prePaymentCodeButton = new JButton("선결제 인증코드");

            inputPanel.add(categoryField);
            inputPanel.add(quantityField);
            inputPanel.add(purchaseButton);
            inputPanel.add(new JLabel()); // 빈 칸
            inputPanel.add(verificationCodeField);
            inputPanel.add(prePaymentCodeButton);

            this.add(inputPanel, BorderLayout.SOUTH);

            // Action Listeners
            purchaseButton.addActionListener(e -> {
                process(categoryField.getText(), quantityField.getText());
            });

            prePaymentCodeButton.addActionListener(e -> {
                // Pre-payment code action
                if (verificationCodeField.getText().length() != 10) {
                    JOptionPane.showMessageDialog(null, "인증코드는 10자리여야 합니다.");

                    return;
                }

                process(verificationCodeField.getText());
            });
        }
    }

    private class UIPayment1 extends UIPanel {
        private JLabel categoryValue;
        private JLabel quantityValue;

        @Override
        protected void processItem(Item item) {
            categoryValue.setText(item.name + "(" + item.code + ")");
            quantityValue.setText(String.valueOf(item.quantity));
        }

        public UIPayment1() {
            // Item Info Panel
            JPanel itemInfoPanel = new JPanel();
            itemInfoPanel.setLayout(new GridLayout(2, 2));
            itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            System.out.println("item: " + item);

            JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
            categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            String item_name = item.name + "(" + item.code + ")";
            System.out.println("item_name: " + item_name);
            categoryValue = new JLabel("item_name", JLabel.CENTER);

            JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
            quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            quantityValue = new JLabel("5", JLabel.CENTER);

            itemInfoPanel.add(categoryLabel);
            itemInfoPanel.add(categoryValue);
            itemInfoPanel.add(quantityLabel);
            itemInfoPanel.add(quantityValue);

            this.add(itemInfoPanel, BorderLayout.CENTER);

            // Payment Panel
            JPanel paymentPanel = new JPanel();
            paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
            paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
            UITextField cardNumberField = new UITextField("", "카드번호");
            cardNumberField.setNumericOnly();

            JButton paymentButton = new JButton("결제");

            paymentPanel.add(cardNumberLabel);
            paymentPanel.add(cardNumberField);
            paymentPanel.add(paymentButton);

            this.add(paymentPanel, BorderLayout.SOUTH);

            paymentButton.addActionListener(e -> {
                process(cardNumberField.getText());
            });
        }
    }

    private class UIPayment2 extends UIPanel {
        private JLabel categoryValue;
        private JLabel quantityValue;

        @Override
        protected void processItem(Item item) {
            categoryValue.setText(item.name + "(" + item.code + ")");
            quantityValue.setText(String.valueOf(item.quantity));
        }

        public UIPayment2() {
            // Item Info Panel
            JPanel itemInfoPanel = new JPanel();
            itemInfoPanel.setLayout(new GridLayout(2, 2));
            itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
            categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

            JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
            quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            quantityValue = new JLabel("5", JLabel.CENTER);

            itemInfoPanel.add(categoryLabel);
            itemInfoPanel.add(categoryValue);
            itemInfoPanel.add(quantityLabel);
            itemInfoPanel.add(quantityValue);

            this.add(itemInfoPanel, BorderLayout.CENTER);

            // Confirmation Panel
            JPanel confirmationPanel = new JPanel();
            confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

            JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
            JButton receiveButton = new JButton("음료 받기");

            confirmationPanel.add(confirmationLabel);
            confirmationPanel.add(receiveButton);

            this.add(confirmationPanel, BorderLayout.SOUTH);

            receiveButton.addActionListener(e -> {
                process();
            });
        }
    }

    private class UIPrePayment1 extends UIPanel {
        public UIPrePayment1() {
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

            this.add(itemInfoPanel, BorderLayout.CENTER);

            // Payment Panel
            JPanel paymentPanel = new JPanel();
            paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
            paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
            UITextField cardNumberField = new UITextField("", "카드번호");
            JButton paymentButton = new JButton("결제");

            paymentPanel.add(cardNumberLabel);
            paymentPanel.add(cardNumberField);
            paymentPanel.add(paymentButton);

            this.add(paymentPanel, BorderLayout.SOUTH);

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
                    showUI("PrepaymentUI_2");
                });
                timer.setRepeats(false);
                timer.start();
            });
        }
    }

    private class UIPrePayment2 extends UIPanel {
        public UIPrePayment2() {
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

            this.add(itemInfoPanel, BorderLayout.CENTER);

            // Confirmation Panel
            JPanel confirmationPanel = new JPanel();
            confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

            JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
            JButton receiveCodeButton = new JButton("인증 코드 받기");

            confirmationPanel.add(confirmationLabel);
            confirmationPanel.add(receiveCodeButton);

            this.add(confirmationPanel, BorderLayout.SOUTH);

            receiveCodeButton.addActionListener(e -> showUI("MainUI"));
        }
    }

    private class UIVerificationCode extends UIPanel {
        public UIVerificationCode() {
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

            this.add(infoPanel, BorderLayout.CENTER);

            // Button Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

            JButton backButton = new JButton("돌아가기");
            buttonPanel.add(backButton);

            this.add(buttonPanel, BorderLayout.SOUTH);

            backButton.addActionListener(e -> showUI("MainUI"));
        }
    }

    private class UILocationInfo extends UIPanel {
        private JLabel nameValue;
        private JLabel locationValue;

        @Override
        protected void processDVM(OtherDVM dvm) {
            nameValue.setText(dvm.name);
            locationValue.setText("(" + dvm.coor_x + ", " + dvm.coor_y + ")");
        }

        public UILocationInfo() {
            // Info Panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(2, 2));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel nameLabel = new JLabel("DVM 이름", JLabel.CENTER);
            nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            nameValue = new JLabel("Team5", JLabel.CENTER);

            JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
            locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            locationValue = new JLabel("(x, y)", JLabel.CENTER);

            infoPanel.add(nameLabel);
            infoPanel.add(nameValue);
            infoPanel.add(locationLabel);
            infoPanel.add(locationValue);

            this.add(infoPanel, BorderLayout.CENTER);

            // Button Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

            JButton nextButton = new JButton("다음");
            buttonPanel.add(nextButton);

            this.add(buttonPanel, BorderLayout.SOUTH);

            nextButton.addActionListener(e -> showUI("MainUI"));
        }
    }

    private class UIDispenseResult extends UIPanel {
        private JLabel categoryValue;
        private JLabel quantityValue;

        @Override
        protected void processItem(Item item) {
            categoryValue.setText(item.name + "(" + item.code + ")");
            quantityValue.setText(String.valueOf(item.quantity));
        }

        public UIDispenseResult() {
            // Item Info Panel
            JPanel itemInfoPanel = new JPanel();
            itemInfoPanel.setLayout(new GridLayout(2, 2));
            itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
            categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

            JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
            quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            quantityValue = new JLabel("5", JLabel.CENTER);

            itemInfoPanel.add(categoryLabel);
            itemInfoPanel.add(categoryValue);
            itemInfoPanel.add(quantityLabel);
            itemInfoPanel.add(quantityValue);

            this.add(itemInfoPanel, BorderLayout.CENTER);

            // Confirmation Panel
            JPanel confirmationPanel = new JPanel();
            confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

            JLabel confirmationLabel = new JLabel("음료를 수령하세요!", JLabel.CENTER);
            JButton completeButton = new JButton("완료");

            confirmationPanel.add(confirmationLabel);
            confirmationPanel.add(completeButton);

            this.add(confirmationPanel, BorderLayout.SOUTH);

            completeButton.addActionListener(e -> {
                synchronized (UIManager.this) {
                    UIManager.this.notify(); // Notify waiting thread
                }
            });
        }
    }
    //endregion
}
