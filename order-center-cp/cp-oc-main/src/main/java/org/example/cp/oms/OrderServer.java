package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * 完整演示的入口：在IDE下启动：会在本机启动Jetty server.
 *
 * HTTP Controller入口在cp-oc-controller module 的 OrderController
 *
 * 下单：
 * curl -XPOST localhost:9090/order
 *
 * 查看日志，了解服务端的执行过程.
 */
@Slf4j
public class OrderServer {
    private static final int DEFAULT_PORT = 9090;
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static String config;
    private static final String CONFIG_LOCATION = "org.example.cp.oms.config";
    private static final String PLUGIN_LOCATION = "org.example.cp.oms.plugin";

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        config = CONFIG_LOCATION;
        if (args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignored) {
            }

            if (args.length > 1) {
                // Plugin dynamic loading
                config = PLUGIN_LOCATION;
            }
        }

        new OrderServer().startJetty(port);
    }

    private void startJetty(int port) throws Exception {
        log.info("Starting server at port {}", port);
        Server server = new Server(port);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        log.info("Server started at port {}", port);
        log.info("现在，另起一个终端窗口，模拟下单，执行命令：");
        log.info("curl -XPOST localhost:9090/order");
        server.join();
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(config);
        return context;
    }

}
