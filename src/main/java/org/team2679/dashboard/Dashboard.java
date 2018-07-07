package org.team2679.dashboard;

import org.team2679.dashboard.Views.Index;
import org.team2679.dashboard.Views.View;
import org.team2679.dashboard.WebSockets.DashboardVariables;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

public class Dashboard {

    final static int PORT = 2679;

    public static void init(){
        port(PORT);

        webSocket("/socket/coolandgood", DashboardVariables.class);

        get("/script/:file", (req, res) -> {
            res.type("text/javascript");
            return loadResource("script/" + req.params(":file"));
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

        // register all routes
        registerView(new Index());
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

    public static void main(String[] args) {
        init();
    }
}

