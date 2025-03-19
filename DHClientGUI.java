import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DHClientGUI {

    private JFrame window;
    private JTextField serverInputField;
    private JTextArea messageInputArea;

    public DHClientGUI() {
        // Set up the main application window
        window = new JFrame("Encrypted Messaging");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(480, 380);
        window.getContentPane().setBackground(new Color(240, 248, 255));

        // Add main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(230, 240, 250));
        window.add(mainPanel);

        setupComponents(mainPanel);
        window.setVisible(true);
    }

    private void setupComponents(JPanel panel) {
        // Title Label
        JLabel titleLabel = new JLabel("Secure Messaging Client");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBounds(10, 10, 400, 30);
        titleLabel.setForeground(new Color(33, 37, 41));
        panel.add(titleLabel);

        // Server Input Label
        JLabel serverLabel = new JLabel("Server Address:");
        serverLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        serverLabel.setBounds(10, 60, 150, 25);
        panel.add(serverLabel);

        // Server Input Field
        serverInputField = new JTextField();
        serverInputField.setBounds(170, 60, 250, 25);
        serverInputField.setBackground(new Color(255, 255, 255));
        serverInputField.setForeground(Color.BLACK);
        panel.add(serverInputField);

        // Message Label
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setBounds(10, 100, 150, 25);
        panel.add(messageLabel);

        // Message Input Area
        messageInputArea = new JTextArea();
        JScrollPane messageScrollPane = new JScrollPane(messageInputArea);
        messageScrollPane.setBounds(10, 130, 410, 120);
        messageInputArea.setBackground(new Color(255, 255, 255));
        messageInputArea.setForeground(Color.BLACK);
        panel.add(messageScrollPane);

        // Submit Button
        JButton sendButton = new JButton("Encrypt & Send");
        sendButton.setBounds(10, 270, 200, 40);
        sendButton.setBackground(new Color(0, 150, 136));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setFocusPainted(false);
        panel.add(sendButton);

        // Add Action Listener for the button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String serverAddress = serverInputField.getText().trim();
        if (serverAddress.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Server address cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String messageToSend = messageInputArea.getText().trim();
        if (messageToSend.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Message cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DHClient.connectToServer(serverAddress, messageToSend);
            JOptionPane.showMessageDialog(window, "Message encrypted and sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(window, "Failed to send the message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DHClientGUI::new);
    }
}
