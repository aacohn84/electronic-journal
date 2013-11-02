package models;

import java.util.Map;

public abstract class RecordManager {
	protected Map<Object, Record> records;

	public Map<Object, Record> getRecords() {
		return records;
	}

	public void add(Object key, Record value) {
		records.put(key, value);
	}

	public Record get(Object key) {
		Record value = records.get(key);
		if (value == null) {
			value = fetchFromDB(key);
			records.put(key, value);
		}
		return value;
	}

	protected abstract Record fetchFromDB(Object key);
}
