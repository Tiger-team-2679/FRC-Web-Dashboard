package org.team2679.dashboard;

import org.team2679.dashboard.Views.Index;
import org.team2679.dashboard.Views.View;
import org.team2679.dashboard.WebSockets.DashboardVariables;
import org.team2679.dashboard.WebSockets.LoggerSocket;
import org.team2679.util.log.Logger;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

public class Dashboard {

    final static int PORT = 2679;

    public static void init(){
        Logger.INSTANCE.logRobotInit();
        Logger.INSTANCE.logRobotSetup();
        Logger.INSTANCE.logThrowException(new FileNotFoundException(" this is just a test... relax"));

        port(PORT);

        webSocket("/socket/coolandgood", DashboardVariables.class);
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

        // start all sockets
        DashboardVariables.start();
        Logger.INSTANCE.registerHandler(new LoggerSocket());

        // register all routes
        registerView(new Index());

        Thread t = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 10000; i ++){
                    try {
                        DashboardVariables.putNumber("Count", i);
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    catch (Exception e){ }
                }
            }
        };
        t.start();
        DashboardVariables.putBoolean("This is working", true);
        Logger.INSTANCE.logDisableEvent();
    }

    private static void registerView(View view){
        get(view.address(), (req, res) -> view.handle(req, res));
    }

    public static byte[] resourceRaw(String path) {
        try(InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static String loadResource(String path){
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
            return "";
        }
    }

    public static String getResourceLoc(String path){
        try {
            return ClassLoader.getSystemClassLoader().getResource(path).toString();
        }
        catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        init();
    }
}

