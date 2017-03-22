package org.springframework.data.mapdb.repository;

import java.io.Serializable;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MapDbRepository<T extends Serializable, ID extends Serializable> extends KeyValueRepository<T, ID> {

}
