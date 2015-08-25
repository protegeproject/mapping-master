package org.mm.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.MappingExpression;
import org.mm.core.MappingExpressionSet;
import org.mm.exceptions.MappingMasterException;
import org.mm.renderer.RendererException;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadSheetUtil;
import org.mm.ss.SpreadsheetLocation;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.DataSourceModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.ui.view.ApplicationView;

public class MapExpressionsAction implements ActionListener
{
	private ApplicationView container;

	public MapExpressionsAction(ApplicationView container)
	{
		this.container = container;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		try {
			/*
			 * Verify the input resources first
			 */
			verify();
			
			/*
			 * Initialize the renderer
			 */
			container.initRenderer();
			
			// TODO: Move this business logic inside the renderer
			MappingExpressionSet mappingSet = getMappingExpressionsModel().getMappingExpressionSet();
			SpreadSheetDataSource dataSource = getDataSourceModel().getDataSource();
			Workbook workbook = dataSource.getWorkbook();
			for (MappingExpression mapping : mappingSet) {
				if (mapping.isActive()) {
					String sheetName = mapping.getSheetName();
					Sheet sheet = workbook.getSheet(sheetName);
					int startColumn = SpreadSheetUtil.columnName2Number(mapping.getStartColumn());
					int startRow = SpreadSheetUtil.row2Number(mapping.getStartRow());
					int endColumn = mapping.hasEndColumnWildcard()
							? sheet.getRow(startRow).getLastCellNum()
							: SpreadSheetUtil.columnName2Number(mapping.getEndColumn());
					int endRow = mapping.hasEndRowWildcard()
							? sheet.getLastRowNum()
							: SpreadSheetUtil.row2Number(mapping.getEndRow());

					if (startColumn > endColumn) {
						throw new RendererException("start column after finish column in expression " + mapping);
					}
					if (startRow > endRow) {
						throw new RendererException("start row after finish row in expression " + mapping);
					}
					SpreadsheetLocation endLocation = new SpreadsheetLocation(sheetName, endColumn, endRow);
					SpreadsheetLocation startLocation = new SpreadsheetLocation(sheetName, startColumn, startRow);
					SpreadsheetLocation currentLocation = new SpreadsheetLocation(sheetName, startColumn, startRow);

					dataSource.setCurrentLocation(currentLocation);

					evaluate(mapping);
					while (!currentLocation.equals(endLocation)) {
						currentLocation = incrementLocation(currentLocation, startLocation, endLocation);
						dataSource.setCurrentLocation(currentLocation);
						evaluate(mapping);
					}
				}
			}
		}
		catch (MappingMasterException | RendererException ex) {
			getApplicationDialogManager().showErrorMessageDialog(container, ex.getMessage());
		}
	}

	private void verify() throws MappingMasterException
	{
		if (getMappingExpressionsModel().isEmpty()) {
			throw new MappingMasterException("No mappings defined");
		}
		if (getDataSourceModel().isEmpty()) {
			throw new MappingMasterException("No workbook loaded");
		}
	}

	private void evaluate(MappingExpression mapping)
	{
		container.evaluate(mapping, container.getDefaultRenderer());
	}

	private SpreadsheetLocation incrementLocation(SpreadsheetLocation current, SpreadsheetLocation start, SpreadsheetLocation end)
			throws RendererException
	{
		if (current.getPhysicalRowNumber() < end.getPhysicalRowNumber()) {
			return new SpreadsheetLocation(current.getSheetName(), current.getPhysicalColumnNumber(), current.getPhysicalRowNumber());
		}
		if (current.getPhysicalRowNumber() == end.getPhysicalRowNumber()) {
			if (current.getPhysicalColumnNumber() < end.getPhysicalColumnNumber()) {
				return new SpreadsheetLocation(current.getSheetName(), current.getPhysicalColumnNumber(), start.getPhysicalRowNumber());
			}
		}
		throw new RendererException("incrementLocation called redundantly");
	}

	private MappingsExpressionsModel getMappingExpressionsModel()
	{
		return container.getApplicationModel().getMappingExpressionsModel();
	}

	private DataSourceModel getDataSourceModel()
	{
		return container.getApplicationModel().getDataSourceModel();
	}

	private MMApplicationDialogManager getApplicationDialogManager()
	{
		return container.getApplicationDialogManager();
	}
}
