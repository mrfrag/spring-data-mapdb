package org.springframework.data.mapdb.repository;

import java.io.Serializable;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;

public class SimpleMapDbRepository<T extends Serializable, ID extends Serializable> extends SimpleKeyValueRepository<T, ID> implements MapDbRepository<T, ID> {

	public SimpleMapDbRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations) {
		super(metadata, operations);
	}
}
