package org.naounit.intellij.plugin.navigation;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class ProjectFileEditorHistory extends FileEditorManagerAdapter implements ProjectComponent {

	private final FileEditorHistory history;
	private final MessageBus bus;
	private MessageBusConnection busConnection;

	public ProjectFileEditorHistory(Project ignoredProject, MessageBus bus) {
		this.bus = bus;
		this.history = new FileEditorHistory();
	}

	@Override
	public void initComponent() {
		busConnection = bus.connect();
		busConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
	}

	@Override
	public void disposeComponent() {
		busConnection.disconnect();
	}

	@NotNull
	@Override
	public String getComponentName() {
		return "org.moreunit.navigation.projectFileEditorHistory";
	}

	@Override
	public void selectionChanged(FileEditorManagerEvent event) {
		history.fileFocused(event.getNewFile());
	}

	@Override
	public void projectOpened() {
		// void
	}

	@Override
	public void projectClosed() {
		// void
	}

	public FileEditorHistory getHistory() {
		return history;
	}
}
