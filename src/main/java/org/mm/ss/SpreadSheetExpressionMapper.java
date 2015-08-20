package org.mm.ss;

import java.io.ByteArrayInputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPIRenderer;

/**
 * Class that converts a spreadsheet to OWL using the supplied mapping expressions.
 */
public class SpreadSheetExpressionMapper
{
	private static void processMappingExpression(SpreadSheetDataSource dataSource, SpreadsheetLocation currentLocation,
			OWLAPIRenderer renderer, MappingExpression mappingExpression) throws MappingMasterException, ParseException
	{
		String comment = mappingExpression.getComment();
		String expressionText = mappingExpression.getExpression();
		String sourceSheetName = mappingExpression.getSourceSheetName();
		Workbook workbook = dataSource.getWorkbook();
		Sheet sheet = workbook.getSheet(sourceSheetName);
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));
		SimpleNode expressionNode = parser.expression();
		ExpressionNode expression = new ExpressionNode((ASTExpression)expressionNode);
		if (sheet == null)
			throw new RendererException("invalid sheet passed to renderer");

	}

}
