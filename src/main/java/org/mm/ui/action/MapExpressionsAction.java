package org.mm.ui.action;

import jxl.Sheet;
import jxl.Workbook;
import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPICoreRenderer;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadSheetUtil;
import org.mm.ss.SpreadsheetLocation;
import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MMApplicationModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.ui.model.DataSourceModel;
import org.mm.ui.view.MMApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class MapExpressionsAction implements ActionListener
{
	private final MMApplication application;

	public MapExpressionsAction(MMApplication application) { this.application = application; }

	public void actionPerformed(ActionEvent e)
	{
		if (!getMappingExpressionsModel().hasMappingExpressions())
			getApplicationDialogManager().showMessageDialog(getApplicationView(), "No mappings defined!");
		else if (!getDataSourceModel().hasDataSource())
			getApplicationDialogManager().showMessageDialog(getApplicationView(), "No data source loaded!");
		else {
			try {
				OWLAPICoreRenderer renderer = getApplicationModel().getRenderer();
				Set<MappingExpression> mappingExpressions = getMappingExpressionsModel().getMappingExpressions(true);
				SpreadSheetDataSource dataSource = getDataSourceModel().getDataSource();
				Workbook workbook = dataSource.getWorkbook();

				for (MappingExpression mappingExpression : mappingExpressions) {
					String sheetName = mappingExpression.getSourceSheetName();
					Sheet sheet = workbook.getSheet(sheetName);
					int startColumnNumber = SpreadSheetUtil.columnName2Number(mappingExpression.getStartColumn());
					int startRowNumber = SpreadSheetUtil.row2Number(mappingExpression.getStartRow());
					int finishColumnNumber = mappingExpression.hasFinishColumnWildcard() ?
							sheet.getColumns() :
							SpreadSheetUtil.columnName2Number(mappingExpression.getFinishColumn());
					int finishRowNumber = mappingExpression.hasFinishRowWildcard() ?
							sheet.getRows() :
							SpreadSheetUtil.row2Number(mappingExpression.getFinishRow());

					if (startColumnNumber > finishColumnNumber)
						throw new RendererException("start column after finish column in expression " + mappingExpression);
					if (startRowNumber > finishRowNumber)
						throw new RendererException("start row after finish row in expression " + mappingExpression);

					SpreadsheetLocation finishLocation = new SpreadsheetLocation(sheetName, finishColumnNumber, finishRowNumber);
					SpreadsheetLocation startLocation = new SpreadsheetLocation(sheetName, startColumnNumber, startRowNumber);
					SpreadsheetLocation currentLocation = new SpreadsheetLocation(sheetName, startColumnNumber, startRowNumber);

					dataSource.setCurrentLocation(currentLocation);
					//TODO renderer.renderExpression(mappingExpression);
					while (!currentLocation.equals(finishLocation)) {
						currentLocation = incrementLocation(currentLocation, startLocation, finishLocation);
						dataSource.setCurrentLocation(currentLocation);
						// TODO renderer.renderExpression(mappingExpression);
					}
				}
				getApplicationDialogManager().showMessageDialog(getApplicationView(), "Mappings performed successfully.");
			} catch (MappingMasterException | RendererException ex) {
				ex.printStackTrace();
				getApplicationDialogManager().showErrorMessageDialog(getApplicationView(), "Mapping error: " + ex.getMessage());
			}
		}
	}

	private static SpreadsheetLocation incrementLocation(SpreadsheetLocation currentLocation,
			SpreadsheetLocation startLocation, SpreadsheetLocation finishLocation) throws RendererException
	{
		if (currentLocation.getRowNumber() < finishLocation.getRowNumber())
			return new SpreadsheetLocation(currentLocation.getSheetName(), currentLocation.getColumnNumber(),
					currentLocation.getRowNumber() + 1);
		else if (currentLocation.getRowNumber() == finishLocation.getRowNumber()) {
			if (currentLocation.getColumnNumber() < finishLocation.getColumnNumber()) {
				return new SpreadsheetLocation(currentLocation.getSheetName(), currentLocation.getColumnNumber() + 1,
						startLocation.getRowNumber());
			} else {
				throw new RendererException("internalError: incrementLocation called redundantly");
			}
		} else
			throw new RendererException("internalError: incrementLocation called redundantly");
	}

	private MappingsExpressionsModel getMappingExpressionsModel()
	{
		return application.getApplicationModel().getMappingExpressionsModel();
	}

	private DataSourceModel getDataSourceModel() { return application.getApplicationModel().getDataSourceModel(); }

	private MMApplicationView getApplicationView() { return application.getApplicationView(); }

	private MMApplicationModel getApplicationModel() { return application.getApplicationModel(); }

	private MMApplicationDialogManager getApplicationDialogManager()
	{
		return getApplicationView().getApplicationDialogManager();
	}
}
