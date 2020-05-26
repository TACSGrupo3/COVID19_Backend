package com.tacs.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.http.ContentType;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc 


public class UserTest {
	
	@Autowired
	private WebApplicationContext wac; 
	
	@Autowired
	public MockMvc mockMvc; 
	
	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); 
	}
	
	@Test
	public void noMePermiteNoCargarDatos() throws Exception { 
		MvcResult mvcResult = mockMvc.perform(post("/users")).andReturn();
			     
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void intentoNoIngresarDatoUserPasswordLastNameOFirstName() throws Exception{
		String jsonBodySinUsername = "{\"password\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodySinUsername)
        .post("/api/users")
        .then()
        .statusCode(400)
        .extract()
        .response();
				
		String jsonBodySinPassword = "{\"username\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodySinPassword)
        .post("/api/users")
        .then()
        .statusCode(400)
        .extract()
        .response();
		
		String jsonBodySinFirstName = "{\"username\": \"Raffa\", \"password\": \"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodySinFirstName)
        .post("/api/users")
        .then()
        .statusCode(400)
        .extract()
        .response();
		
		String jsonBodySinPLastName = "{\"username\": \"Raffa\", \"password\": \"Raffa\", \"firstName\":\"Raffa\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodySinPLastName)
        .post("/api/users")
        .then()
        .statusCode(400)
        .extract()
        .response();
		
		String errorDeJsonMalEscrito = "{\"username\": \"Raffa\", \"password\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\",}";
		
		
		given()
		.contentType(ContentType.JSON)
		.body(errorDeJsonMalEscrito)
        .post("/api/users")
        .then()
        .statusCode(400)
        .extract()
        .response();
	}
	
	@Test
	public void ingresoUnUserNuevo() throws Exception { 
		String jsonBody = "{\"username\": \"Raffa\", \"password\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBody)
        .post("/api/users")
        .then()
        .statusCode(200)
        .extract()
        .response();
		
		//no me deja ingresarlo por segunda vez ya que la primera fue guardado
		
		String jsonBodyRepetido = "{\"username\": \"Raffa\", \"password\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBodyRepetido)
        .post("/api/users")
        .then()
        .statusCode(406)
        .extract()
        .response();

	}
	
	@Test 
	public void intengoIngresarUserRepetido() throws Exception{
		String jsonBody = "{\"username\": \"Rufus\", \"password\": \"Raffa\", \"firstName\":\"Raffa\", \"lastName\" : \"Carmaicol\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBody)
        .post("/api/users")
        .then()
        .statusCode(406)
        .extract()
        .response();
	}
	
	@Test
	public void puedoHacerLogInDeUserRecienCreado() throws Exception {
		
		String jsonBody = "{\"username\": \"Raffa\", \"password\": \"Raffa\"}";
		
		given()
		.contentType(ContentType.JSON)
		.body(jsonBody)
        .post("/api/session")
        .then()
        .body("id", equalTo(4))
        .body("username", equalTo("Raffa"))
        .body("firstName", equalTo("Raffa"))
        .body("lastName", equalTo("Carmaicol"))
        .body("password", equalTo("$2a$10$bIVJNF93VFmU2DK8W7vwJe7ye8Hg773lLHN6Ppv3MoVwFHNoyRlEO"))
        .statusCode(200)
        .extract()
        .response();
		
	}

	
}
