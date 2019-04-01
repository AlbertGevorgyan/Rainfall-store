package io.rainfall.store.dataset;

import io.rainfall.store.RainfallStoreApp;
import io.rainfall.store.values.Case;
import io.rainfall.store.values.Job;
import io.rainfall.store.values.Run;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.rainfall.store.values.Run.Status.INCOMPLETE;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@DataJpaTest
@ComponentScan("io.rainfall.store.dataset")
@ContextConfiguration(classes = RainfallStoreApp.class)
@RunWith(SpringRunner.class)
@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class JobDatasetTest {

  @Autowired
  private CaseDataset caseDataset;

  @Autowired
  private RunDataset runDataset;

  @Autowired
  private JobDataset jobDataset;

  private final Run run = Run.builder()
          .status(INCOMPLETE)
          .version("1.1.1.1")
          .baseline(true)
          .className("my.Class")
          .build();

  @Test
  public void testSaveWithNonExistentParent() {
    Job value = Job.builder()
            .build();
    try {
      jobDataset.save(0L, value);
      fail();
    } catch (Throwable e) {
      assertThat(e, instanceOf(IllegalArgumentException.class));
    }
  }

  @Test
  public void testSave() {
    long parentId = saveParent();

    Job job = Job.builder()
            .clientNumber(1)
            .host("localhost")
            .symbolicName("localhost-1")
            .details("details")
            .build();

    JobRecord childRecord = jobDataset.save(parentId, job);
    assertThat(childRecord.getValue(), is(job));
    assertThat(childRecord.getParent().getValue(), is(run));

    RunRecord parentRecord = jobDataset.getRecord(childRecord.getId())
            .map(ChildRecord::getParent)
            .get();
    assertThat(parentRecord.getValue(), is(run));
    assertThat(parentRecord.getJobs(), contains(childRecord));
  }

  private long saveParent() {
    Case testCase = Case.builder().build();
    long caseId = caseDataset.save(testCase).getId();
    return runDataset.save(caseId, run).getId();
  }
}
