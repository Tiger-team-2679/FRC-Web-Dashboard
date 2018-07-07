package org.team2679.dashboard.Views;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.team2679.dashboard.Dashboard;
import spark.Request;
import spark.Response;

public class Index implements View {
    @Override
    public String address(){
        return "/";
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        JtwigTemplate template = JtwigTemplate.inlineTemplate(Dashboard.loadResource("templates/hello.html"));
        JtwigModel model = JtwigModel.newModel().with("var", "World");

        return template.render(model);
    }
}
