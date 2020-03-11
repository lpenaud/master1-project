package helpers;

public class ListHelpers {
	public static <T> String join(T[] elements, String sep) {
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
	
	public static <T> String join(T[] elements) {
		return join(elements, ",");
	}
}
