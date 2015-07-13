package org.mm.ui.view;

import org.mm.ui.MMApplication;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.model.MMApplicationModel;
import org.mm.renderer.MappingConfigurationOptionsManager;
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

		statusWindowAppend("MappingMaster V0.95\n\n");
		statusWindowAppend("See http://protege.cim3.net/cgi-bin/wiki.pl?MappingMaster for documentation.\n");
		statusWindowAppend("Use the Expressions tab to define mappings using MappingMaster's DSL.\n");
		statusWindowAppend("Click the Map button to perform mappings.\n");
	}

	@Override
	public void update()
	{
		if (getApplicationModel().hasMappingFile()) {
			fileNameTextField.setText(getApplicationModel().getMappingFileName());
			saveButton.setEnabled(true);
			saveAsButton.setEnabled(true);
		} else {
			fileNameTextField.setText("");
			saveButton.setEnabled(false);
			saveAsButton.setEnabled(true);
		}

		validate();
	}

	public void clearStatusWindow()
	{
		statusWindow.setText("");
	}

	public void statusWindowAppend(String text)
	{
		statusWindow.append(text);
	}

	// TODO: need to make option stuff more generic. MappingConfigurationOptionsManager should return list of options
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
		mapExpressionsButton.addActionListener(new MapExpressionsAction(application));

		mappingsButtonPanel = new JPanel(new GridLayout(3, 1));
		mappingsButtonPanel.add(new JPanel(), 0);
		mappingsButtonPanel.add(mapExpressionsButton, 1);
		mappingsButtonPanel.add(new JPanel(), 2);

		headingPanel.add(mappingsButtonPanel, BorderLayout.WEST);

		optionsPanel = new JPanel(new GridLayout(5, 2));
		headingPanel.add(optionsPanel, BorderLayout.EAST);

		nameEncodingLabel = new JLabel("Default Name Encoding");
		optionsPanel.add(nameEncodingLabel);
		nameEncodingComboBox = new JComboBox();
		for (String nameEncoding : getOptionsManager().getNameEncodings())
			nameEncodingComboBox.addItem(nameEncoding);
		nameEncodingComboBox.setSelectedItem(getOptionsManager().getDefaultNameEncoding());
		nameEncodingComboBox.addActionListener(
			new ConfigurationActionListener(getOptionsManager().getDefaultValueEncodingOptionName()));
		optionsPanel.add(nameEncodingComboBox);

		referenceTypeLabel = new JLabel("Default Entity Type");
		optionsPanel.add(referenceTypeLabel);
		referenceTypeComboBox = new JComboBox();
		for (String referenceType : getOptionsManager().getReferenceValueTypes())
			referenceTypeComboBox.addItem(referenceType);
		referenceTypeComboBox.setSelectedItem(getOptionsManager().getDefaultReferenceType());
		referenceTypeComboBox.addActionListener(
			new ConfigurationActionListener(getOptionsManager().getDefaultReferenceTypeOptionName()));
		optionsPanel.add(referenceTypeComboBox);

		propertyTypeLabel = new JLabel("Default Property Type");
		optionsPanel.add(propertyTypeLabel);
		propertyTypeComboBox = new JComboBox();
		for (String propertyType : getOptionsManager().getPropertyTypes())
			propertyTypeComboBox.addItem(propertyType);
		propertyTypeComboBox.setSelectedItem(getOptionsManager().getDefaultPropertyType());
		propertyTypeComboBox.addActionListener(new ConfigurationActionListener(getOptionsManager()
				.getDefaultPropertyTypeOptionName()));
		optionsPanel.add(propertyTypeComboBox);

		propertyValueTypeLabel = new JLabel("Default Property Value Type");
		optionsPanel.add(propertyValueTypeLabel);
		propertyValueTypeComboBox = new JComboBox();
		for (String propertyValueType : getOptionsManager().getPropertyValueTypes())
			propertyValueTypeComboBox.addItem(propertyValueType);
		propertyValueTypeComboBox.setSelectedItem(getOptionsManager().getDefaultPropertyValueType());
		propertyValueTypeComboBox.addActionListener(new ConfigurationActionListener(getOptionsManager()
				.getDefaultPropertyValueTypeOptionName()));
		optionsPanel.add(propertyValueTypeComboBox);

		dataPropertyValueTypeLabel = new JLabel("Default Data Property Value Type");
		optionsPanel.add(dataPropertyValueTypeLabel);
		dataPropertyValueTypeComboBox = new JComboBox();
		for (String dataPropertyValueType : getOptionsManager().getDataPropertyValueTypes())
			dataPropertyValueTypeComboBox.addItem(dataPropertyValueType);
		dataPropertyValueTypeComboBox.setSelectedItem(getOptionsManager().getDefaultDataPropertyValueType());
		dataPropertyValueTypeComboBox.addActionListener(new ConfigurationActionListener(getOptionsManager()
				.getDefaultDataPropertyValueTypeOptionName()));
		optionsPanel.add(dataPropertyValueTypeComboBox);

		statusWindow = new JTextArea();
		statusWindow.setBorder(BorderFactory.createEtchedBorder());
		statusWindow.setBackground(Color.WHITE);
		statusWindow.setLineWrap(true);
		statusWindow.setEditable(false);
		scrollPane = new JScrollPane(statusWindow);

		add(scrollPane, BorderLayout.CENTER);

		footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mapping Ontology"));
		add(footerPanel, BorderLayout.SOUTH);

		fileNameTextField = createTextField("");
		fileNameTextField.setEnabled(true);
		footerPanel.add(fileNameTextField, BorderLayout.CENTER);

		fileButtonPanel = new JPanel(new GridLayout(1, 4));
		footerPanel.add(fileButtonPanel, BorderLayout.EAST);

		openButton = new JButton("Open");
		openButton.addActionListener(new OpenMappingsAction(application));
		fileButtonPanel.add(openButton);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveMappingsAction(application));
		saveButton.setEnabled(false);
		fileButtonPanel.add(saveButton);

		saveAsButton = new JButton("Save As...");
		saveAsButton.addActionListener(new SaveAsMappingsAction(application));
		saveAsButton.setEnabled(true);
		fileButtonPanel.add(saveAsButton, BorderLayout.CENTER);

		closeButton = new JButton("Close");
		closeButton.addActionListener(new CloseMappingsAction(application));
		fileButtonPanel.add(closeButton);
	}

	private MMApplicationModel getApplicationModel()
	{
		return application.getApplicationModel();
	}

	private MappingConfigurationOptionsManager getOptionsManager()
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
			getOptionsManager().setMappingConfigurationOption(optionName, selectedItem);
		}

	}

	private JTextField createTextField(String text)
	{
		JTextField textField = new JTextField(text);
		textField.setPreferredSize(new Dimension(80, 30));
		return textField;
	}
}
