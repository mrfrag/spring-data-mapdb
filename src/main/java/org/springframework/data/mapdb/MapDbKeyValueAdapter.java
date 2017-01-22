/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mapdb;

import java.io.Serializable;
import java.util.Map.Entry;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

public class MapDbKeyValueAdapter extends AbstractKeyValueAdapter {

	private DB mapDb;

	public MapDbKeyValueAdapter() {
		this(DBMaker.memoryDB().closeOnJvmShutdown().make());
	}

	public MapDbKeyValueAdapter(DB mapDb) {
		super(new MapDbQueryEngine());
		Assert.notNull(mapDb, "hzInstance must not be 'null'.");
		this.mapDb = mapDb;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Serializable id, Object item, Serializable keyspace) {
		return getMap(keyspace).put(id, item);
	}

	@Override
	public boolean contains(Serializable id, Serializable keyspace) {
		return getMap(keyspace).containsKey(id);
	}

	@Override
	public Object get(Serializable id, Serializable keyspace) {
		return getMap(keyspace).get(id);
	}

	@Override
	public Object delete(Serializable id, Serializable keyspace) {
		return getMap(keyspace).remove(id);
	}

	@Override
	public Iterable<?> getAllOf(Serializable keyspace) {
		return getMap(keyspace).getEntries();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CloseableIterator<Entry<Serializable, Object>> entries(Serializable keyspace) {
		return new ForwardingCloseableIterator<Entry<Serializable, Object>>(getMap(keyspace).entryIterator());
	}

	@Override
	public void deleteAllOf(Serializable keyspace) {
		getMap(keyspace).clear();
	}

	@Override
	public void clear() {
		mapDb.getAllNames().forEach((name) -> deleteAllOf(name));
	}

	@Override
	public long count(Serializable keyspace) {
		return getMap(keyspace).size();
	}

	@SuppressWarnings("rawtypes")
	public BTreeMap getMap(Serializable keyspace) {
		Assert.isInstanceOf(String.class, keyspace, "Keyspace identifier must be of type String.");
		return mapDb.treeMap((String) keyspace).counterEnable().createOrOpen();
	}

	@Override
	public void destroy() throws Exception {
		mapDb.close();
	}

}
