package io.rainfall.store.dataset;

import org.springframework.data.jpa.repository.JpaRepository;

interface RecordRepository<R extends Record<?>> extends JpaRepository<R, Long> {

  @Override
  default <S extends R> S save(S s) {
    return saveAndFlush(s);
  }
}
