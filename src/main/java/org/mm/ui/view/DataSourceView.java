package org.mm.ui.view;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.mm.exceptions.MappingMasterException;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.DataSourceModel;
import org.mm.ui.model.MMApplicationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataSourceView extends JPanel implements MMView
{
  private MMApplication application; // Use Optional to get rid of null.

  private final Map<String, SheetView> sheetViewMaps;
  private final JTextField fileNameTextField;
  private final JTabbedPane tabbedPane;

  public DataSourceView(MMApplication application)
  {
    this.application = application;
    getDataSourceModel().setView(this);

    this.sheetViewMaps = new HashMap<>();

    JPanel footerPanel, buttonPanel;
    JButton openButton, closeButton;

    setLayout(new BorderLayout());

    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Workbook"));

    this.tabbedPane = new JTabbedPane();

    footerPanel = new JPanel(new BorderLayout());
    footerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Workbook File"));
    add(footerPanel, BorderLayout.SOUTH);

    this.fileNameTextField = createTextField("");
    this.fileNameTextField.setEnabled(true);
    footerPanel.add(this.fileNameTextField, BorderLayout.CENTER);

    buttonPanel = new JPanel(new BorderLayout());
    footerPanel.add(buttonPanel, BorderLayout.EAST);

    openButton = new JButton("Open");
    openButton.addActionListener(new OpenWorkbookAction());
    buttonPanel.add(openButton, BorderLayout.WEST);

    closeButton = new JButton("Close");
    closeButton.addActionListener(new CloseWorkbookAction());
    buttonPanel.add(closeButton, BorderLayout.EAST);

    add(this.tabbedPane, BorderLayout.CENTER);

    validate();
  }

  public void setApplicationModel(MMApplication application)
  {
    this.application = application;
    getDataSourceModel().setView(this);

    update();
  }

  public void clearModel()
  {
    this.application = null;

    update();
  }

  public void update()
  {
    this.sheetViewMaps.clear();
    this.tabbedPane.removeAll();

    if (this.application != null && getDataSourceModel().hasDataSource()) {
      int i = 0;
      for (Sheet sheet : getDataSourceModel().getDataSource().getSheets()) {
        SheetView sheetView = new SheetView(sheet);
        String sheetName = sheet.getName();
        this.tabbedPane.addTab(sheetName, null, sheetView, "Sheet '" + sheetName + "'");
        this.tabbedPane.setForegroundAt(i, sheet.getSettings().isHidden() ? Color.GRAY : Color.BLACK);
        this.sheetViewMaps.put(sheetName, sheetView);
        i++;
      }
    }

    if (getDataSourceModel().hasFileName())
      this.fileNameTextField.setText(getDataSourceModel().getFileName());
    else
      this.fileNameTextField.setText("");

    validate();
  }

  private class OpenWorkbookAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      JFileChooser fileChooser = getApplicationDialogManager().createFileChooser("Open Data Source", "xls");
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String fileName = file.getAbsolutePath();

        try {
          Workbook workbook = Workbook.getWorkbook(file);
          SpreadSheetDataSource dataSource = new SpreadSheetDataSource(workbook);
          getDataSourceModel().setDataSource(dataSource);
          getDataSourceModel().setFileName(fileName);
          getApplicationModel().dataSourceUpdated();
        } catch (MappingMasterException | IOException | BiffException ex) {
          getApplicationDialogManager()
            .showErrorMessageDialog(DataSourceView.this.tabbedPane, "error opening file '" + fileName + "': " + ex.getMessage());
        }
      }
    }
  }

  private class CloseWorkbookAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      if (getDataSourceModel().hasDataSource() && getApplicationDialogManager()
        .showConfirmDialog(DataSourceView.this.tabbedPane, "Close Data Source", "Do you really want to close the data source?")) {
        getDataSourceModel().clearDataSource();
        getDataSourceModel().clearFileName();
      }
    }
  }

  private JTextField createTextField(String text)
  {
    JTextField textField = new JTextField(text);
    textField.setPreferredSize(new Dimension(80, 30));
    return textField;
  }

  private MMApplicationModel getApplicationModel() { return this.application.getApplicationModel(); }

  private DataSourceModel getDataSourceModel() { return this.application.getApplicationModel().getDataSourceModel(); }

  private MMApplicationView getApplicationView() { return this.application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}
