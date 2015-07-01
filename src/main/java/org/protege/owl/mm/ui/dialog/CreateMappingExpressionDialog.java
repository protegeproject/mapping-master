package org.protege.owl.mm.ui.dialog;

import org.protege.owl.mm.MappingExpression;
import org.protege.owl.mm.exceptions.MappingMasterException;
import org.protege.owl.mm.ss.SpreadSheetUtil;
import org.protege.owl.mm.ui.MMApplication;
import org.protege.owl.mm.ui.model.DataSourceModel;
import org.protege.owl.mm.ui.model.MappingsExpressionsModel;
import org.protege.owl.mm.ui.view.MMApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CreateMappingExpressionDialog extends JDialog
{
  private MMApplication application;

  private JLabel commentLabel, sourceSheetNameLabel, startColumnLabel, finishColumnLabel, startRowLabel, finishRowLabel;
  private JTextField commentTextField, startColumnTextField, finishColumnTextField, startRowTextField, finishRowTextField;
  private JTextArea expressionTextArea;
  private JComboBox subSourceNameComboBox;

  private boolean editMode = false;
  private MappingExpression editMappingExpression;

  public CreateMappingExpressionDialog(MMApplication application)
  {
    setTitle("MappingMaster Expression");
    setModal(true);

    this.application = application;

    createComponents();

    setLocationRelativeTo(application.getApplicationView());

    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we) {}
    }); // Thwart user close
  }

  public void reset()
  {
    clearEntryFields();
    for (String subSourceName : getDataSourceModel().getSubSourceNames())
      subSourceNameComboBox.addItem(subSourceName);
  }

  public void setEditMappingExpression(MappingExpression mappingExpression)
  {
    String sourceSheetName = mappingExpression.getSourceSheetName();

    clearEntryFields();

    commentTextField.setText(mappingExpression.getComment());
    expressionTextArea.setText(mappingExpression.getExpression());

    if (getDataSourceModel().hasDataSource()) {
      List<String> subSourceNames = getDataSourceModel().getSubSourceNames();
      for (String sheetName : subSourceNames)
        subSourceNameComboBox.addItem(sheetName);
      if (!subSourceNames.contains(sourceSheetName))
        subSourceNameComboBox.addItem(sourceSheetName);
    } else {
      subSourceNameComboBox.addItem(mappingExpression.getSourceSheetName());
    }

    subSourceNameComboBox.setSelectedItem(sourceSheetName);

    startColumnTextField.setText(mappingExpression.getStartColumn());
    finishColumnTextField.setText(mappingExpression.getFinishColumn());
    startRowTextField.setText("" + mappingExpression.getStartRow());
    finishRowTextField.setText("" + mappingExpression.getFinishRow());

    editMode = true;
    editMappingExpression = mappingExpression;
  }

  private void clearEntryFields()
  {
    commentTextField.setText("");
    commentTextField.setEnabled(true);
    expressionTextArea.setText("");
    expressionTextArea.setEnabled(true);
    subSourceNameComboBox.removeAllItems();
    startColumnTextField.setText("");
    finishColumnTextField.setText("");
    startRowTextField.setText("");
    finishRowTextField.setText("");

    editMode = false;
    editMappingExpression = null;
  }

  private void createComponents()
  {
    Container contentPane = getContentPane();
    JPanel surroundPanel, buttonPanel, textFieldPanel;
    JButton cancelButton, okButton;

    commentLabel = new JLabel("Comment");
    commentTextField = new JTextField("");

    expressionTextArea = new JTextArea("", 20, 80);
    expressionTextArea.setBorder(BorderFactory.createLoweredBevelBorder());

    sourceSheetNameLabel = new JLabel("Sheet name");
    subSourceNameComboBox = new JComboBox();

    startColumnLabel = new JLabel("Start column");
    startColumnTextField = new JTextField("");

    finishColumnLabel = new JLabel("Finish column");
    finishColumnTextField = new JTextField("");

    startRowLabel = new JLabel("Start row");
    startRowTextField = new JTextField("");

    finishRowLabel = new JLabel("Finish row");
    finishRowTextField = new JTextField("");

    cancelButton = new JButton("Cancel");
    cancelButton.setPreferredSize(new Dimension(100, 30));
    cancelButton.addActionListener(new CancelButtonActionListener());

    okButton = new JButton("OK");
    okButton.setPreferredSize(new Dimension(100, 30));
    okButton.addActionListener(new OkButtonActionListener());

    contentPane.setLayout(new BorderLayout());

    surroundPanel = new JPanel(new BorderLayout());
    surroundPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    contentPane.add(surroundPanel, BorderLayout.CENTER);

    textFieldPanel = new JPanel(new GridLayout(6, 2));

    surroundPanel.add(textFieldPanel, BorderLayout.NORTH);
    surroundPanel.add(expressionTextArea, BorderLayout.CENTER);

    textFieldPanel.add(commentLabel);
    textFieldPanel.add(commentTextField);
    textFieldPanel.add(sourceSheetNameLabel);
    textFieldPanel.add(subSourceNameComboBox);
    textFieldPanel.add(startColumnLabel);
    textFieldPanel.add(startColumnTextField);
    textFieldPanel.add(finishColumnLabel);
    textFieldPanel.add(finishColumnTextField);
    textFieldPanel.add(startRowLabel);
    textFieldPanel.add(startRowTextField);
    textFieldPanel.add(finishRowLabel);
    textFieldPanel.add(finishRowTextField);

    buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(cancelButton);
    buttonPanel.add(okButton);

    surroundPanel.add(buttonPanel, BorderLayout.SOUTH);

    pack();
  }

  private class CancelButtonActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      clearEntryFields();
      setVisible(false);
    }
  }

  private class OkButtonActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      String comment, expression, sourceSheetName;
      String startColumn = "", finishColumn = "";
      String startRow = "", finishRow = "";
      MappingExpression mappingExpression;
      boolean errorOccurred = false;

      comment = commentTextField.getText();
      expression = expressionTextArea.getText().trim();
      sourceSheetName = (String)subSourceNameComboBox.getSelectedItem();

      try {
        startColumn = startColumnTextField.getText().trim().toUpperCase();
        finishColumn = finishColumnTextField.getText().trim().toUpperCase();
        SpreadSheetUtil.checkColumnSpecification(startColumn);
        SpreadSheetUtil.checkColumnSpecification(finishColumn);

        startRow = startRowTextField.getText().trim();
        finishRow = finishRowTextField.getText().trim();
      } catch (MappingMasterException ex) {
        getApplicationDialogManager().showErrorMessageDialog(ex.getMessage());
        errorOccurred = true;
      }

      if (!errorOccurred) {
        if (editMode) {
          editMappingExpression
            .update(comment, expression, sourceSheetName, startColumn, finishColumn, startRow, finishRow);
          getMappingExpressionsModel().removeMappingExpression(editMappingExpression); // Remove original
          getMappingExpressionsModel().addMappingExpression(editMappingExpression);
        } else {
          mappingExpression = new MappingExpression(comment, expression, sourceSheetName, startColumn, finishColumn,
            startRow, finishRow);
          getMappingExpressionsModel().addMappingExpression(mappingExpression);
        }

        setVisible(false);

        clearEntryFields();
      }
    }
  }

  private MappingsExpressionsModel getMappingExpressionsModel()
  {
    return application.getApplicationModel().getMappingExpressionsModel();
  }

  private DataSourceModel getDataSourceModel() { return application.getApplicationModel().getDataSourceModel(); }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}
