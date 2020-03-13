package base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import annotation.Column;
import helpers.ListHelpers;

class Delete implements Function<String, String> {
	protected String table;
	protected Map<String, Value> whereMap;
	
	Delete(String table) {
		this.table = table;
		this.whereMap = new HashMap<>();
	}

	public void putWhere(Field f, Object model) throws IllegalArgumentException, IllegalAccessException {
		String key = f.getName();
		Object value = f.get(model);
		int type = f.getAnnotation(Column.class).type().sqlType();
		this.whereMap.put(key, new Value(value, type));
	}

	public Value[] where() {
		return this.whereMap.values().toArray(new Value[this.whereMap.size()]);
	}

	@Override
	public String toString() {
		if (this.whereMap.isEmpty()) {
			return "";
		}
		Object[] where = this.whereMap.keySet().stream().map(this).toArray();
		return String.format("DELETE FROM %s WHERE %s", this.table, ListHelpers.join(where));
	}
	
	@Override
	public String apply(String t) {
		return t + "=?";
	}
}
