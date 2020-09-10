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
public final class RoomFrame extends JFrame {

    public RoomFrame(Client client) {
        setTitle("Tic Tac Toe Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setRoomPanel(new RoomPanel(client));
        add(client.getRoomPanel());
        pack();
        setMinimumSize(getSize());
    }
}
