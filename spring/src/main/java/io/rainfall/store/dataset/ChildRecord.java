package io.rainfall.store.dataset;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class ChildRecord<V, P extends Record<?>> extends Record<V> {

  ChildRecord(P parent, V value) {
    super(value);
    this.parent = parent;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", nullable = false)
  @Getter
  @NonNull
  private P parent;
}
