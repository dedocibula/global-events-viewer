package edu.vt.dlrl.controller;

import edu.vt.dlrl.domain.TermFrequency;
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
import java.util.List;

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
    public ResponseEntity<List<TermFrequency>> termFrequencies(
            @RequestParam("from") int fromDate,
            @RequestParam("to") int toDate,
            @RequestParam("count") int topK) {
        Calendar from = Calendar.getInstance();
        from.set(fromDate, Calendar.JANUARY, 0);
        Calendar to = Calendar.getInstance();
        to.set(toDate, Calendar.JANUARY, 0);
        return new ResponseEntity<>(service.loadTermFrequencies(from.getTime(), to.getTime(), topK), HttpStatus.OK);
    }
}
