package rest;
import org.apache.camel.builder.RouteBuilder;

import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiResponse;
import pl.com.stream.camel.process.lib.UserPojo;

/**
 * A Camel Java DSL Router
 */
 class MyRouteBuilder extends RouteBuilder {
    public void configure() {
  rest().tag("Demo API")
rest("/user").description("User API")
  .produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
  .skipBindingOnErrorCode(false) //Enable json marshalling for body in case of errors
.get("/{id}")
  //swagger
  .description("Query user")
  .param().name("id").type(RestParamType.path).description("Id of the user").required(true).dataType("string").endParam()
  .responseMessage().code(200).responseModel(UserPojo.class).endResponseMessage() //OK
  .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage() //Not-OK
  //route
  .route().routeId("user-get")
    .log("Get user: \${header.id}")
  .endRest()
.post("/").type(UserPojo.class)
  //swagger
  .description("Send user")
  .responseMessage().code(200).responseModel(ApiResponse.class).endResponseMessage() //OK
  .responseMessage().code(400).responseModel(ApiResponse.class).message("Unexpected body").endResponseMessage() //Wrong input
  .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage() //Not-OK
  //route
  .route().routeId("post-user")
    .log("User received: \${body}")
.endRest();
    }

}

new MyRouteBuilder();