package org.moreunit.intellij.plugin.fixtures;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiModificationTrackerImpl;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.util.PathUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.jetbrains.annotations.NotNull;
import org.moreunit.intellij.plugin.navigation.ProjectFileEditorHistory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Most of the code comes from CodeInsightTestFixtureImpl and HeavyIdeaTestFixtureImpl.
 */
public abstract class MoreUnitTestCase extends PlatformTestCase {

	static {
		Logger.getRootLogger().addAppender(new NullAppender());
	}

	protected Editor lastOpenedEditor;
	protected VirtualFile lastOpenedFile;
	protected ModuleFacade mainModule;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Module module = getModule();
		createModuleDir(module);
		mainModule = new ModuleFacade(module);
		mainModule.createSourceRoot("src1", false);
		mainModule.createSourceRoot("src2", false);
		mainModule.createSourceRoot("test1", true);
		mainModule.createSourceRoot("test2", true);
	}

	private void createModuleDir(final Module module) throws IOException {
		executeWriteAction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				VirtualFile contentRoot = VfsUtil.createDirectoryIfMissing(getProjectDir(), module.getName());
				PsiTestUtil.addContentRoot(module, contentRoot);
				return null;
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		for (Editor editor : EditorFactory.getInstance().getAllEditors()) {
			EditorFactory.getInstance().releaseEditor(editor);
		}

		super.tearDown();
	}

	private <T> T executeWriteAction(final Callable<T> c) {
		return new WriteCommandAction<T>(getProject()) {
			@Override
			protected void run(@NotNull Result<T> result) throws Throwable {
				try {
					result.setResult(c.call());
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					((PsiModificationTrackerImpl) PsiManager.getInstance(getProject()).getModificationTracker()).incCounter();
				}
			}
		}.execute().getResultObject();
	}

	@NotNull
	protected Editor openFileInEditor(@NotNull VirtualFile file) {
		return openFileInEditor(file, true);
	}

	@NotNull
	protected Editor openFileInEditor(@NotNull VirtualFile file, boolean focusEditor) {
		Project project = getProject();
		PsiDocumentManager.getInstance(project).commitAllDocuments();

		lastOpenedEditor = FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file), focusEditor);
		lastOpenedEditor.getCaretModel().moveToOffset(0);

		DaemonCodeAnalyzer.getInstance(project).restart();

		// we have to fire this event ourselves, since unfortunately TestEditorManagerImpl doesn't fire it
		selectionChanged(file);

		lastOpenedFile = file;
		return lastOpenedEditor;
	}

	private void selectionChanged(VirtualFile file) {
		FileEditorManager editorManager = FileEditorManager.getInstance(getProject());
		FileEditorManagerEvent event = new FileEditorManagerEvent(editorManager, null, null, file, null);

		ProjectFileEditorHistory projectFileEditorHistory = getProject().getComponent(ProjectFileEditorHistory.class);
		projectFileEditorHistory.selectionChanged(event);
	}

	protected void performEditorAction(@NotNull String actionId) {
		final DataContext dataContext = ((EditorEx) lastOpenedEditor).getDataContext();

		final ActionManagerEx managerEx = ActionManagerEx.getInstanceEx();
		final AnAction action = managerEx.getAction(actionId);
		final AnActionEvent event = new AnActionEvent(null, dataContext, ActionPlaces.UNKNOWN, new Presentation(), managerEx, 0);

		executeWriteAction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				action.update(event);

				assert event.getPresentation().isEnabled();

				managerEx.fireBeforeActionPerformed(action, dataContext, event);
				action.actionPerformed(event);
				managerEx.fireAfterActionPerformed(action, dataContext, event);
				return null;
			}
		});
	}

	protected Editor getSelectedEditor() {
		return FileEditorManager.getInstance(getProject()).getSelectedTextEditor();
	}

	protected VirtualFile getEditedFile() {
		return FileDocumentManager.getInstance().getFile(getSelectedEditor().getDocument());
	}

	protected String pathRelativeToProject(VirtualFile file) {
		return VfsUtil.getRelativePath(file, getProjectDir());
	}

	protected VirtualFile getProjectDir() {
		return getProject().getBaseDir();
	}

	protected PsiFile psiFileFor(VirtualFile vFile) {
		return PsiManager.getInstance(getProject()).findFile(vFile);
	}

	public class ModuleFacade {
		private final Map<String, VirtualFile> sourceRoots = new HashMap<String, VirtualFile>();
		private final Module module;

		private ModuleFacade(Module module) {
			this.module = module;
		}

		public VirtualFile getDir() {
			return VfsUtil.findRelativeFile(module.getName(), getProjectDir());
		}

		public VirtualFile findFile(String relativePath) {
			return getDir().findFileByRelativePath(relativePath);
		}

		public String getRelativePath(VirtualFile file) {
			return VfsUtil.getRelativePath(file, getDir());
		}

		public VirtualFile getSourceRoot(String path) {
			return sourceRoots.get(path);
		}

		@NotNull
		public VirtualFile createSourceRoot(final @NotNull String pathRelativeToModule, final boolean testSource) {
			return executeWriteAction(new Callable<VirtualFile>() {
				@Override
				public VirtualFile call() throws Exception {
					VirtualFile dir = VfsUtil.createDirectoryIfMissing(getDir(), pathRelativeToModule);
					PsiTestUtil.addSourceRoot(module, dir, testSource);
					sourceRoots.put(pathRelativeToModule, dir);
					return dir;
				}
			});
		}

		@NotNull
		public VirtualFile addFile(@NotNull String pathRelativeToModule) {
			return addFile(pathRelativeToModule, "");
		}

		@NotNull
		public VirtualFile addFile(final @NotNull String pathRelativeToModule, final @NotNull String fileText) {
			return executeWriteAction(new Callable<VirtualFile>() {
				@Override
				public VirtualFile call() throws Exception {
					VirtualFile dir = VfsUtil.createDirectoryIfMissing(getDir(), PathUtil.getParentPath(pathRelativeToModule));
					VirtualFile file = dir.createChildData(this, StringUtil.getShortName(pathRelativeToModule, '/'));

					VfsUtil.saveText(file, fileText);
					PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
					return file;
				}
			});
		}
	}
}
