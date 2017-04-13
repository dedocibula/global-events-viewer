package edu.vt.dlrl.controller;

import edu.vt.dlrl.domain.TermSelection;
import edu.vt.dlrl.service.GlobalEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@Controller
public class MainController {

    private final GlobalEventsService service;

    @Autowired
    public MainController(GlobalEventsService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/term-frequencies",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TermSelection> termFrequencies(
            @RequestParam("from") int fromDate,
            @RequestParam("to") int toDate,
            @RequestParam("count") int topK,
            @RequestParam(value = "eventIds", required = false) Set<String> eventIds) {
        Calendar from = Calendar.getInstance();
        from.set(fromDate, Calendar.JANUARY, 1);
        Calendar to = Calendar.getInstance();
        to.set(toDate, Calendar.JANUARY, 1);
        eventIds = eventIds != null ? eventIds : Collections.<String>emptySet();
        return new ResponseEntity<>(service.getTermSelection(from.getTime(), to.getTime(), topK, eventIds), HttpStatus.OK);
    }
}
