package org.mm.ss;

import jxl.Sheet;
import jxl.Workbook;
import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPIRenderer;

import java.io.ByteArrayInputStream;
import java.util.Set;

/**
 * Class that converts a spreadsheet to OWL using the supplied mapping expressions.
 */
public class SpreadSheetExpressionMapper
{
	public static void map(OWLAPIRenderer renderer, Set<MappingExpression> mappingExpressions)
			throws MappingMasterException
	{
		try {
			processMappingExpressions(renderer, mappingExpressions);
		} catch (RendererException e) {
			e.printStackTrace();
			throw new MappingMasterException(e.getMessage());
		}
	}

	private static void processMappingExpressions(OWLAPIRenderer renderer, Set<MappingExpression> mappingExpressions)
			throws RendererException
	{
		SpreadsheetLocation currentLocation = null;
		renderer.reset();
		renderer.setCreateEntities(true);

		for (MappingExpression mappingExpression : mappingExpressions) {
			try {
				processMappingExpression(renderer, mappingExpression, currentLocation);
			} catch (MappingMasterException e) {
				throw new RendererException("error processing expression\n" + mappingExpression + "\n" + e.getMessage());
			} catch (ParseException e) {
				if (currentLocation != null)
					throw new RendererException(
							"error rendering expression\n" + mappingExpression.getExpression() + "\nat sheet \"" + currentLocation
									.getSheetName() + "\", current column " + currentLocation.getColumnName() + ", " + "current row "
									+ currentLocation.getRowNumber() + "\n" + e.getMessage());
				else
					throw new RendererException(
							"error rendering expression\n" + mappingExpression.getExpression() + "\n" + e.getMessage());
			}
		}
	}

	private static void processMappingExpression(OWLAPIRenderer renderer, MappingExpression mappingExpression,
			SpreadsheetLocation currentLocation) throws MappingMasterException, ParseException
	{
		String comment = mappingExpression.getComment();
		String expressionText = mappingExpression.getExpression();
		String sourceSheetName = mappingExpression.getSourceSheetName();
		Workbook workbook = renderer.getDataSource().getWorkbook();
		Sheet sheet = workbook.getSheet(sourceSheetName);
		int startColumnNumber = SpreadSheetUtil.columnName2Number(mappingExpression.getStartColumn());
		int startRowNumber = SpreadSheetUtil.row2Number(mappingExpression.getStartRow());
		int finishColumnNumber = mappingExpression.hasFinishColumnWildcard() ?
				sheet.getColumns() :
				SpreadSheetUtil.columnName2Number(mappingExpression.getFinishColumn());
		int finishRowNumber = mappingExpression.hasFinishRowWildcard() ?
				sheet.getRows() :
				SpreadSheetUtil.row2Number(mappingExpression.getFinishRow());
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));
		SimpleNode expressionNode = parser.expression();
		ExpressionNode expression = new ExpressionNode((ASTExpression)expressionNode);
		SpreadsheetLocation finishLocation = new SpreadsheetLocation(sheet, finishColumnNumber, finishRowNumber);
		SpreadsheetLocation startLocation = new SpreadsheetLocation(sheet, startColumnNumber, startRowNumber);

		currentLocation = new SpreadsheetLocation(sheet, startColumnNumber, startRowNumber);

		if (sheet == null)
			throw new RendererException("invalid sheet passed to renderer");

		if (startColumnNumber > finishColumnNumber)
			throw new RendererException("start column after finish column in expression " + mappingExpression);
		if (startRowNumber > finishRowNumber)
			throw new RendererException("start row after finish row in expression " + mappingExpression);

		System.err.println("**********************************************************************************");
		System.err.println("********************** Processing Mapping Master Expression **********************");
		System.err.println("Grid range: " + sheet.getName() + "!" + startLocation.getCellLocation() + ":" + finishLocation
				.getCellLocation());
		System.err.println("Comment: " + comment);
		System.err.println("Expression: ");
		System.err.println(expressionText);

		renderer.getDataSource().setCurrentLocation(currentLocation);
		renderer.renderExpression(expression);

		while (!currentLocation.equals(finishLocation)) {
			incrementLocation(currentLocation, startLocation, finishLocation);
			renderer.getDataSource().setCurrentLocation(currentLocation);
			renderer.renderExpression(expression);
		}
	}

	private static void incrementLocation(SpreadsheetLocation currentLocation, SpreadsheetLocation startLocation,
			SpreadsheetLocation finishLocation) throws RendererException
	{
		if (currentLocation == finishLocation)
			throw new RendererException("internalError: incrementLocation called redundantly");

		if (currentLocation.getRowNumber() < finishLocation.getRowNumber())
			currentLocation.incrementRowNumber();
		else if (currentLocation.getRowNumber() == finishLocation.getRowNumber()) {
			if (currentLocation.getColumnNumber() < finishLocation.getColumnNumber()) {
				currentLocation.incrementColumnNumber();
				currentLocation.setRowNumber(startLocation.getRowNumber());
			}
		}
	}
}
