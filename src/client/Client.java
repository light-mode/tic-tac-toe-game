/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Socket;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ADMIN
 */
public class Client {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        Client client = new Client();
        client.setLoginFrame(new LoginFrame(client));
        client.getLoginFrame().setVisible(true);
    }
    private LoginFrame loginFrame;
    private LoginPanel loginPanel;
    private Socket socket;
    private ClientThread clientThread;
    private RoomFrame roomFrame;
    private RoomPanel roomPanel;
    private InvitingFrame invitingFrame;
    private InviteFrame inviteFrame;
    private GameFrame gameFrame;
    private GamePanel gamePanel;
    private String name;
    private String opponentName;
    private boolean playerX;
    private boolean busy;

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    public RoomFrame getRoomFrame() {
        return roomFrame;
    }

    public void setRoomFrame(RoomFrame roomFrame) {
        this.roomFrame = roomFrame;
    }

    public RoomPanel getRoomPanel() {
        return roomPanel;
    }

    public void setRoomPanel(RoomPanel roomPanel) {
        this.roomPanel = roomPanel;
    }

    public InvitingFrame getInvitingFrame() {
        return invitingFrame;
    }

    public void setInvitingFrame(InvitingFrame invitingFrame) {
        this.invitingFrame = invitingFrame;
    }

    public InviteFrame getInviteFrame() {
        return inviteFrame;
    }

    public void setInviteFrame(InviteFrame inviteFrame) {
        this.inviteFrame = inviteFrame;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public boolean isPlayerX() {
        return playerX;
    }

    public void setPlayerX(boolean playerX) {
        this.playerX = playerX;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
