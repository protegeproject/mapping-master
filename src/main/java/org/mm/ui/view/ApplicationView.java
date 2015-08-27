package org.mm.ui.view;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.mm.core.MappingExpression;
import org.mm.core.MappingExpressionSet;
import org.mm.core.settings.ReferenceSettings;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.Renderer;
import org.mm.rendering.Rendering;
import org.mm.ui.MMApplication;
import org.mm.ui.MMApplicationFactory;
import org.mm.ui.dialog.MMDialogManager;
import org.mm.ui.model.ApplicationModel;

/**
 * This is the main Mapping Master user interface. It contains a view of a
 * spreadsheet and a control area to edit and execute Mapping Master
 * expressions.
 */
public class ApplicationView extends JSplitPane implements MMView
{
	private static final long serialVersionUID = 1L;

	private MMDialogManager applicationDialogManager;
	private DataSourceView dataSourceView;
	private MappingControlView mappingControlView;
	private MappingBrowserView mappingExpressionView;

	private MMApplication application;
	private MMApplicationFactory applicationFactory = new MMApplicationFactory();

	private ReferenceSettings referenceSettings = new ReferenceSettings();

	public ApplicationView(MMDialogManager applicationDialogManager)
	{
		this.applicationDialogManager = applicationDialogManager;

		setupApplication();

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

	public void updateOntologyDocument(String path)
	{
		applicationFactory.setOntologyLocation(path);
		fireApplicationResourceChanged();
	}

	public void loadWorkbookDocument(String path)
	{
		applicationFactory.setWorkbookLocation(path);
		fireApplicationResourceChanged();

		updateDataSourceView();
		updateMappingControlView();
		updateMappingBrowserView();
	}

	public void loadMappingDocument(String path)
	{
		applicationFactory.setMappingLocation(path);
		fireApplicationResourceChanged();
		
		updateMappingBrowserView();
	}

	private void fireApplicationResourceChanged()
	{
		setupApplication();
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
			application = applicationFactory.createApplication();
		} catch (Exception e) {
			applicationDialogManager.showErrorMessageDialog(this, e.getMessage());
		}
	}

	public void evaluate(MappingExpression mapping, Renderer renderer, List<Rendering> results) throws ParseException
	{
		String expression = mapping.getExpressionString();
		ExpressionNode expressionNode = parseExpression(expression, referenceSettings);
		results.add(renderer.renderExpression(expressionNode).get());
	}

	private ExpressionNode parseExpression(String expression, ReferenceSettings settings) throws ParseException
	{
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expression.getBytes()), settings, -1);
		SimpleNode simpleNode = parser.expression();
		return new ExpressionNode((ASTExpression) simpleNode);
	}

	@Override
	public void update()
	{
		// NO-OP
	}

	public void updateMappingExpressionModel(MappingExpressionSet mappings)
	{
		getApplicationModel().getMappingExpressionsModel().changeMappingExpressionSet(mappings);
	}

	public Renderer getDefaultRenderer()
	{
		return getApplicationModel().getRenderer();
	}

	public MMDialogManager getApplicationDialogManager()
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
