package org.mm.ui.dialog;

import org.mm.ui.MMApplication;
import org.mm.core.MappingExpression;

import javax.swing.*;
import java.awt.*;

// TODO No implementation in this project (previous implementation was Protege 3-specific).
// See https://github.com/protegeproject/swrlapi/blob/master/src/main/java/org/swrlapi/factory/DefaultSWRLAPIDialogManager.java
// for a potential implementation.
public interface MMApplicationDialogManager
{
  void initialize(MMApplication application);

  JDialog getCreateMappingExpressionDialog();

  JDialog getCreateMappingExpressionDialog(MappingExpression mappingExpression);

  boolean showConfirmDialog(Component component, String title, String message);

  void showMessageDialog(Component component, String message);

  void showErrorMessageDialog(Component component, String message);

  void showErrorMessageDialog(String message);

  JFileChooser createFileChooser(String message, String extension);

  JFileChooser createSaveFileChooser(String message, String extension, boolean overwrite);
}
