package com.tacs.rest;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.security.JWTAuthorizationFilter;
import com.tacs.rest.util.IsoUtil;
import com.tacs.rest.util.ParseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RestApplication {

    public static HashMap<String, Object> data = new HashMap<String, Object>();


    public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
        RestApplication.initData();
        /** Telegram BOT API init */
        ApiContextInitializer.init();
        SpringApplication.run(RestApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }
    
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
//    
    //Esto de abajo hace que todas las consultas que hagamos requieran el token como authentication menos
    //la del /session
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {


		@Autowired
		AuthenticationEntryPoint jwtAuthenticationEntryPoint;
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/session").permitAll()
				.antMatchers(HttpMethod.POST, "/users").permitAll()
				.anyRequest().authenticated().and().
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

    @SuppressWarnings("unchecked")
    public static void initData() throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("/data/data.json"));
        } catch (FileNotFoundException e) {
            try {
                obj = parser.parse(new FileReader("src/main/resources/data/data.json"));
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        


        ConnectionApiCovid apiCovid = new ConnectionApiCovid();
        Gson gson = new Gson();

        java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>() {
        }.getType();
        Collection<Covid19_latestResponse> latestResponse;
        List<Country> listCountries = new ArrayList<Country>();

        IsoUtil iu = new IsoUtil();
        iu.agregarPaises();

        JSONObject jsonObject = (JSONObject) obj;
        Iterator<JSONObject> iterator;

        try {
            latestResponse = gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);


            int id = 1;
            for (Covid19_latestResponse response : latestResponse) {
                Country country = ParseUtil.latestResponseToCountry(response);
                country.setId(id);
                listCountries.add(country);
                id++;
            }
            RestApplication.data.put("Countries", listCountries);


            JSONArray countriesList = (JSONArray) jsonObject.get("countriesList");
            List<CountriesList> listCountriesList = new ArrayList<CountriesList>();
            iterator = countriesList.iterator();
            while (iterator.hasNext()) {
                listCountriesList.add(ParseUtil.parseJsonToCountryList(iterator.next()));
            }
            RestApplication.data.put("CountriesList", listCountriesList);

            JSONArray usersList = (JSONArray) jsonObject.get("users");
            List<User> listUsers = new ArrayList<User>();
            iterator = usersList.iterator();
            while (iterator.hasNext()) {
                listUsers.add(ParseUtil.parseJsonToUser(iterator.next()));
            }

            RestApplication.data.put("Users", listUsers);

            String uri = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/timeseries";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            JSONArray json = (JSONArray) parser.parse(result);

            List<Country> countriesTimeSeries = new ArrayList<Country>();
            for (int i = 0; i < json.size(); i++) {
                JSONObject object = (JSONObject) json.get(i);
                countriesTimeSeries.add(ParseUtil.parseJsonToCountryTimeSeries(object));
            }

            RestApplication.data.put("CountriesTimeSeries", countriesTimeSeries);

        } catch (UnknownHostException ex) {
            System.out.println("-------------------------ERROR AL CONECTAR CON LA API COVID 19-------------------");
            System.out.println("-------------------------VOLVIENDO A CONECTAR EN 10 SEGUNDOS-----------------------");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String[] args = new String[0];
            RestApplication.main(args);
        } catch (Exception e) {
            System.out.println("Error al levantar la aplicación. Error: ");
            e.printStackTrace();
        }

    }

}
