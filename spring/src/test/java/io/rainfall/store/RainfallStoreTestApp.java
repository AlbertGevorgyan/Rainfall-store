package io.rainfall.store;

import io.rainfall.store.data.Payload;
import io.rainfall.store.dataset.CaseDataset;
import io.rainfall.store.dataset.CaseRecord;
import io.rainfall.store.dataset.JobDataset;
import io.rainfall.store.dataset.MonitorLogDataset;
import io.rainfall.store.dataset.OutputLogDataset;
import io.rainfall.store.dataset.OutputLogRecord;
import io.rainfall.store.dataset.Record;
import io.rainfall.store.dataset.RunDataset;
import io.rainfall.store.dataset.Utils;
import io.rainfall.store.values.Case;
import io.rainfall.store.values.Job;
import io.rainfall.store.values.MonitorLog;
import io.rainfall.store.values.OutputLog;
import io.rainfall.store.values.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Optional;

import static io.rainfall.store.values.Run.Status.COMPLETE;

@SpringBootApplication
@SuppressWarnings("unused")
public class RainfallStoreTestApp {

  @Autowired
  private CaseDataset caseDataset;

  @Autowired
  private RunDataset runDataset;

  @Autowired
  private MonitorLogDataset monitorLogDataset;

  @Autowired
  private JobDataset jobDataset;

  @Autowired
  private OutputLogDataset outputLogDataset;

  public static void main(String[] args) {
    SpringApplication.run(RainfallStoreApp.class, args);
  }

  @Bean
  CommandLineRunner runner() {
    return args -> addData();
  }

  private void addData() throws IOException {
    Case test1 = Case.builder()
            .name("Test1")
            .description("Some test")
            .build();
    caseDataset.save(test1);

    Case test2 = Case.builder()
            .name("Test2")
            .build();
    CaseRecord caseRecord = caseDataset.save(test2);
    caseDataset.setDescription(caseRecord.getId(), "Updated");

    Run run = Run.builder().build();
    long runId = runDataset.save(caseRecord.getId(), run).getId();
    runDataset.setStatus(runId, COMPLETE);
    runDataset.setBaseline(runId, true);
    Long lastBaselineID = runDataset.getLastBaselineID(caseRecord.getId())
            .orElse(null);

    Payload payload = Utils.readBytes("150.hlog");

    MonitorLog log = MonitorLog.builder()
            .host("localhost")
            .type("vmstat")
            .payload(payload)
            .build();
    long logId = monitorLogDataset.save(runId, log).getId();

    Job job = Job.builder()
            .clientNumber(1)
            .host("localhost")
            .symbolicName("localhost-1")
            .details("details")
            .build();
    long jobId = jobDataset.save(runId, job).getId();

    OutputLog outputLog = OutputLog.builder()
            .operation("MISS")
            .format("hlog")
            .payload(payload)
            .build();
    long outputLogId = outputLogDataset.save(jobId, outputLog).getId();

    Case testSaved = caseDataset.findByName("Test2")
            .map(Record::getValue)
            .orElse(null);
    Run runSaved = runDataset.getRecord(runId)
            .map(Record::getValue)
            .orElse(null);
    MonitorLog logSaved = monitorLogDataset.getRecord(logId)
            .map(Record::getValue)
            .orElse(null);
    Job jobSaved = jobDataset.getRecord(jobId)
            .map(Record::getValue)
            .orElse(null);

    Optional<OutputLogRecord> outputLogRecord = outputLogDataset.getRecord(outputLogId);
    Optional<OutputLog> outputLogFound = outputLogRecord
            .map(Record::getValue);
    OutputLog outputLogSaved = outputLogRecord
            .map(Record::getValue)
            .orElse(null);
    Payload payloadSaved = outputLogRecord
            .map(OutputLogRecord::getPayloadRecord)
            .map(Record::getValue)
            .orElse(null);

    System.out.println("TEST: " + testSaved);
    System.out.println("RUN: " + runSaved);
    System.out.println("LAST BASELINE: " +
            lastBaselineID);
    System.out.println("MONITOR LOG: " + logSaved);
    System.out.println("JOB: " + logSaved);
    System.out.println("OUTPUT LOG: " + outputLogSaved);
    System.out.println("OUTPUT LOG PAYLOAD: " + payloadSaved);
  }
}
