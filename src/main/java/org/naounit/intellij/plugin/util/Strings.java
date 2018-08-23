package org.naounit.intellij.plugin.util;

public class Strings {

	private Strings() {
		// no instantiable
	}

	public static String applySameCapitalization(String target, String example) {
		if (target.isEmpty() || example.isEmpty()) {
			return target;
		}

		if (Character.isUpperCase(example.charAt(0))) {
			return capitalize(target);
		}
		return uncapitalize(target);
	}

	public static String capitalize(String str) {
		return str.isEmpty() ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static String uncapitalize(String str) {
		return str.isEmpty() ? str : Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}
}
