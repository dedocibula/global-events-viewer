package edu.vt.dlrl.controller;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.TermSelection;
import edu.vt.dlrl.service.GlobalEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView index() {
        return new ModelAndView("index", "maxDateRange", service.getMaxDateRange());
    }

    @RequestMapping(value = "/mentions/{term}", method = RequestMethod.GET)
    public ModelAndView mentions(@PathVariable String term,
                           @RequestParam("from") int fromDate,
                           @RequestParam("to") int toDate,
                           @RequestParam(value = "eventIds[]", required = false) Set<String> eventIds) {
        eventIds = eventIds != null ? eventIds : Collections.<String>emptySet();
        return new ModelAndView("mentions", "mentions", service.getTermMentions(term, createDateRange(fromDate, toDate), eventIds));
    }

    @RequestMapping(value = "/terms-selection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TermSelection> termsSelection(
            @RequestParam("from") int fromDate,
            @RequestParam("to") int toDate,
            @RequestParam("count") int topK,
            @RequestParam(value = "eventIds[]", required = false) Set<String> eventIds) {
        eventIds = eventIds != null ? eventIds : Collections.<String>emptySet();
        return new ResponseEntity<>(service.getTermSelection(createDateRange(fromDate, toDate), topK, eventIds), HttpStatus.OK);
    }

    private DateRange createDateRange(int fromDate, int toDate) {
        Calendar from = Calendar.getInstance();
        from.set(fromDate, Calendar.JANUARY, 1);
        Calendar to = Calendar.getInstance();
        to.set(toDate, Calendar.JANUARY, 1);
        return new DateRange(from.getTime(), to.getTime());
    }
}
