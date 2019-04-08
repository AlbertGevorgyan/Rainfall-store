package io.rainfall.store.values;

import lombok.*;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Run {

  public enum Status {
    UNKNOWN, INCOMPLETE, COMPLETE, FAILED
  }

  @Builder.Default
  private final String version = null;

  @Builder.Default
  private final String className = null;

  @Builder.Default
  private final String checksum = null;

  @Builder.Default
  private final Status status = Status.UNKNOWN;

  @Builder.Default
  private final boolean baseline = false;
}
