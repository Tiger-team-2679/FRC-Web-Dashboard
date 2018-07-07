package org.team2679.dashboard.Views;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.team2679.dashboard.Dashboard;
import spark.Request;
import spark.Response;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Index implements View {
    @Override
    public String address(){
        return "/";
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map model = new HashMap();
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template t = new Template("templateName", new StringReader(Dashboard.loadResource("templates/hello.html")), cfg);

        Writer out = new StringWriter();
        t.process(model, out);
        return out.toString();
    }
}
