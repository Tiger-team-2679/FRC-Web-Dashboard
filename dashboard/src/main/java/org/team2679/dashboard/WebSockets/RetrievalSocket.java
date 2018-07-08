package org.team2679.dashboard.WebSockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;
import org.team2679.util.log.Logger;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class RetrievalSocket {

    private static ArrayList<Session> sessions = new ArrayList<>();

    private static JSONObject values = new JSONObject();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        sessions.forEach(session -> {
            try{
                session.getRemote().sendString(message);
            } catch (Exception e) {
                Logger.INSTANCE.logThrowException(e);
            }
        });
    }

    private static void onPut(){
        sessions.forEach(session -> {
            try{
                session.getRemote().sendString(values.toString());
            } catch (Exception e) {
                Logger.INSTANCE.logThrowException(e);
            }
        });
    }

    private static void message(){System.out.println("hello java"); }

    private static void putNumber(String name, int value){
        values.put(name, value);
        onPut();
    }

    private static void putString(String name, String value){
        values.put(name, value);
        onPut();
    }

    private static void putDouble(String name, double value){
        values.put(name, value);
        onPut();
    }

    private static void putBoolean(String name, boolean value){
        values.put(name, value);
        onPut();
    }
}
