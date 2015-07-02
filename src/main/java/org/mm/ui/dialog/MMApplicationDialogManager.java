package org.mm.ui.dialog;

import org.mm.ui.MMApplication;
import org.mm.core.MappingExpression;

import javax.swing.*;
import java.awt.*;

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
