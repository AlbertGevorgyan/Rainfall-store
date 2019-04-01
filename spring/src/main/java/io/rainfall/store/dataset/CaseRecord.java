package io.rainfall.store.dataset;

import io.rainfall.store.values.Case;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Entity
@Table(name = "test_case")
@AttributeOverrides({
        @AttributeOverride(name = "value.name", column = @Column(unique = true)),
        @AttributeOverride(name = "value.description", column = @Column(length = 1024))
})
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CaseRecord extends Record<Case> {

  CaseRecord(Case value) {
    super(value);
  }

  @OneToMany(
          mappedBy = "parent",
          fetch = FetchType.LAZY,
          cascade = CascadeType.ALL
  )
  private List<RunRecord> runs = new CopyOnWriteArrayList<>();

  public List<RunRecord> getRuns() {
    return Collections.unmodifiableList(runs);
  }

  void addRun(RunRecord run) {
    runs.add(run);
  }
}
