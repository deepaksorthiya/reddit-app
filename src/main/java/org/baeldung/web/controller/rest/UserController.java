package org.baeldung.web.controller.rest;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private UserRepository userReopsitory;

    @RequestMapping(value = "/user/preference")
    @ResponseBody
    public Preference getUserPreference() {
        Preference pref = getCurrentUser().getPreference();
        if (pref == null) {
            pref = new Preference();
            preferenceReopsitory.save(pref);
            final User user = getCurrentUser();
            user.setPreference(pref);
            userReopsitory.save(user);
        }
        return pref;
    }

    @RequestMapping(value = "/user/preference/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserPreference(@RequestBody final Preference pref) {
        preferenceReopsitory.save(pref);
        final User user = getCurrentUser();
        getCurrentUser().setPreference(pref);
        userReopsitory.save(user);
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}