package org.moreunit.intellij.plugin.files;

public class Files {

	private Files() {
		// not instantiable
	}

	public static String withoutExtension(String name) {
		int lastDotIndex = name.lastIndexOf('.');
		return lastDotIndex == -1 ? name : name.substring(0, lastDotIndex);
	}
}
