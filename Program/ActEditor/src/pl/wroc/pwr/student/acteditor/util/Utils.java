package pl.wroc.pwr.student.acteditor.util;

import java.util.List;

public final class Utils {
	public static String[] convertStringListToArray(List<?> list) {
		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}
}