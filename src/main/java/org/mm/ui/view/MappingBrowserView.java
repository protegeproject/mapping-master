package org.mm.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.mm.core.MappingExpression;
import org.mm.ui.dialog.CreateMappingExpressionDialog;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MappingExpressionModel;

public class MappingBrowserView extends JPanel implements MMView
{
	private static final long serialVersionUID = 1L;

	private ApplicationView container;

	private JButton cmdAdd;
	private JButton cmdEdit;
	private JButton cmdDelete;

	private JTextField txtMappingPath;
	private JTable tblMappingExpression;

	public MappingBrowserView(ApplicationView container)
	{
		this.container = container;
		
		tblMappingExpression = new JTable();
		tblMappingExpression.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblMappingExpression.getSelectionModel().addListSelectionListener(new EditMappingExpressionListener());
		
		JScrollPane scrMappingExpression = new JScrollPane(tblMappingExpression);
		
		setLayout(new BorderLayout());
		
		JPanel pnlTop = new JPanel(new BorderLayout());
		add(pnlTop, BorderLayout.NORTH);
		
		JPanel pnlCommandButton = new JPanel(new BorderLayout());
		pnlTop.add(pnlCommandButton, BorderLayout.EAST);
		
		cmdAdd = new JButton("Add");
		cmdAdd.addActionListener(new AddButtonActionListener());
		cmdAdd.setEnabled(false);
		pnlCommandButton.add(cmdAdd, BorderLayout.WEST);
		
		cmdEdit = new JButton("Edit");
		cmdEdit.addActionListener(new EditButtonActionListener());
		cmdEdit.setEnabled(false);
		pnlCommandButton.add(cmdEdit, BorderLayout.CENTER);
		
		cmdDelete = new JButton("Delete");
		cmdDelete.addActionListener(new DeleteButtonActionListener());
		cmdDelete.setEnabled(false);
		pnlCommandButton.add(cmdDelete, BorderLayout.EAST);
		
		add(scrMappingExpression, BorderLayout.CENTER);
		
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mapping File"));
		add(pnlBottom, BorderLayout.SOUTH);

		txtMappingPath = new JTextField();
		txtMappingPath.setPreferredSize(new Dimension(80, 30));
		txtMappingPath.setEnabled(true);
		pnlBottom.add(txtMappingPath, BorderLayout.CENTER);

		JPanel pnlMappingOpenSave = new JPanel(new GridLayout(1, 4));
		pnlBottom.add(pnlMappingOpenSave, BorderLayout.EAST);

		JButton cmdOpen = new JButton("Open");
		cmdOpen.addActionListener(new OpenMappingAction());
		pnlMappingOpenSave.add(cmdOpen);

		JButton cmdSave = new JButton("Save");
		cmdSave.addActionListener(new SaveMappingAction());
		pnlMappingOpenSave.add(cmdSave);

		JButton cmdSaveAs = new JButton("Save As...");
		cmdSaveAs.addActionListener(new SaveAsMappingAction());
		pnlMappingOpenSave.add(cmdSaveAs);
		
		validate();
	}

	@Override
	public void update()
	{
		cmdAdd.setEnabled(true);
		cmdEdit.setEnabled(true);
		cmdDelete.setEnabled(true);
		
		tblMappingExpression.setModel(new MappingExpressionTableModel(getMappingExpressionsModel().getExpressions()));
	}

	private MappingExpressionModel getMappingExpressionsModel()
	{
		return container.getApplicationModel().getMappingExpressionsModel();
	}

	private MMApplicationDialogManager getApplicationDialogManager()
	{
		return container.getApplicationDialogManager();
	}

	class MappingExpressionTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		private final String[] COLUMN_NAMES = {
			"Sheet name", "Start column", "End column", "Start row", "End row",
			"Mapping expression", "Comment"
		};

		private Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		public MappingExpressionTableModel(List<MappingExpression> mappings)
		{
			for (MappingExpression mapping : mappings) {
				Vector<Object> row = new Vector<Object>();
				row.add(mapping.getSheetName());
				row.add(mapping.getStartColumn());
				row.add(mapping.getEndColumn());
				row.add(mapping.getStartRow());
				row.add(mapping.getEndRow());
				row.add(mapping.getExpressionString());
				row.add(mapping.getComment());
				data.add(row);
			}
		}

		@Override
		public String getColumnName(int column) // 0-based
		{
			return COLUMN_NAMES[column];
		}
	
		@Override
		public int getRowCount()
		{
			return data.size();
		}

		@Override
		public int getColumnCount()
		{
			return COLUMN_NAMES.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) // 1-based
		{
			return data.get(rowIndex).get(columnIndex);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return false;
		}
	}

	class EditMappingExpressionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			int rowIndex = tblMappingExpression.getSelectedRow();
			CreateMappingExpressionDialog dialog = new CreateMappingExpressionDialog(container);
			dialog.fillDialogFields(getMappingExpressionsModel().getExpressions().get(rowIndex));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
	}

	/*
	 * Action listener implementations for command buttons in MappingExpressionView panel
	 */

	class AddButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			CreateMappingExpressionDialog dialog = new CreateMappingExpressionDialog(container);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
	}

	class EditButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int selectedRow = tblMappingExpression.getSelectedRow();
			if (selectedRow == -1) {
				getApplicationDialogManager().showMessageDialog(container, "No mapping expression was selected");
				return;
			}
			MappingExpression selectedMapping = getSelectedExpression(selectedRow);
			CreateMappingExpressionDialog dialog = new CreateMappingExpressionDialog(container);
			dialog.fillDialogFields(selectedMapping);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}

		private MappingExpression getSelectedExpression(int selectedRow)
		{
			return getMappingExpressionsModel().getExpressions().get(selectedRow);
		}
	}

	class DeleteButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int selectedRow = tblMappingExpression.getSelectedRow();
			if (selectedRow == -1) {
				getApplicationDialogManager().showMessageDialog(container, "No mapping expression was selected");
				return;
			}
			MappingExpression selectedMapping = getSelectedExpression(selectedRow);
			int answer = getApplicationDialogManager().showConfirmDialog(container,
					"Delete", "Do you really want to delete the selected expression?");
			if (answer == JOptionPane.YES_OPTION) {
				getMappingExpressionsModel().removeMappingExpression(selectedMapping);
			}
		}

		private MappingExpression getSelectedExpression(int selectedRow)
		{
			return getMappingExpressionsModel().getExpressions().get(selectedRow);
		}
	}

	class OpenMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = getApplicationDialogManager().createOpenFileChooser(
					"Open Mapping Expression", "json", "MappingMaster DSL Mapping Expression (.json)");
			if (fileChooser.showOpenDialog(container) == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChooser.getSelectedFile();
					String filename = file.getAbsolutePath();
					container.loadMappingDocument(filename);
					txtMappingPath.setText(filename);
				} catch (Exception ex) {
					getApplicationDialogManager().showErrorMessageDialog(container,
							"Error opening file: " + ex.getMessage());
				}
			}
		}
	}

	class SaveMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Implement save to JSON
		}
	}

	class SaveAsMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Implement save as to JSON
		}
	}
}
