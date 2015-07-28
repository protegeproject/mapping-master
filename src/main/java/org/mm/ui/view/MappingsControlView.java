package org.mm.ui.view;

import org.mm.renderer.ReferenceRendererOptionsManager;
import org.mm.ui.MMApplication;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.model.MMApplicationModel;
import org.mm.ui.action.CloseMappingsAction;
import org.mm.ui.action.MapExpressionsAction;
import org.mm.ui.action.OpenMappingsAction;
import org.mm.ui.action.SaveAsMappingsAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MappingsControlView extends JPanel implements MMView
{
	private final MMApplication application;
	private JTextField fileNameTextField;
	private JTextArea statusWindow;
	private JButton saveButton, saveAsButton;
	private JComboBox nameEncodingComboBox, referenceTypeComboBox, propertyTypeComboBox, propertyValueTypeComboBox,
			dataPropertyValueTypeComboBox;

	public MappingsControlView(MMApplication application)
	{
		this.application = application;

		createComponents();

		statusWindowAppend("MappingMaster V1.0.0\n\n");
		statusWindowAppend("See https://github.com/protegeproject/mapping-master/wiki for documentation.\n");
		statusWindowAppend("Use the Expressions tab to define mappings using MappingMaster's DSL.\n");
		statusWindowAppend("Click the Map button to perform mappings.\n");
	}

	@Override
	public void update()
	{
		if (getApplicationModel().hasMappingFile()) {
			this.fileNameTextField.setText(getApplicationModel().getMappingFileName());
			this.saveButton.setEnabled(true);
			this.saveAsButton.setEnabled(true);
		} else {
			this.fileNameTextField.setText("");
			this.saveButton.setEnabled(false);
			this.saveAsButton.setEnabled(true);
		}

		validate();
	}

	public void clearStatusWindow()
	{
		this.statusWindow.setText("");
	}

	public void statusWindowAppend(String text)
	{
		this.statusWindow.append(text);
	}

	// TODO Need to make options stuff more generic. ReferenceRendererOptionsManager should return list of options.
	private void createComponents()
	{
		JPanel headingPanel, mappingsButtonPanel, optionsPanel, fileButtonPanel, footerPanel;
		JButton openButton, closeButton, mapExpressionsButton;
		JLabel nameEncodingLabel, referenceTypeLabel, propertyTypeLabel, propertyValueTypeLabel, dataPropertyValueTypeLabel;

		JScrollPane scrollPane;

		setLayout(new BorderLayout());

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));

		headingPanel = new JPanel(new BorderLayout());
		add(headingPanel, BorderLayout.NORTH);

		mapExpressionsButton = new JButton("Map");
		mapExpressionsButton.setPreferredSize(new Dimension(125, 25));
		mapExpressionsButton.addActionListener(new MapExpressionsAction(this.application));

		mappingsButtonPanel = new JPanel(new GridLayout(3, 1));
		mappingsButtonPanel.add(new JPanel(), 0);
		mappingsButtonPanel.add(mapExpressionsButton, 1);
		mappingsButtonPanel.add(new JPanel(), 2);

		headingPanel.add(mappingsButtonPanel, BorderLayout.WEST);

		optionsPanel = new JPanel(new GridLayout(5, 2));
		headingPanel.add(optionsPanel, BorderLayout.EAST);

		nameEncodingLabel = new JLabel("Default Name Encoding");
		optionsPanel.add(nameEncodingLabel);
		this.nameEncodingComboBox = new JComboBox();
		getOptionsManager().getNameEncodings().forEach(this.nameEncodingComboBox::addItem);
		this.nameEncodingComboBox.setSelectedItem(getOptionsManager().getDefaultNameEncoding());
		this.nameEncodingComboBox
			.addActionListener(new ConfigurationActionListener(getOptionsManager().getDefaultValueEncodingOptionName()));
		optionsPanel.add(this.nameEncodingComboBox);

		referenceTypeLabel = new JLabel("Default Entity Type");
		optionsPanel.add(referenceTypeLabel);
		this.referenceTypeComboBox = new JComboBox();
		getOptionsManager().getReferenceValueTypes().forEach(this.referenceTypeComboBox::addItem);
		this.referenceTypeComboBox.setSelectedItem(getOptionsManager().getDefaultReferenceType());
		this.referenceTypeComboBox
			.addActionListener(new ConfigurationActionListener(getOptionsManager().getDefaultReferenceTypeOptionName()));
		optionsPanel.add(this.referenceTypeComboBox);

		propertyTypeLabel = new JLabel("Default Property Type");
		optionsPanel.add(propertyTypeLabel);
		this.propertyTypeComboBox = new JComboBox();
		getOptionsManager().getPropertyTypes().forEach(this.propertyTypeComboBox::addItem);
		this.propertyTypeComboBox.setSelectedItem(getOptionsManager().getDefaultPropertyType());
		this.propertyTypeComboBox
			.addActionListener(new ConfigurationActionListener(getOptionsManager().getDefaultPropertyTypeOptionName()));
		optionsPanel.add(this.propertyTypeComboBox);

		propertyValueTypeLabel = new JLabel("Default Property Value Type");
		optionsPanel.add(propertyValueTypeLabel);
		this.propertyValueTypeComboBox = new JComboBox();
		getOptionsManager().getPropertyValueTypes().forEach(this.propertyValueTypeComboBox::addItem);
		this.propertyValueTypeComboBox.setSelectedItem(getOptionsManager().getDefaultPropertyValueType());
		this.propertyValueTypeComboBox
			.addActionListener(new ConfigurationActionListener(getOptionsManager().getDefaultPropertyValueTypeOptionName()));
		optionsPanel.add(this.propertyValueTypeComboBox);

		dataPropertyValueTypeLabel = new JLabel("Default Data Property Value Type");
		optionsPanel.add(dataPropertyValueTypeLabel);
		this.dataPropertyValueTypeComboBox = new JComboBox();
		getOptionsManager().getDataPropertyValueTypes().forEach(this.dataPropertyValueTypeComboBox::addItem);
		this.dataPropertyValueTypeComboBox.setSelectedItem(getOptionsManager().getDefaultDataPropertyValueType());
		this.dataPropertyValueTypeComboBox.addActionListener(
			new ConfigurationActionListener(getOptionsManager().getDefaultDataPropertyValueTypeOptionName()));
		optionsPanel.add(this.dataPropertyValueTypeComboBox);

		this.statusWindow = new JTextArea();
		this.statusWindow.setBorder(BorderFactory.createEtchedBorder());
		this.statusWindow.setBackground(Color.WHITE);
		this.statusWindow.setLineWrap(true);
		this.statusWindow.setEditable(false);
		scrollPane = new JScrollPane(this.statusWindow);

		add(scrollPane, BorderLayout.CENTER);

		footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mapping Ontology"));
		add(footerPanel, BorderLayout.SOUTH);

		this.fileNameTextField = createTextField("");
		this.fileNameTextField.setEnabled(true);
		footerPanel.add(this.fileNameTextField, BorderLayout.CENTER);

		fileButtonPanel = new JPanel(new GridLayout(1, 4));
		footerPanel.add(fileButtonPanel, BorderLayout.EAST);

		openButton = new JButton("Open");
		openButton.addActionListener(new OpenMappingsAction(this.application));
		fileButtonPanel.add(openButton);

		this.saveButton = new JButton("Save");
		this.saveButton.addActionListener(new SaveMappingsAction(this.application));
		this.saveButton.setEnabled(false);
		fileButtonPanel.add(this.saveButton);

		this.saveAsButton = new JButton("Save As...");
		this.saveAsButton.addActionListener(new SaveAsMappingsAction(this.application));
		this.saveAsButton.setEnabled(true);
		fileButtonPanel.add(this.saveAsButton, BorderLayout.CENTER);

		closeButton = new JButton("Close");
		closeButton.addActionListener(new CloseMappingsAction(this.application));
		fileButtonPanel.add(closeButton);
	}

	private MMApplicationModel getApplicationModel()
	{
		return this.application.getApplicationModel();
	}

	private ReferenceRendererOptionsManager getOptionsManager()
	{
		return getApplicationModel().getMappingConfigurationOptionsManager();
	}

	private class ConfigurationActionListener implements ActionListener
	{
		private final String optionName;

		public ConfigurationActionListener(String optionName)
		{
			this.optionName = optionName;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JComboBox cb = (JComboBox)e.getSource();
			String selectedItem = (String)cb.getSelectedItem();
			// cb.setSelectedItem(selectedItem);
			getOptionsManager().setMappingConfigurationOption(this.optionName, selectedItem);
		}

	}

	private JTextField createTextField(String text)
	{
		JTextField textField = new JTextField(text);
		textField.setPreferredSize(new Dimension(80, 30));
		return textField;
	}
}
