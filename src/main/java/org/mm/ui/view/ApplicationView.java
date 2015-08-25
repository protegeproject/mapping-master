package org.mm.ui.view;

import java.io.ByteArrayInputStream;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.mm.core.MappingExpression;
import org.mm.core.settings.ReferenceSettings;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.Renderer;
import org.mm.ui.MMApplication;
import org.mm.ui.MMApplicationFactory;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.ApplicationModel;

/**
 * This is the main Mapping Master user interface. It contains a view of a
 * spreadsheet and a control area to edit and execute Mapping Master
 * expressions.
 */
public class ApplicationView extends JSplitPane implements MMView
{
	private static final long serialVersionUID = 1L;

	private MMApplicationDialogManager applicationDialogManager;
	private DataSourceView dataSourceView;
	private MappingControlView mappingControlView;
	private MappingBrowserView mappingExpressionView;

	private MMApplication application;
	private MMApplicationFactory applicationFactory = new MMApplicationFactory();

	private boolean resourceChanged = true;

	private ReferenceSettings referenceSettings = new ReferenceSettings();

	public ApplicationView(MMApplicationDialogManager applicationDialogManager)
	{
		this.applicationDialogManager = applicationDialogManager;

		setOrientation(JSplitPane.VERTICAL_SPLIT);

		/*
		 * Workbook sheet GUI presentation
		 */
		dataSourceView = new DataSourceView(this);
		setTopComponent(dataSourceView);

		JTabbedPane tabContainer = new JTabbedPane();
		setBottomComponent(tabContainer);

		/*
		 * Mapping Master command control, reference settings
		 */
		mappingControlView = new MappingControlView(this);
		tabContainer.addTab("Mapping Control", null, mappingControlView, "Evaluate mapping expressions");

		/*
		 * Mapping browser, create, edit, remove panel
		 */
		mappingExpressionView = new MappingBrowserView(this);
		tabContainer.addTab("Mapping Browser", null, mappingExpressionView, "Load, add, edit, remove mapping expressions");
		
		validate();
	}

	public ApplicationModel getApplicationModel()
	{
		return application.getApplicationModel();
	}

	public void loadWorkbookDocument(String path)
	{
		applicationFactory.setWorkbookLocation(path);
		resourceChanged = true;
		
		setupApplication();
		updateDataSourceView();
		updateMappingControlView();
		updateMappingBrowserView();
	}

	public void loadMappingDocument(String path)
	{
		applicationFactory.setMappingLocation(path);
		resourceChanged = true;
		
		setupApplication();
		updateMappingBrowserView();
	}

	private void updateDataSourceView()
	{
		dataSourceView.update();
	}

	private void updateMappingControlView()
	{
		mappingControlView.update();
	}

	private void updateMappingBrowserView()
	{
		mappingExpressionView.update();
	}

	private void setupApplication()
	{
		try {
			if (resourceChanged) {
				application = applicationFactory.createApplication();
				resourceChanged = false; // application was created, reset the flag
			}
		} catch (Exception e) {
			applicationDialogManager.showErrorMessageDialog(this, e.getMessage());
		}
	}

	public void updateOntologyDocument(String path)
	{
		applicationFactory.setOntologyLocation(path);
		resourceChanged = true;
	}

	public void initRenderer()
	{
		setupApplication();
	}

	public void evaluate(MappingExpression mapping, Renderer renderer)
	{
		try {
			String expression = mapping.getExpressionString();
			ExpressionNode expressionNode = parseExpression(expression, referenceSettings);
			renderer.renderExpression(expressionNode);
		} catch (Exception e) {
			applicationDialogManager.showErrorMessageDialog(this, e.getMessage());
		}
	}

	private ExpressionNode parseExpression(String expression, ReferenceSettings settings) throws ParseException
	{
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expression.getBytes()), settings, -1);
		SimpleNode simpleNode = parser.expression();
		return new ExpressionNode((ASTExpression)simpleNode);
	}

	@Override
	public void update()
	{
		// NO-OP
	}

	public Renderer getDefaultRenderer()
	{
		return getApplicationModel().getRenderer();
	}

	public MMApplicationDialogManager getApplicationDialogManager()
	{
		return applicationDialogManager;
	}

	public ReferenceSettings getReferenceSettings()
	{
		return referenceSettings;
	}

	public DataSourceView getDataSourceView()
	{
		return dataSourceView;
	}

	public MappingControlView getMappingsControlView()
	{
		return mappingControlView;
	}

	public MappingBrowserView getMappingBrowserView()
	{
		return mappingExpressionView;
	}
}
