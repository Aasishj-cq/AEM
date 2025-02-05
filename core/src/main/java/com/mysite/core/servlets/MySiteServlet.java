package com.mysite.core.servlets;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

/*
* A Sling Servlet in Adobe Experience Manager (AEM) is a Java-based server-side component that handles HTTP requests (GET, POST, etc.)
* within the AEM environment. Types of Sling Servlets
* Path-Based Servlets (sling.servlet.paths) = Registered at a specific URL path.
* Resource Type-Based Servlets (sling.servlet.resourceTypes) = Tied to a specific AEM component’s resource type.
* Used for component-specific logic.
* Extension-Based Servlets (sling.servlet.extensions) = Triggered based on the request file extension (e.g., .json, .xml).
*
* @Component  - registers this as an OSGI service
* sling.servlet.methods=GET,POST – Handles both GET & POST requests.
* sling.servlet.paths=/bin/sample-servlet – Makes it accessible at /bin/sample-servlet.
* sling.servlet.extensions=json/xml/txt – Responds to .json requests.
* */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.methods=POST",
                "sling.servlet.paths=/bin/sample-servlet",
                "sling.servlet.extensions=json"
        }
)
/* SlingSafeMethodsServlet = supports GET, HEAD, OPTIONS http methods
*  SlingAllMethodsServlet = supports all HTTP methods GET, POST, PUT, DELETE, HEAD, OPTIONS. Extending
*  this you need to override doGet(), doPost(), doPut() etc
* */
public class MySiteServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MySiteServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOG.info("Received GET request on path {}",request.getPathInfo());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("message", "Hello from AEM Sling Servlet!");

        response.getWriter().write(jsonResponse.toString());
    }
}