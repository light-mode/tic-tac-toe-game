/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.JFrame;

/**
 *
 * @author ADMIN
 */
public final class LoginFrame extends JFrame {

    public LoginFrame(Client client) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setLoginPanel(new LoginPanel(client));
        add(client.getLoginPanel());
        pack();
        setMinimumSize(getSize());
    }
}
