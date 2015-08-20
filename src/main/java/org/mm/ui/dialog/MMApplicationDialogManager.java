package org.mm.ui.dialog;

import org.mm.ui.MMApplication;
import org.mm.core.MappingExpression;

import javax.swing.*;
import java.awt.*;

/*
 * See https://github.com/protegeproject/swrlapi/blob/master/src/main/java/org/swrlapi/factory/DefaultSWRLAPIDialogManager.java
 * for a potential implementation.
 */
public interface MMApplicationDialogManager
{
	void initialize(MMApplication application);

	JDialog getCreateMappingExpressionDialog();

	JDialog getCreateMappingExpressionDialog(MappingExpression mappingExpression);

	boolean showConfirmDialog(Component parent, String title, String message);

	void showMessageDialog(Component parent, String message);

	void showErrorMessageDialog(Component parent, String message);

	JFileChooser createOpenFileChooser(String message, String extension);

	JFileChooser createSaveFileChooser(String message, String extension, boolean overwrite);
}
