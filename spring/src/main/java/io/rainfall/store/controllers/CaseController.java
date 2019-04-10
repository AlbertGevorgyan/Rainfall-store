package io.rainfall.store.controllers;

import io.rainfall.store.dataset.CaseDataset;
import io.rainfall.store.dataset.CaseRecord;
import io.rainfall.store.dataset.RunRecord;
import io.rainfall.store.values.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.SEE_OTHER;

@Controller
@SuppressWarnings("unused")
class CaseController {

  private final CaseDataset dataset;

  @Autowired
  CaseController(CaseDataset dataset) {
    this.dataset = dataset;
  }

  @GetMapping({"/", "/cases"})
  public ModelAndView getCases(ModelMap model) {
    model.addAttribute("cases", dataset.getRecords());
    return new ModelAndView("cases", model);
  }

  @GetMapping({"/cases/{id}"})
  public ModelAndView getCase(ModelMap model, @PathVariable long id) {
    model.addAttribute("case", getRecord(id));
    return new ModelAndView("case", model);
  }

  @PostMapping({"/cases"})
  public ResponseEntity<?> postCase(String name, String description) {
    Case value = Case.builder()
            .name(name)
            .description(description)
            .build();
    dataset.save(value);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{name}")
            .buildAndExpand(name)
            .toUri();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setLocation(location);
    return new ResponseEntity<>(null, responseHeaders, SEE_OTHER);
  }

  private CaseRecord getRecord(Long id) {
    return dataset.getRecord(id)
            .orElseThrow(() -> new IllegalArgumentException("Test not found: " + id));
  }
}
