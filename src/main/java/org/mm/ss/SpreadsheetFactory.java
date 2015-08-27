package org.mm.ss;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetFactory
{
	public static Workbook createEmptyWorkbook()
	{
		return new XSSFWorkbook(); // an empty xlsx workbook
	}

	public static Workbook loadWorkbookFromDocument(String location) throws Exception
	{
		return WorkbookFactory.create(new FileInputStream(location));
	}
}
