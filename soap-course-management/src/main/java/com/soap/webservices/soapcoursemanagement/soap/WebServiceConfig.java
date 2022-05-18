package com.soap.webservices.soapcoursemanagement.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

// I want to tell that this is a configuration file
// and
// Enable Spring Web Services

// @EnableWs we enable spring services
@EnableWs
// @Configuration we tell that this is a configuration file
@Configuration
public class WebServiceConfig /* extends WsConfigurerAdapter*/ { // we extends to WsConfigAd cuz we have to add the
															// XwsSecurityInnterceptor to Interceptors
	// the first thing we want to configure
	// is something called message dispatcher servlet
	// MessageDispatcherServlet:
	// this is a Servlet for dispatching of Web service messages.
	// It handles all the request, all the SOAP request
	// and be able to identify the endpoints and things like that.
	
	// When we are creating a MessageDispatcherServlet
	// we would need to pass a couple of inputs.
	// 1) an Application Context
	// 2) we want to configure an URL to this message servlet
	// The URL we want to use is -> URL -> /ws/*
	
	// We want to define a @Bean to be able to do this
			// now we want to map this message dispatcher servlet to the URL
			// so we want to create a servlet registration.
	// The ServletRegistrationBean helps us to map the servlet to a URI.
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		// we want to map MessageDispatcherServlet to "/ws/*"
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();	// created new messageDisServlet
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
	} // so in this method
	// up here we have defined a servlet to handle all the requests
	// and we have mapped a simple URL to it.
	// So we have created a bean, so as soon I send a request /ws/*
	// it will handle the request and it will start processing it.
	
	// ========================================================================
	
	// So Spring Web Services will create WSDL for us.
	// We will not create WSDL by hand.
	// We want to call this WSDL as: courses.wsdl
	// and we want to expose it to an URL /ws/ => /ws/courses.wsdl
	// This WebService definition is based on the schema => course-details.xsd
	// so we want to use the schema and generate the WSDL for us.
	// And when we configure the WSDL, we will need to configure some stuff..
	// 1) PortType => CoursePort (PortType is something like an interface)
	// 2) Namespace => http://in28minutes.com/courses
	// 3) course-details.xsd is where we want the WSDL to be created
	
	// Let's start to define a Schema
	// The way we would define a Schema is defining another bean
	@Bean // With this Bean we have defined a Schema
	public XsdSchema coursesSchema() {// the way we define a schema is with XsdSchema
		return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
		// SimpleXsdSchema is one the classes of XsdSchema
		// we already know the path of course-details.xsd
		// that it is in src/main/resources so we just put in the ClassPathResource
		// Once we have defined the Schema we want to use it in our WSDL definition
	}
	
	// ==================================================================================
	
	// After we have defined our Schema, How do we use it in our WSDL definition.
	// With DefaultWsdl11Definition we define a WSDL, so we use that
	// we create a bean of that type and return it back.
	// One of the inputs we want to pass in is XsdSchema coursesSchema..
	// SO WHAT WILL HAPPEN IS THAT SPRING WILL CREATE AN INSTANCE OF THIS @Bean
	// AND AUTOWIRES IT TO THE INPUT OF THE PARAMETER WE PASS IN THE METHOD/@Bean,
	// SO AUTOWIRES THE INSTANCE WITH XsdSchema coursesSchema [coursesSchema becomes one of the dependencies]
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema coursesSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		// 1) PortType => CoursePort (PortType is something like an interface)
		definition.setPortTypeName("CoursePort");
		// 2) Namespace => http://in28minutes.com/courses
		definition.setTargetNamespace("http://in28minutes.com/courses");
		// 3) course-details.xsd is where we want the WSDL to be created
		definition.setLocationUri("/ws");
		definition.setSchema(coursesSchema);
		return definition;
	}
	
	// RECAP............
	// FIRST: We have defined a Servlet registration bean basically mapping a
	// dispatcher servlet to the URL
	// and then we have exposed the WSDL definition based on our Schema
	
	// AFTER ALL THESE CONFIGURATIONS
	// WHEN WE RUN THE APPLICATION,
	// TO BE ABLE TO DEFINE A WSDL FILE
	// WE HAVE TO ADD IN THE pom.xml
	// the wsdl4j dependency
	
}
