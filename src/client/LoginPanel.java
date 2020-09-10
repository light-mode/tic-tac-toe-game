/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Request;
import data.RequestKey;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public final class LoginPanel extends JPanel {

    private final Client client;
    private final JLabel labelName = new JLabel();
    private final JTextField textFieldName = new JTextField();
    private final JButton buttonLogin = new JButton();
    private boolean connectedToServer = false;

    public LoginPanel(Client client) {
        this.client = client;
        labelName.setText("Enter your name:");
        textFieldName.setColumns(7);
        buttonLogin.setText("Login");
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!connectedToServer) {
                    connectToServer();
                    sendName();
                } else {
                    sendName();
                }
            }
        });

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(3, 4, 4, 4);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(labelName, gbc);

        gbc.gridx = 1;
        add(textFieldName, gbc);

        gbc.gridx = 2;
        add(buttonLogin, gbc);
    }

    private void connectToServer() {
        try {
            client.setSocket(new Socket("localhost", 49152));
            connectedToServer = true;
            client.setClientThread(new ClientThread(client));
            client.getClientThread().start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Server offline");
        }
    }

    private void sendName() {
        if (!connectedToServer) {
            return;
        }
        String name = textFieldName.getText().trim();
        if (isInvalidName(name)) {
            return;
        }
        Request request = new Request(RequestKey.SEND_NAME, name);
        client.getClientThread().sendRequestToServer(request);
    }

    private boolean isInvalidName(String name) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
            return true;
        } else if (name.length() >= 9) {
            JOptionPane.showMessageDialog(this, "Name too long");
            return true;
        } else if (name.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Name cannot contains space");
            return true;
        } else {
            return false;
        }
    }

    public void showServerShutdownMessage() {
        JOptionPane.showMessageDialog(this, "Server shutdown");
    }

    public void showExistedNameMessage() {
        JOptionPane.showMessageDialog(this, "Existed name");
    }
}
