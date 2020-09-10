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
public final class GameFrame extends JFrame {

    public GameFrame(Client client) {
        setTitle("Tic-Tac-Toe");
        client.setGamePanel(new GamePanel(client, 3));
        add(client.getGamePanel());
        pack();
        setMinimumSize(getSize());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                Request request = new Request(RequestKey.EXIT_GAME, null);
                client.getClientThread().sendRequestToServer(request);
                client.setBusy(false);
                dispose();
            }
        });
    }
}
