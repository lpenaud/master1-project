package helpers;

import java.util.Iterator;
import java.util.List;

public class ListHelpers {
	public static String join(Object[] elements, String sep) {
		if (elements == null) {
			return null;
		}
		if (elements.length == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder(elements[0].toString());
		for (int i = 1; i < elements.length; i++) {
			builder.append(sep);
			builder.append(elements[i]);
		}
		return builder.toString();
	}
	
	public static String join(Object[] elements) {
		return join(elements, ",");
	}
	
	public static String join(List<?> elements, String sep) {
		if (elements == null) {
			return null;
		}
		if (elements.size() == 0) {
			return "";
		}
		Iterator<?> it = elements.iterator();
		StringBuilder builder = new StringBuilder(it.next().toString());
		while (it.hasNext()) {
			builder.append(sep);
			builder.append(it.next());
		}
		return builder.toString();
	}
	
	public static String join(List<?> elements) {
		return join(elements, ",");
	}
}
