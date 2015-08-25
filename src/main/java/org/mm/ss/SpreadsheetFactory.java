package org.mm.ss;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SpreadsheetFactory
{
	public static Workbook createEmptyWorkbook()
	{
		return null;
	}

	public static Workbook loadWorkbookFromDocument(String location) throws Exception
	{
		return WorkbookFactory.create(new FileInputStream(location));
	}
}
