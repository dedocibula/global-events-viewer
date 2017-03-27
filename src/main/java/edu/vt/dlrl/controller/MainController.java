package edu.vt.dlrl.controller;

import edu.vt.dlrl.dao.GlobalEventsDAO;
import edu.vt.dlrl.domain.WordFrequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@Controller
public class MainController {

    private final GlobalEventsDAO dao;

    @Autowired
    public MainController(@Qualifier("in-memory-dao") GlobalEventsDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/word-frequencies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WordFrequency>> wordFrequencies() {
        return new ResponseEntity<>(dao.getWordFrequencies(), HttpStatus.OK);
    }
}
