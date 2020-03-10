package base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class PrimaryKeys {
	protected String table;
	protected Collection<String> keys;
	
	PrimaryKeys(String table) {
		this.table = table;
		this.keys = new ArrayList<>();
	}
	
	public void addKey(String key) {
		keys.add(key);
	}
	
	@Override
	public String toString() {
		if (this.keys.size() == 0) {
			return "";
		}
		Iterator<String> it = this.keys.iterator();
		StringBuilder builder = new StringBuilder("CONSTRAINT PK_");
		builder.append(table);
		builder.append(" PRIMARY KEY (");
		builder.append(it.next());
		while (it.hasNext()) {
			builder.append(",");
			builder.append(it.next());
		}
		builder.append(")");
		return builder.toString();
	}
}
