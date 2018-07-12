package org.team2679.dashboard.WebSockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.team2679.util.log.LogHandler;
import org.team2679.util.log.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class LoggerSocket implements LogHandler {

    private static ConcurrentLinkedQueue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        sessions.add(session);
        try {
            BufferedReader file_reader = new BufferedReader(new FileReader(Logger.INSTANCE.getLogFile()));
            String ln;
            while ((ln = file_reader.readLine()) != null)
                session.getRemote().sendString(ln);
        } catch (Exception e) {
            Logger.INSTANCE.logThrowException(e);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException { }

    @Override
    public void onLog(@NotNull Logger.ENTRY_TYPE type, @NotNull String message, @NotNull String formatted) {
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(formatted);
            } catch (Exception e) {
                Logger.INSTANCE.logThrowException(e);
            }
        });
    }
}
