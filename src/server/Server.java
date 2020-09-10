/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Response;
import data.ResponseKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ADMIN
 */
public class Server {

    public static void main(String[] args) {
        new Server().open();
    }
    private final int port = 49152;
    private final Set<ServerThread> notNamedServerThreads = new LinkedHashSet<>(0);
    private final Set<ServerThread> namedServerThreads = new LinkedHashSet<>(0);

    public Set<ServerThread> getNotNamedServerThreads() {
        return notNamedServerThreads;
    }

    public Set<ServerThread> getNamedServerThreads() {
        return namedServerThreads;
    }

    public void sendUpdateNameListResponseToAllClient() {
        Map<String, String> map = new LinkedHashMap<>(0);
        for (ServerThread serverThread : namedServerThreads) {
            map.put(serverThread.getName(), serverThread.getStatus());
        }
        for (ServerThread serverThread : namedServerThreads) {
            Response response = new Response(ResponseKey.UPDATE_NAME_LIST, map);
            serverThread.sendResponseToClient(response);
        }
    }

    private void open() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this, socket);
                notNamedServerThreads.add(serverThread);
                threadPool.execute(serverThread);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
