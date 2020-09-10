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
public final class InvitingPanel extends JPanel {

    private final JLabel labelMessage = new JLabel();
    private final JButton buttonCancel = new JButton();

    public InvitingPanel(Client client) {
        labelMessage.setText("Inviting " + client.getOpponentName());
        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = new Request(RequestKey.CANCEL_INVITE, null);
                client.getClientThread().sendRequestToServer(request);
                client.setBusy(false);
                client.getInvitingFrame().dispose();
            }
        });

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(3, 4, 4, 4);
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelMessage, gbc);

        gbc.gridy = 1;
        add(buttonCancel, gbc);
    }
}
