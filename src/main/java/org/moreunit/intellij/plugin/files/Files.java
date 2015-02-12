package org.moreunit.intellij.plugin.files;

public class Files {

	private Files() {
		// not instantiable
	}

	public static String withoutExtension(String name) {
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex <= 0) {
			return name;
		}
		return name.substring(0, lastDotIndex);
	}

	public static String withoutLeadingDot(String name) {
		if (name.startsWith(".")) {
			return name.substring(1);
		}
		return name;
	}
}
