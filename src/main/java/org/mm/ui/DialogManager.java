package org.mm.ui;

import java.awt.Component;
import java.io.File;

public interface DialogManager
{
   int showConfirmDialog(Component parent, String title, String message);

   String showInputDialog(Component parent, String message);

   void showMessageDialog(Component parent, String message);

   void showErrorMessageDialog(Component parent, String message);

   File showOpenFileChooser(Component parent, String message, String fileExtension, String fileDescription);

   File showSaveFileChooser(Component parent, String message, String fileExtension, String fileDescription, boolean overwrite);
}
