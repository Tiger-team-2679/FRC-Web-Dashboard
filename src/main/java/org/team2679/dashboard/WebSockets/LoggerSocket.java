package org.team2679.dashboard.WebSockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.team2679.logging.LogHandler;
import org.team2679.logging.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class LoggerSocket implements LogHandler {

    private static ConcurrentLinkedQueue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        sessions.add(session);
        try {
            Logger.INSTANCE.getBuffered().forEach(it -> {
                try {
                    session.getRemote().sendString(it);
                } catch (IOException e) { }
            });
        } catch (Exception e) {
            Logger.INSTANCE.logFATAL(e.getMessage(), "dashboard", "loggerSocket");
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException { }

    @Override
    public void onLog(Logger.LOG_LEVEL log_level, String[] tags, String message, String formatted, String time) {
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(formatted);
            } catch (Exception e) {
                Logger.INSTANCE.logFATAL("problem sending log info to clients", "dashboard", "loggerSocket");
            }
        });
    }
}
