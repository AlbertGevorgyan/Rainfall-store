package io.rainfall.store.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rainfall.store.dataset.CaseDataset;
import io.rainfall.store.dataset.Record;
import io.rainfall.store.dataset.RunDataset;
import io.rainfall.store.dataset.RunRecord;
import io.rainfall.store.values.Case;
import io.rainfall.store.values.Run;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static io.rainfall.store.values.Run.Status.INCOMPLETE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RunControllerIT extends ControllerIT {

  @Autowired
  private CaseDataset caseDataset;

  @Autowired
  private RunDataset runDataset;

  private final Run run = Run.builder()
          .status(INCOMPLETE)
          .version("1.1.1.1")
          .baseline(false)
          .className("my.Class")
          .build();

  private long caseId;
  private long runId;

  @Before
  @Override
  public void setup() {
    super.setup();
    Case testCase = Case.builder()
            .name("Test1")
            .description("Some test")
            .build();
    caseId = caseDataset.save(testCase).getId();
    runId = runDataset.save(caseId, run).getId();
  }

  @Transactional
  @Test
  public void testGetRunsByCaseID() throws Exception {
    String url = format("/cases/%d/runs", caseId);
    mvc.perform(get(url))
            .andExpect(status().isOk())
            .andExpect(content().contentType(DEFAULT_TEXT_HTML))
            .andExpect(content().string(containsAll(
                    run.getVersion(),
                    run.getClassName(),
                    run.getChecksum(),
                    run.getStatus().name())
            ));
  }


  @Transactional
  @Test
  public void testListRunsByCaseID() throws Exception {
    String url = format("/cases/%d/runs/json", caseId);
    String json = mvc.perform(get(url))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<RunRecord> records = new ObjectMapper()
            .readValue(json, new TypeReference<List<RunRecord>>(){});
    Set<Run> runs = records.stream()
            .map(Record::getValue)
            .collect(toSet());
    assertThat(runs, contains(run));
  }

  @Transactional
  @Test
  public void testGetRun() throws Exception {
    mvc.perform(get("/runs/" + runId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(DEFAULT_TEXT_HTML))
            .andExpect(content().string(containsAll(
                    "Test1",
                    run.getVersion(),
                    run.getClassName(),
                    run.getChecksum(),
                    run.getStatus().name())
            ));
  }
}
