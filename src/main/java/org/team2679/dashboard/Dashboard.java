package org.team2679.dashboard;

import org.team2679.dashboard.Views.View;
import org.team2679.dashboard.WebSockets.RetrievalSocket;
import org.team2679.dashboard.WebSockets.LoggerSocket;
import org.team2679.logging.Logger;

import java.io.*;
import java.lang.reflect.Method;

import static spark.Spark.*;

public enum Dashboard {

    INSTANCE;

    private boolean initiated = false;

    public void init(int PORT){
        if(!initiated) {
            initiated = true;
            port(PORT);
            Logger.INSTANCE.logINFO("dashboard initiated", "dashboard");
            Logger.INSTANCE.registerHandler(new LoggerSocket());
            webSocket("/socket/coolandgood", RetrievalSocket.class);
            webSocket("/socket/logger", LoggerSocket.class);

            get("/script/:file", (req, res) -> {
                res.type("text/javascript");
                return loadResource("script/" + req.params(":file"));
            });

            get("/script/vendor/:file", (req, res) -> {
                res.type("text/javascript");
                return loadResource("script/vendor/" + req.params(":file"));
            });

            get("/style/:file", (req, res) -> {
                res.type("text/css");
                return loadResource("style/" + req.params(":file"));
            });

            get("/img/:file", (req, res) -> {
                res.type("image");
                return resourceRaw("img/" + req.params(":file"));
            });
        }
        else{
            Logger.INSTANCE.logWARNING("dashboard initiated twice", "dashboard");
        }
    }

    public void registerView(View view){
        get(view.address(), (req, res) -> view.handle(req, res));
    }

    public byte[] resourceRaw(String path) {
        try(InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
            Logger.INSTANCE.logFATAL("problem getting raw resource", "dashboard", "resources");
            return new byte[0];
        }
    }

    public String loadResource(String path){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));
            String line;
            String file = "";
            while ((line = reader.readLine()) != null) {
                file += line + "\n";
            }
            reader.close();
            return file;
        }
        catch (Exception e){
            Logger.INSTANCE.logFATAL("problem loading resources", "dashboard", "resource");
            return "";
        }
    }

    public void putNumber(String name, int value){
        try {
            Method method = RetrievalSocket.class.getDeclaredMethod("putNumber", String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, name, value);
        }
        catch (Exception e){
            Logger.INSTANCE.logFATAL("having problem calling data putter", "dashboard", "put");
        }
    }

    public void putString(String name, String value){
        try {
            Method method = RetrievalSocket.class.getDeclaredMethod("putString", String.class, String.class);
            method.setAccessible(true);
            method.invoke(null, name, value);
        }
        catch (Exception e){
            Logger.INSTANCE.logFATAL("having problem calling data putter", "dashboard", "put");
        }
    }

    public void putDouble(String name, double value){
        try {
            Method method = RetrievalSocket.class.getDeclaredMethod("putDouble", String.class, double.class);
            method.setAccessible(true);
            method.invoke(null, name, value);
        }
        catch (Exception e){
            Logger.INSTANCE.logFATAL("having problem calling data putter", "dashboard", "put");
        }
    }

    public void putBoolean(String name, boolean value){
        try {
            Method method = RetrievalSocket.class.getDeclaredMethod("putBoolean", String.class, boolean.class);
            method.setAccessible(true);
            method.invoke(null, name, value);
        }
        catch (Exception e){
            Logger.INSTANCE.logFATAL("having problem calling data putter", "dashboard", "put");
        }
    }
}

