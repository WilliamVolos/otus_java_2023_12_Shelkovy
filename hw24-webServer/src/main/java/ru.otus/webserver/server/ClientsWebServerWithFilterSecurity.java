package ru.otus.webserver.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.service.DBServiceClient;
import ru.otus.webserver.servlet.AuthorizationFilter;
import ru.otus.webserver.servlet.LoginServlet;
import ru.otus.webserver.services.TemplateProcessor;
import ru.otus.webserver.services.UserAuthService;

import java.util.Arrays;

public class ClientsWebServerWithFilterSecurity extends ClientsWebServerSimple {
    private final UserAuthService authService;

    public ClientsWebServerWithFilterSecurity(
            int port, UserAuthService authService, Gson gson, TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        super(port, gson, templateProcessor, dbServiceClient);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
