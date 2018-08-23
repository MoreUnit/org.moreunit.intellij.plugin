package org.naounit.intellij.plugin.fixtures;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.vfs.VirtualFile;

public abstract class JumpActionHandlerTestCase extends NaoUnitTestCase {

	protected void assertTargetFilesInOrder(GotoTargetHandler.GotoData data, VirtualFile... files) {
		assertEquals(files.length, data.targets.length);
		for (int i = 0; i < files.length; i++) {
			assertEquals(files[i], data.targets[i].getContainingFile().getVirtualFile());
		}
	}
}
