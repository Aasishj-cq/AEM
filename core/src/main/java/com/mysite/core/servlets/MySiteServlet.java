package com.mysite.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                ServletResolverConstants.SLING_SERVLET_METHODS+"=GET",
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES+"=mysite/getServlet",
                ServletResolverConstants.SLING_SERVLET_EXTENSIONS+"=json"
        }
)
/* SlingSafeMethodsServlet = supports GET, HEAD, OPTIONS http methods
*  SlingAllMethodsServlet = supports all HTTP methods GET, POST, PUT, DELETE, HEAD, OPTIONS. Extending
*  this you need to override doGet(), doPost(), doPut() etc
* */
public class MySiteServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MySiteServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        JsonObject jsonResponse = getJsonResponse(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
        /*
        * response will be
        * {
        *  "message": "Hello from AEM Sling Servlet!",
        *  "data": {
        *    "pageTitle": "My Site"
        *   }
        *  }
        */
    }

    private JsonObject getJsonResponse(SlingHttpServletRequest request) {
        // you can get resource resolver in a servlet from request
        ResourceResolver resolver = request.getResourceResolver();
        ValueMap vm = resolver.getResource("/content/mysite/us/en/jcr:content").getValueMap();
        JsonObject jsonResponse = new JsonObject();
        Map<String, String> dataMap =new HashMap<>();
        dataMap.put("pageTitle",vm.get("pageTitle",""));

        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(dataMap);
        jsonResponse.addProperty("message", "Hello from AEM Sling Servlet!");
        jsonResponse.add("data", jsonElement);
        return jsonResponse;
    }
}