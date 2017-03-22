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
		mapDb.close();
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
