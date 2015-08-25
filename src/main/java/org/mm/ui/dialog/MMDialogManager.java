package org.mm.ui.dialog;

import java.awt.Component;

import javax.swing.JFileChooser;

/*
 * See https://github.com/protegeproject/swrlapi/blob/master/src/main/java/org/swrlapi/factory/DefaultSWRLAPIDialogManager.java
 * for a potential implementation.
 */
public interface MMDialogManager
{
	int showConfirmDialog(Component parent, String title, String message);

	void showMessageDialog(Component parent, String message);

	void showErrorMessageDialog(Component parent, String message);

	JFileChooser createOpenFileChooser(String message, String fileExtension, String fileDescription);

	JFileChooser createSaveFileChooser(String message, String fileExtension, String fileDescription, boolean overwrite);
}
