package com.tacs.rest.controller;

import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}")
public class ReportRestController {

    @GetMapping("/report")
    // add produces = MediaType.APPLICATION_JSON_VALUE when done
    public String getReport(@PathVariable("userId") int userId, @RequestParam int baseCountryId) {
        // User user = userService.findById(userId);
        // countries = user.suscribedCountries(); --> validate that baseCountryId belongs to the suscribedCountries
        // call the external api for each countries element.
        // do the correct mapping country/days(date, infected, dead, recovered)/offset (based on the baseCountryId first date)
        // return the json to front end

        //		MOCK
        User user = new User();
        user.setId(userId);
        user.setFirstName("Rick");
        user.setLastName("Sanchez");

        Country argentina = new Country();
        argentina.setId(baseCountryId);
        argentina.setName("Argentina");

        // add country to users list.

        return String.format("%s's report with %s as base for offset", user.getFirstName(), argentina.getName());
    }
}
