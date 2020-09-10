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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ADMIN
 */
public final class InvitePanel extends JPanel {

    private final JLabel labelMessage = new JLabel();
    private final JButton buttonAccept = new JButton();
    private final JButton buttonReject = new JButton();

    public InvitePanel(Client client) {
        labelMessage.setText("Do you want to play with " + client.getOpponentName() + "?");
        buttonAccept.setText("Accept");
        buttonAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = new Request(RequestKey.ACCEPT_INVITE, null);
                client.getClientThread().sendRequestToServer(request);
                client.getInviteFrame().dispose();
            }
        });
        buttonReject.setText("Reject");
        buttonReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = new Request(RequestKey.REJECT_INVITE, null);
                client.getClientThread().sendRequestToServer(request);
                client.setBusy(false);
                client.getInviteFrame().dispose();
            }
        });

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 4, 4, 4);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(labelMessage, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(buttonAccept, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(buttonReject, gbc);
    }
}
