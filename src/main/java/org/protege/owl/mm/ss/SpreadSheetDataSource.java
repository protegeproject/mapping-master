package org.protege.owl.mm.ss;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import org.protege.owl.mm.DataSource;
import org.protege.owl.mm.exceptions.MappingMasterException;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.node.ReferenceNode;
import org.protege.owl.mm.parser.node.SourceSpecificationNode;
import org.protege.owl.mm.renderer.RendererException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpreadSheetDataSource implements DataSource, MappingMasterParserConstants
{
  private Workbook workbook;
  private SpreadsheetLocation currentLocation;

  public SpreadSheetDataSource()
  {
    workbook = null;
    currentLocation = null;
  }

  public SpreadSheetDataSource(String fileName) throws MappingMasterException
  {
    workbook = null;
    currentLocation = null;

    try {
      workbook = Workbook.getWorkbook(new File(fileName));
    } catch (IOException ex) {
      throw new MappingMasterException("error opening file '" + fileName + "': " + ex.getMessage());
    } catch (jxl.read.biff.BiffException ex) {
      throw new MappingMasterException("error reading data source '" + fileName + "': " + ex.getMessage());
    }
  }

  public void setCurrentLocation(SpreadsheetLocation currentLocation)
  {
    this.currentLocation = currentLocation;
  }

  public SpreadsheetLocation getCurrentLocation()
  {
    return currentLocation;
  }

  public boolean hasCurrentLocation()
  {
    return currentLocation != null;
  }

  public Workbook getWorkbook()
  {
    return workbook;
  }

  public boolean hasWorkbook()
  {
    return workbook != null;
  }

  public List<Sheet> getSheets()
  {
    if (hasWorkbook())
      return Arrays.asList(workbook.getSheets());
    else
      return Collections.emptyList();
  }

  public List<String> getSubSourceNames()
  {
    if (hasWorkbook())
      return Arrays.asList(workbook.getSheetNames());
    else
      return Collections.emptyList();
  }

  public String getLocationValue(SpreadsheetLocation location, ReferenceNode reference) throws RendererException
  {
    String locationValue = getLocationValue(location);

    if (reference.getActualShiftDirective() != MM_NO_SHIFT)
      locationValue = getLocationValueWithShifting(location, reference);

    return locationValue;
  }

  public String getLocationValue(SpreadsheetLocation location) throws RendererException
  {
    Cell cell = null;
    int columnNumber = location.getColumnNumber();
    int rowNumber = location.getRowNumber();

    try {
      cell = location.getSheet().getCell(columnNumber - 1, rowNumber - 1);
    } catch (Exception e) {
      throw new RendererException(
        "error accessing sheet " + location.getSheetName() + " at location " + location + ": " + e.getMessage());
    }

    if (cell.getType() == CellType.EMPTY)
      return null;
    else
      return cell.getContents();
  }

  public String getLocationValueWithShifting(SpreadsheetLocation location, ReferenceNode reference)
    throws RendererException
  {
    String shiftedLocationValue = getLocationValue(location);

    if (shiftedLocationValue == null || shiftedLocationValue.equals("")) {
      if (reference.getActualShiftDirective() == MM_SHIFT_LEFT) {
        for (int currentColumn = location.getColumnNumber() - 1; currentColumn >= 1; currentColumn--) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(location.getSheet(), currentColumn, location.getRowNumber()));
          if (shiftedLocationValue != null)
            return shiftedLocationValue;
        }
      } else if (reference.getActualShiftDirective() == MM_SHIFT_RIGHT) {
        for (int currentColumn = location.getColumnNumber() + 1;
             currentColumn <= location.getSheet().getColumns(); currentColumn++) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(location.getSheet(), currentColumn, location.getRowNumber()));
          if (shiftedLocationValue != null)
            return shiftedLocationValue;
        }
      } else if (reference.getActualShiftDirective() == MM_SHIFT_DOWN) {
        for (int currentRow = location.getRowNumber() + 1; currentRow <= location.getSheet().getRows(); currentRow++) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(location.getSheet(), location.getColumnNumber(), currentRow));
          if (shiftedLocationValue != null)
            return shiftedLocationValue;
        }
      } else if (reference.getActualShiftDirective() == MM_SHIFT_UP) {
        for (int currentRow = location.getRowNumber() - 1; currentRow >= 1; currentRow--) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(location.getSheet(), location.getColumnNumber(), currentRow));
          if (shiftedLocationValue != null)
            return shiftedLocationValue;
        }
      }
      throw new RendererException("unknown shift setting " + reference.getActualShiftDirective());
    } else {
      reference.setShiftedLocation(location);
      return shiftedLocationValue;
    }
  }

  public SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecification) throws RendererException
  {
    Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)"); // ( \* | [a-zA-z]+ ) ( \* | [0-9]+ )
    Matcher m = p.matcher(sourceSpecification.getLocation());
    Sheet sheet = null;
    SpreadsheetLocation resolvedLocation = null;

    if (currentLocation == null)
      throw new RendererException("current location not set");

    if (sourceSpecification.hasSource()) {
      String sheetName = sourceSpecification.getSource();

      if (!hasWorkbook())
        throw new RendererException("sheet name " + sheetName + " specified but there is no active workbook");

      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null)
        throw new RendererException("invalid sheet name " + sheetName);
    } else
      sheet = getCurrentLocation().getSheet();

    if (m.find()) {
      String columnSpecification = m.group(1);
      String rowSpecification = m.group(2);

      if (columnSpecification == null)
        throw new RendererException("missing column specification in location " + sourceSpecification);
      if (rowSpecification == null)
        throw new RendererException("missing row specification in location " + sourceSpecification);

      boolean isColumnWildcard = columnSpecification.equals("*");
      boolean isRowWildcard = rowSpecification.equals("*");
      int columnNumber, rowNumber;

      try {
        if (isColumnWildcard)
          columnNumber = getCurrentLocation().getColumnNumber();
        else
          columnNumber = SpreadSheetUtil.getColumnNumber(sheet, columnSpecification);

        if (isRowWildcard)
          rowNumber = currentLocation.getRowNumber();
        else
          rowNumber = SpreadSheetUtil.getRowNumber(sheet, rowSpecification);
      } catch (MappingMasterException e) {
        throw new RendererException("invalid sourceSpecification " + sourceSpecification + ": " + e.getMessage());
      }

      resolvedLocation = new SpreadsheetLocation(sheet, columnNumber, rowNumber);
    } else
      throw new RendererException("invalid source specification " + sourceSpecification);

    return resolvedLocation;
  }
}
