/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Request;
import data.RequestKey;
import data.Response;
import data.ResponseKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public class ServerThread implements Runnable {

    private final Server server;
    private String name;
    private String status;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ServerThread opponent;
    private boolean firstMatch = true;
    private boolean thisIsPlayerX;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        status = "";
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Request request = (Request) ois.readObject();
                handleRequestFromClient(request);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                handleClientLeftRoom();
                break;
            }
        }
    }

    private void handleClientLeftRoom() {
        if (opponent != null) {
            opponent.setStatus("");
            opponent.sendResponseToClient(new Response(ResponseKey.OPPONENT_LEFT_ROOM, null));
            opponent.setOpponent(null);
        }
        server.getNotNamedServerThreads().remove(this);
        server.getNamedServerThreads().remove(this);
        server.sendUpdateNameListResponseToAllClient();
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOpponent(ServerThread opponent) {
        this.opponent = opponent;
    }

    public void sendResponseToClient(Response response) {
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleRequestFromClient(Request request) {
        RequestKey requestKey = request.getKey();
        switch (requestKey) {
            case SEND_NAME:
                handleSendNameRequest(request);
                break;
            case SEND_INVITE:
                handleSendInviteRequest(request);
                break;
            case CANCEL_INVITE:
                handleCancelInviteRequest();
                break;
            case ACCEPT_INVITE:
                handleAcceptInviteRequest();
                break;
            case REJECT_INVITE:
                handleRejectInviteRequest();
                break;
            case SEND_MOVE:
                handleSendMoveRequest(request);
                break;
            case SEND_I_WIN:
                handleSendIWinRequest();
                break;
            case SEND_DRAW:
                handleSendDrawRequest();
                break;
            case EXIT_GAME:
                handleExitGameRequest();
                break;
            default:
                break;
        }
    }

    private void handleSendNameRequest(Request request) {
        String sentName = request.getValue().toString().trim();
        if (isExistedName(sentName)) {
            sendResponseToClient(new Response(ResponseKey.EXISTED_NAME, null));
        } else {
            sendResponseToClient(new Response(ResponseKey.ACCEPTED_NAME, sentName));
            name = sentName;
            server.getNamedServerThreads().add(this);
            server.getNotNamedServerThreads().remove(this);
            server.sendUpdateNameListResponseToAllClient();
        }
    }

    private boolean isExistedName(String sentName) {
        Set<ServerThread> namedServerThreads = server.getNamedServerThreads();
        for (ServerThread serverThread : namedServerThreads) {
            String existedName = serverThread.getName();
            if (sentName.equals(existedName)) {
                return true;
            }
        }
        return false;
    }

    private void handleSendInviteRequest(Request request) {
        String sentName = request.getValue().toString().trim();
        Set<ServerThread> namedServerThreads = server.getNamedServerThreads();
        for (ServerThread serverThread : namedServerThreads) {
            if (serverThread.getName().equals(sentName)) {
                opponent = serverThread;
                break;
            }
        }
        status = "(busy)";
        opponent.setOpponent(this);
        opponent.setStatus("(busy)");
        server.sendUpdateNameListResponseToAllClient();
        opponent.sendResponseToClient(new Response(ResponseKey.RECEIVE_INVITE, name));
    }

    private void handleCancelInviteRequest() {
        removeConnectionAndNotifyOpponent(ResponseKey.INVITE_CANCELED);
    }

    private void handleAcceptInviteRequest() {
        if (firstMatch) {
            firstMatch = false;
            thisIsPlayerX = new Random().nextBoolean();
        } else {
            thisIsPlayerX = !thisIsPlayerX;
        }
        if (thisIsPlayerX) {
            sendResponseToClient(new Response(ResponseKey.INVITE_ACCEPTED, true));
            opponent.sendResponseToClient(new Response(ResponseKey.INVITE_ACCEPTED, false));
        } else {
            sendResponseToClient(new Response(ResponseKey.INVITE_ACCEPTED, false));
            opponent.sendResponseToClient(new Response(ResponseKey.INVITE_ACCEPTED, true));
        }
    }

    private void handleRejectInviteRequest() {
        removeConnectionAndNotifyOpponent(ResponseKey.INVITE_REJECTED);
    }

    private void handleSendMoveRequest(Request request) {
        opponent.sendResponseToClient(new Response(ResponseKey.RECEIVE_MOVE, request.getValue()));
    }

    private void handleSendIWinRequest() {
        sendResponseToClient(new Response(ResponseKey.YOU_WIN, null));
        opponent.sendResponseToClient(new Response(ResponseKey.YOU_LOSE, null));
        handleAcceptInviteRequest();
    }

    private void handleSendDrawRequest() {
        sendResponseToClient(new Response(ResponseKey.DRAW, null));
        opponent.sendResponseToClient(new Response(ResponseKey.DRAW, null));
        handleAcceptInviteRequest();
    }

    private void handleExitGameRequest() {
        removeConnectionAndNotifyOpponent(ResponseKey.OPPONENT_EXITED_GAME);
    }

    private void removeConnectionAndNotifyOpponent(ResponseKey responseKey) {
        status = "";
        opponent.setStatus("");
        opponent.setOpponent(null);
        server.sendUpdateNameListResponseToAllClient();
        opponent.sendResponseToClient(new Response(responseKey, null));
        opponent = null;
        firstMatch = true;
    }
}
