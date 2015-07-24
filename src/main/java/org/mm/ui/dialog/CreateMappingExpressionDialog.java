package org.mm.ui.dialog;

import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.ss.SpreadSheetUtil;
import org.mm.ui.MMApplication;
import org.mm.ui.model.DataSourceModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.ui.view.MMApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CreateMappingExpressionDialog extends JDialog
{
  private final MMApplication application;

  private JLabel commentLabel, sourceSheetNameLabel, startColumnLabel, finishColumnLabel, startRowLabel, finishRowLabel;
  private JTextField commentTextField, startColumnTextField, finishColumnTextField, startRowTextField, finishRowTextField;
  private JTextArea expressionTextArea;
  private JComboBox subSourceNameComboBox;

  private boolean editMode;
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
    getDataSourceModel().getSubSourceNames().forEach(this.subSourceNameComboBox::addItem);
  }

  public void setEditMappingExpression(MappingExpression mappingExpression)
  {
    String sourceSheetName = mappingExpression.getSourceSheetName();

    clearEntryFields();

    this.commentTextField.setText(mappingExpression.getComment());
    this.expressionTextArea.setText(mappingExpression.getExpression());

    if (getDataSourceModel().hasDataSource()) {
      List<String> subSourceNames = getDataSourceModel().getSubSourceNames();
      subSourceNames.forEach(this.subSourceNameComboBox::addItem);
      if (!subSourceNames.contains(sourceSheetName))
        this.subSourceNameComboBox.addItem(sourceSheetName);
    } else {
      this.subSourceNameComboBox.addItem(mappingExpression.getSourceSheetName());
    }

    this.subSourceNameComboBox.setSelectedItem(sourceSheetName);

    this.startColumnTextField.setText(mappingExpression.getStartColumn());
    this.finishColumnTextField.setText(mappingExpression.getFinishColumn());
    this.startRowTextField.setText("" + mappingExpression.getStartRow());
    this.finishRowTextField.setText("" + mappingExpression.getFinishRow());

    this.editMode = true;
    this.editMappingExpression = mappingExpression;
  }

  private void clearEntryFields()
  {
    this.commentTextField.setText("");
    this.commentTextField.setEnabled(true);
    this.expressionTextArea.setText("");
    this.expressionTextArea.setEnabled(true);
    this.subSourceNameComboBox.removeAllItems();
    this.startColumnTextField.setText("");
    this.finishColumnTextField.setText("");
    this.startRowTextField.setText("");
    this.finishRowTextField.setText("");

    this.editMode = false;
    this.editMappingExpression = null;
  }

  private void createComponents()
  {
    Container contentPane = getContentPane();
    JPanel surroundPanel, buttonPanel, textFieldPanel;
    JButton cancelButton, okButton;

    this.commentLabel = new JLabel("Comment");
    this.commentTextField = new JTextField("");

    this.expressionTextArea = new JTextArea("", 20, 80);
    this.expressionTextArea.setBorder(BorderFactory.createLoweredBevelBorder());

    this.sourceSheetNameLabel = new JLabel("Sheet name");
    this.subSourceNameComboBox = new JComboBox();

    this.startColumnLabel = new JLabel("Start column");
    this.startColumnTextField = new JTextField("");

    this.finishColumnLabel = new JLabel("Finish column");
    this.finishColumnTextField = new JTextField("");

    this.startRowLabel = new JLabel("Start row");
    this.startRowTextField = new JTextField("");

    this.finishRowLabel = new JLabel("Finish row");
    this.finishRowTextField = new JTextField("");

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
    surroundPanel.add(this.expressionTextArea, BorderLayout.CENTER);

    textFieldPanel.add(this.commentLabel);
    textFieldPanel.add(this.commentTextField);
    textFieldPanel.add(this.sourceSheetNameLabel);
    textFieldPanel.add(this.subSourceNameComboBox);
    textFieldPanel.add(this.startColumnLabel);
    textFieldPanel.add(this.startColumnTextField);
    textFieldPanel.add(this.finishColumnLabel);
    textFieldPanel.add(this.finishColumnTextField);
    textFieldPanel.add(this.startRowLabel);
    textFieldPanel.add(this.startRowTextField);
    textFieldPanel.add(this.finishRowLabel);
    textFieldPanel.add(this.finishRowTextField);

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

      comment = CreateMappingExpressionDialog.this.commentTextField.getText();
      expression = CreateMappingExpressionDialog.this.expressionTextArea.getText().trim();
      sourceSheetName = (String)CreateMappingExpressionDialog.this.subSourceNameComboBox.getSelectedItem();

      try {
        startColumn = CreateMappingExpressionDialog.this.startColumnTextField.getText().trim().toUpperCase();
        finishColumn = CreateMappingExpressionDialog.this.finishColumnTextField.getText().trim().toUpperCase();
        SpreadSheetUtil.checkColumnSpecification(startColumn);
        SpreadSheetUtil.checkColumnSpecification(finishColumn);

        startRow = CreateMappingExpressionDialog.this.startRowTextField.getText().trim();
        finishRow = CreateMappingExpressionDialog.this.finishRowTextField.getText().trim();
      } catch (MappingMasterException ex) {
        getApplicationDialogManager().showErrorMessageDialog(ex.getMessage());
        errorOccurred = true;
      }

      if (!errorOccurred) {
        if (CreateMappingExpressionDialog.this.editMode) {
          CreateMappingExpressionDialog.this.editMappingExpression
            .update(comment, expression, sourceSheetName, startColumn, finishColumn, startRow, finishRow);
          getMappingExpressionsModel().removeMappingExpression(CreateMappingExpressionDialog.this.editMappingExpression); // Remove original
          getMappingExpressionsModel().addMappingExpression(CreateMappingExpressionDialog.this.editMappingExpression);
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
    return this.application.getApplicationModel().getMappingExpressionsModel();
  }

  private DataSourceModel getDataSourceModel() { return this.application.getApplicationModel().getDataSourceModel(); }

  private MMApplicationView getApplicationView() { return this.application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}
