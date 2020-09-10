/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Move;
import data.Request;
import data.Response;
import data.ResponseKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class ClientThread implements Runnable {

    private Client client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientThread(Client client) {
        this.client = client;
        try {
            oos = new ObjectOutputStream(client.getSocket().getOutputStream());
            ois = new ObjectInputStream(client.getSocket().getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void sendRequestToServer(Request request) {
        try {
            oos.writeObject(request);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Response response = (Response) ois.readObject();
                handleResponseFromServer(response);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                handleServerShutdown();
                break;
            }
        }
    }

    private void handleResponseFromServer(Response response) {
        ResponseKey responseKey = response.getKey();
        switch (responseKey) {
            case EXISTED_NAME:
                handleExistedNameResponse();
                break;
            case ACCEPTED_NAME:
                handleAcceptedNameResponse(response);
                break;
            case UPDATE_NAME_LIST:
                handleUpdateNameListResponse(response);
                break;
            case RECEIVE_INVITE:
                handleReceiveInviteResponse(response);
                break;
            case INVITE_CANCELED:
                handleInviteCanceledResponse();
                break;
            case INVITE_ACCEPTED:
                handleInviteAcceptedResponse(response);
                break;
            case INVITE_REJECTED:
                handleInviteRejectedResponse();
                break;
            case OPPONENT_LEFT_ROOM:
                handleOpponentLeftRoomResponse();
                break;
            case RECEIVE_MOVE:
                handleReceiveMoveResponse(response);
                break;
            case YOU_WIN:
                handleYouWinResponse();
                break;
            case YOU_LOSE:
                handleYouLoseResponse();
                break;
            case DRAW:
                handleDrawResponse();
                break;
            case OPPONENT_EXITED_GAME:
                handleOpponentExitedGameResponse();
                break;
            default:
                break;
        }
    }

    private void handleExistedNameResponse() {
        client.getLoginPanel().showExistedNameMessage();
    }

    private void handleAcceptedNameResponse(Response response) {
        client.setName(response.getValue().toString().trim());
        client.setRoomFrame(new RoomFrame(client));
        client.getRoomFrame().setLocationRelativeTo(client.getLoginFrame());
        client.getRoomFrame().setVisible(true);
        client.getLoginFrame().dispose();
    }

    @SuppressWarnings("unchecked")
    private void handleUpdateNameListResponse(Response response) {
        client.getRoomPanel().updateNameList((Map<String, String>) response.getValue());
    }

    private void handleReceiveInviteResponse(Response response) {
        client.setBusy(true);
        client.setOpponentName(response.getValue().toString().trim());
        client.setInviteFrame(new InviteFrame(client));
        client.getInviteFrame().setLocationRelativeTo(client.getRoomFrame());
        client.getInviteFrame().setVisible(true);
    }

    private void handleInviteCanceledResponse() {
        client.getInviteFrame().dispose();
        client.getRoomPanel().showInviteCanceledMessage();
        client.setBusy(false);
    }

    private void handleInviteAcceptedResponse(Response response) {
        if (client.getInvitingFrame() != null && client.getInvitingFrame().isDisplayable()) {
            client.getInvitingFrame().dispose();
        }
        if (client.getGameFrame() != null && client.getGameFrame().isDisplayable()) {
            client.getGameFrame().dispose();
        }
        client.setPlayerX((boolean) response.getValue());
        client.setGameFrame(new GameFrame(client));
        client.getGameFrame().setLocationRelativeTo(client.getRoomFrame());
        client.getGameFrame().setVisible(true);
    }

    private void handleInviteRejectedResponse() {
        client.getInvitingFrame().dispose();
        client.getRoomPanel().showInviteRejectedMessage();
        client.setBusy(false);
    }

    private void handleOpponentLeftRoomResponse() {
        if (client.getInvitingFrame() != null && client.getInvitingFrame().isDisplayable()) {
            client.getInvitingFrame().dispose();
        }
        if (client.getInviteFrame() != null && client.getInviteFrame().isDisplayable()) {
            client.getInviteFrame().dispose();
        }
        if (client.getGameFrame() != null && client.getGameFrame().isDisplayable()) {
            client.getGameFrame().dispose();
        }
        client.getRoomPanel().showOpponentLeftRoomMessage();
        client.setBusy(false);
    }

    private void handleReceiveMoveResponse(Response response) {
        client.getGamePanel().setOpponentMove((Move) response.getValue());
    }

    private void handleYouWinResponse() {
        client.getRoomPanel().showYouWinMessage();
        client.getGameFrame().dispose();
    }

    private void handleYouLoseResponse() {
        client.getRoomPanel().showYouLoseMessage();
        client.getGameFrame().dispose();
    }

    private void handleDrawResponse() {
        client.getRoomPanel().showDrawMessage();
        client.getGameFrame().dispose();
    }

    private void handleOpponentExitedGameResponse() {
        client.getGamePanel().showOpponentExitedGameMessage();
        client.getGameFrame().dispose();
        client.setBusy(false);
    }

    private void handleServerShutdown() {
        if (client.getLoginFrame() != null && client.getLoginFrame().isDisplayable()) {
            client.getLoginPanel().showServerShutdownMessage();
            client.getLoginFrame().dispose();
        }
        if (client.getRoomFrame() != null && client.getRoomFrame().isDisplayable()) {
            client.getRoomPanel().showServerShutdownMessage();
            client.getRoomFrame().dispose();
        }
        if (client.getInvitingFrame() != null && client.getInvitingFrame().isDisplayable()) {
            client.getInvitingFrame().dispose();
        }
        if (client.getInviteFrame() != null && client.getInviteFrame().isDisplayable()) {
            client.getInviteFrame().dispose();
        }
        if (client.getGameFrame() != null && client.getGameFrame().isDisplayable()) {
            client.getGameFrame().dispose();
        }
    }
}
