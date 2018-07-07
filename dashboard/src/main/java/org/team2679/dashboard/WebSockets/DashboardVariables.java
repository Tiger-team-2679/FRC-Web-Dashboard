package org.team2679.dashboard.WebSockets;

import kotlin.Unit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;
import org.team2679.dashboard.DashboardConfig;
import org.team2679.util.Notifier;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class DashboardVariables {

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
            } catch (Exception e) { }
        });
    }

    public static void start(){
        Notifier notifier = new Notifier( () -> {
            periodic();
            return Unit.INSTANCE;
        }, DashboardConfig.VariablesRefreshRate);
        notifier.start();
    }

    public static void periodic(){
        sessions.forEach(session -> {
            try{
                session.getRemote().sendString(values.toString());
            } catch (Exception e) { }
        });
    }

    public static void putNumber(String name, int value){
        values.put(name, value);
    }

    public static void putString(String name, String value){
        values.put(name, value);
    }

    public static void putDouble(String name, double value){
        values.put(name, value);
    }

    public static void putBoolean(String name, boolean value){
        values.put(name, value);
    }
}
