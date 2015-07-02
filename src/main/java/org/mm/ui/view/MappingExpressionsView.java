package org.mm.ui.view;

import org.mm.core.MappingExpression;
import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MappingsExpressionsModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MappingExpressionsView extends JPanel implements MMView
{
  private final MMApplication application;
  private final JTable mappingExpressionsTable;

  public MappingExpressionsView(MMApplication application)
  {
    this.application = application;
    this.mappingExpressionsTable = new JTable(getMappingExpressionsModel());

    addTableListeners();
    setPreferredColumnWidths();

    getMappingExpressionsModel().setView(this);

    JPanel headingPanel, buttonPanel;
    JButton addButton, editButton, deleteButton;

    JScrollPane scrollPane = new JScrollPane(mappingExpressionsTable);
    JViewport viewport = scrollPane.getViewport();

    setLayout(new BorderLayout());

    headingPanel = new JPanel(new BorderLayout());
    add(headingPanel, BorderLayout.NORTH);

    viewport.setBackground(mappingExpressionsTable.getBackground());

    buttonPanel = new JPanel(new BorderLayout());
    headingPanel.add(buttonPanel, BorderLayout.EAST);

    addButton = new JButton("Add");
    addButton.addActionListener(new AddButtonActionListener());
    buttonPanel.add(addButton, BorderLayout.WEST);

    editButton = new JButton("Edit");
    editButton.addActionListener(new EditButtonActionListener());
    buttonPanel.add(editButton, BorderLayout.CENTER);

    deleteButton = new JButton("Delete");
    deleteButton.addActionListener(new DeleteButtonActionListener());
    buttonPanel.add(deleteButton, BorderLayout.EAST);

    add(scrollPane, BorderLayout.CENTER);

    validate();
  }

  private void addTableListeners()
  {
    mappingExpressionsTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() == 2) {
          if (e.getSource() == mappingExpressionsTable) {
            editSelectedClassMap();
          }
        }
      }
    });
  }

  private void setPreferredColumnWidths()
  {
    TableColumnModel columnModel = mappingExpressionsTable.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(30);
    columnModel.getColumn(0).setMaxWidth(50);
    columnModel.getColumn(1).setPreferredWidth(150);
    columnModel.getColumn(2).setPreferredWidth(300);
    columnModel.getColumn(3).setPreferredWidth(100);
    columnModel.getColumn(3).setMaxWidth(200);
    columnModel.getColumn(4).setPreferredWidth(100);
    columnModel.getColumn(4).setMaxWidth(150);
    columnModel.getColumn(5).setPreferredWidth(100);
    columnModel.getColumn(5).setMaxWidth(150);
    columnModel.getColumn(6).setMaxWidth(100);
    columnModel.getColumn(7).setMaxWidth(100);
  }

  public void update()
  {
    getMappingExpressionsModel().fireTableDataChanged();
    validate();
  }

  /**
   * Returns the selected expression if one is selected; null is returned otherwise.
   */
  public MappingExpression getSelectedExpression()
  {
    MappingExpression selectedClassMap = null;
    int selectedRow = mappingExpressionsTable.getSelectedRow();

    if (selectedRow != -1)
      selectedClassMap = (MappingExpression)getMappingExpressionsModel().getMappingExpressions().toArray()[selectedRow];

    return selectedClassMap;
  }

  private class AddButtonActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      getApplicationDialogManager().getCreateMappingExpressionDialog().setVisible(true);
    }
  }

  private class EditButtonActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      editSelectedClassMap();
    }
  }

  private void editSelectedClassMap()
  {
    MappingExpression selectedClassMap = getSelectedExpression();

    if (selectedClassMap != null && getMappingExpressionsModel().hasMappingExpressions(selectedClassMap)) {
      getApplicationDialogManager().getCreateMappingExpressionDialog(selectedClassMap).setVisible(true);
    }
  }

  private class DeleteButtonActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      MappingExpression selectedClassMap = getSelectedExpression();

      if (getMappingExpressionsModel().hasMappingExpressions(selectedClassMap) && getApplicationDialogManager()
        .showConfirmDialog(getApplicationView(), "Delete Expression", "Do you really want to delete the expression?")) {
        getMappingExpressionsModel().removeMappingExpression(selectedClassMap);
      }
    }
  }

  private MappingsExpressionsModel getMappingExpressionsModel()
  {
    return application.getApplicationModel().getMappingExpressionsModel();
  }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}
