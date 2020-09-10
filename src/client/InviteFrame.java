/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Request;
import data.RequestKey;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author ADMIN
 */
public final class InviteFrame extends JFrame {

    public InviteFrame(Client client) {
        setTitle("Invite");
        add(new InvitePanel(client));
        pack();
        setMinimumSize(getSize());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                Request request = new Request(RequestKey.REJECT_INVITE, null);
                client.getClientThread().sendRequestToServer(request);
                client.setBusy(false);
                dispose();
            }
        });
    }
}
