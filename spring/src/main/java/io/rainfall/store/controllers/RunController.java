package io.rainfall.store.controllers;

import io.rainfall.store.dataset.RunDataset;
import io.rainfall.store.dataset.RunRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RunController {

  private final RunDataset dataset;

  @Autowired
  RunController(RunDataset dataset) {
    this.dataset = dataset;
  }

  @GetMapping({"/cases/{parentId}/runs"})
  public ModelAndView getRunsByCaseID(ModelMap model, @PathVariable long parentId) {
    List<RunRecord> runs = dataset.findByParentId(parentId);
    model.addAttribute("runs", runs);
    return new ModelAndView("runs", model);
  }

  @GetMapping({"/cases/{parentId}/runs/json"})
  @ResponseBody
  public List<RunRecord> listRunsByCaseID(@PathVariable long parentId) {
    return dataset.findByParentId(parentId);
  }

  @GetMapping({"/runs/{id}"})
  public ModelAndView getRun(ModelMap model, @PathVariable long id) {
    model.addAttribute("run", getRecord(id));
    return new ModelAndView("run", model);
  }

  private RunRecord getRecord(Long id) {
    return dataset.getRecord(id)
            .orElseThrow(() -> new IllegalArgumentException("Run not found: " + id));
  }
}
