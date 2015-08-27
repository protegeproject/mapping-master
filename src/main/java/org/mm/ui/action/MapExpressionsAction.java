package org.mm.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.renderer.RendererException;
import org.mm.rendering.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadSheetUtil;
import org.mm.ss.SpreadsheetLocation;
import org.mm.ui.Environment;
import org.mm.ui.dialog.MMDialogManager;
import org.mm.ui.model.DataSourceModel;
import org.mm.ui.view.ApplicationView;
import org.mm.ui.view.MappingControlView;

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
			
			// TODO: Move this business logic inside the renderer
			List<Rendering> results = new ArrayList<Rendering>();
			List<MappingExpression> mappings = getMappingExpressions();
			SpreadSheetDataSource dataSource = getDataSourceModel().getDataSource();
			Workbook workbook = dataSource.getWorkbook();
			for (MappingExpression mapping : mappings) {
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

					evaluate(mapping, results);
					while (!currentLocation.equals(endLocation)) {
						currentLocation = incrementLocation(currentLocation, startLocation, endLocation);
						dataSource.setCurrentLocation(currentLocation);
						evaluate(mapping, results);
					}
				}
			}
			printResults(results);
		}
		catch (Exception ex) {
			getApplicationDialogManager().showErrorMessageDialog(container, ex.getMessage());
		}
	}

	private void printResults(List<Rendering> results)
	{
		final MappingControlView view = container.getMappingsControlView();
		view.messageAreaClear();
		view.messageAreaAppend("MappingMaster v" + Environment.MAPPINGMASTER_VERSION + "\n\n");
		view.messageAreaAppend("Successfully rendering " + results.size() + " axioms.\n");
		for (Rendering rendering : results) {
			view.messageAreaAppend(rendering + "\n");
		}
	}

	private void verify() throws MappingMasterException
	{
		if (getMappingExpressions().isEmpty()) {
			throw new MappingMasterException("No mappings defined");
		}
		if (getDataSourceModel().isEmpty()) {
			throw new MappingMasterException("No workbook loaded");
		}
	}

	private void evaluate(MappingExpression mapping, List<Rendering> results) throws ParseException
	{
		container.evaluate(mapping, container.getDefaultRenderer(), results);
	}

	private SpreadsheetLocation incrementLocation(SpreadsheetLocation current, SpreadsheetLocation start, SpreadsheetLocation end)
			throws RendererException
	{
		if (current.getPhysicalRowNumber() < end.getPhysicalRowNumber()) {
			return new SpreadsheetLocation(current.getSheetName(), current.getPhysicalColumnNumber(), current.getPhysicalRowNumber() + 1);
		}
		if (current.getPhysicalRowNumber() == end.getPhysicalRowNumber()) {
			if (current.getPhysicalColumnNumber() < end.getPhysicalColumnNumber()) {
				return new SpreadsheetLocation(current.getSheetName(), current.getPhysicalColumnNumber() + 1, start.getPhysicalRowNumber());
			}
		}
		throw new RendererException("incrementLocation called redundantly");
	}

	private List<MappingExpression> getMappingExpressions()
	{
		return container.getMappingBrowserView().getMappingExpressions();
	}

	private DataSourceModel getDataSourceModel()
	{
		return container.getApplicationModel().getDataSourceModel();
	}

	private MMDialogManager getApplicationDialogManager()
	{
		return container.getApplicationDialogManager();
	}
}
