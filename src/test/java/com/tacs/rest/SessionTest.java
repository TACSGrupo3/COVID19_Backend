package com.tacs.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import io.restassured.http.ContentType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc //need this in Spring Boot test

public class SessionTest {

	@Autowired
	private WebApplicationContext wac; //proporciona la configuracion de la aplicacion web
	
	@Autowired
	public MockMvc mockMvc; //brinda soporte para pruebas Spring MVC y encapsula todos los beans de la ap web y los pone a disposicion de la prueba 
	
	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); //inicializo mockMvc
	}
	
	@Test
	public void givenWac_whenServletContext_thenItProvidesGreetController() { //verifico configuracion de la prueba
	    ServletContext servletContext = wac.getServletContext();
	     
	    Assert.assertNotNull(servletContext);
	    Assert.assertTrue(servletContext instanceof MockServletContext);
	    Assert.assertNotNull(wac.getBean("sessionRestController"));
	}
	
	@Test
	public void noPermiteAccesoSinParametrosUserYPasswordEnBody() throws Exception { 
		MvcResult mvcResult = mockMvc.perform(post("/session")).andReturn();
			     
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void userYPasswordCorrectosDejaHacerLogInYMuestraDatosCorrectos(){

		String jsonBody = "{\"username\": \"Rufus\", \"password\": \"Rufus\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBody)
        .post("/api/session")
        .then()
        .body("id", equalTo(3))
        .body("username", equalTo("Rufus"))
        .body("firstName", equalTo("Rufus"))
        .body("lastName", equalTo("Carmaicol"))
        .body("password", equalTo("$2a$04$eXiOu5epUEbriIPy6Nck/OCbXQKZO1PLUY6ATQQBlRPGP2SEXdijy"))
        .statusCode(200)
        .extract()
        .response();
	}
	
	@Test
	public void userOPasswordIncorrectosNoPermitenIngreso(){

		//Error en user
		String jsonBodyErrorUser = "{\"username\": \"Raffa\", \"password\": \"Rufus\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodyErrorUser)
        .post("/api/session")
        .then()
        .statusCode(401)
        .extract()
        .response();
		
		//Error en password
		
		String jsonBodyErrorPassword="{\"username\": \"Rufus\", \"password\": \"Raffa\"}";
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodyErrorPassword)
        .post("/api/session")
        .then()
        .statusCode(401)
        .extract()
        .response();
	}
	@Test
	public void camposVaciosNoPermitenAcceso() {
		//No coloco contrasena
		
		String jsonBodyErrorPassword="{\"username\": \"Rufus\", \"password\": \"\"}";
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodyErrorPassword)
        .post("/api/session")
        .then()
        .statusCode(400)
        .extract()
        .response();
	}

	@Test
	public void noColocarCampoPasswordOUserNoDebeDarAcceso() {
		//No coloco campo password ni la password
		
		String jsonBodyErrorPassword="{\"username\": \"Rufus\"}";
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodyErrorPassword)
        .post("/api/session")
        .then()
        .statusCode(400)
        .extract()
        .response();
	

	//No coloco campo user ni el user
	
	String jsonBodyErrorUser="{\"password\": \"\"}";
	
	given()
	.contentType(ContentType.JSON)
	.body(jsonBodyErrorUser)
    .post("/api/session")
    .then()
    .statusCode(400)
    .extract()
    .response();
	
	}
		
}

	


