package io.rainfall.store.dataset;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
interface ChildRepository<CR extends ChildRecord<?, ?>> extends RecordRepository<CR> {

  List<CR> findByParentId(long parentId);
}
