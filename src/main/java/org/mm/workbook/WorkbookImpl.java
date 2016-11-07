package org.mm.workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.mm.exceptions.MappingMasterException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.RendererException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class WorkbookImpl implements Workbook, MappingMasterParserConstants {

   private final Map<String, Sheet> sheets = new HashMap<>();

   private Optional<CellLocation> currentCellLocation = Optional.empty();

   public WorkbookImpl(@Nonnull org.apache.poi.ss.usermodel.Workbook excelWorkbook) {
      /*
       * Populate the sheets from the workbook
       */
      for (int i = 0; i < excelWorkbook.getNumberOfSheets(); i++) {
         org.apache.poi.ss.usermodel.Sheet excelSheet = excelWorkbook.getSheetAt(i);
         sheets.put(excelSheet.getSheetName(), new SheetImpl(excelSheet));
      }
   }

   public void setCurrentCellLocation(CellLocation location) { // TODO: Revisit this
      currentCellLocation = Optional.of(location);
   }

   public Optional<CellLocation> getCurrentCellLocation() { // TODO: Revisit this
      return currentCellLocation;
   }

   public boolean hasCurrentLocation() { // TODO: Revisit this
      return currentCellLocation != null;
   }

   @Override
   public Sheet getSheet(String sheetName) {
      return sheets.get(sheetName);
   }

   @Override
   public List<Sheet> getSheets() {
      return new ArrayList<Sheet>(sheets.values());
   }

   @Override
   public List<String> getSheetNames() {
      return new ArrayList<String>(sheets.keySet());
   }

   public String getLocationValue(CellLocation cellLocation, ReferenceNode referenceNode)
         throws RendererException {
      String locationValue = getLocationValue(cellLocation);
      if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT) {
         locationValue = getLocationValueWithShifting(cellLocation, referenceNode);
      }
      return locationValue;
   }

   public String getLocationValue(CellLocation cellLocation) throws RendererException {
      Sheet sheet = getSheet(cellLocation.getSheetName());
      Object value = sheet.getCellValue(cellLocation.getRowIndex(), cellLocation.getColumnIndex());
      return String.valueOf(value);
   }

   // TODO: Revisit this method
   public String getLocationValueWithShifting(CellLocation cellLocation,
         ReferenceNode referenceNode) throws RendererException {
      String sheetName = cellLocation.getSheetName();
      Sheet sheet = getSheet(sheetName);
      String shiftedCellLocation = getLocationValue(cellLocation);
      if (shiftedCellLocation == null || shiftedCellLocation.isEmpty()) {
         switch (referenceNode.getActualShiftDirective()) {
            case MM_SHIFT_LEFT :
               int firstColumnNumber = 1;
               for (int currentColumn = cellLocation
                     .getPhysicalColumnNumber(); currentColumn >= firstColumnNumber; currentColumn--) {
                  shiftedCellLocation = getLocationValue(new CellLocation(sheetName,
                        currentColumn, cellLocation.getPhysicalRowNumber()));
                  if (shiftedCellLocation != null && !shiftedCellLocation.isEmpty()) {
                     break;
                  }
               }
               return shiftedCellLocation;
            case MM_SHIFT_RIGHT :
               int lastColumnNumber = sheet.getLastColumnIndexAt(cellLocation.getRowIndex()) + 1;
               for (int currentColumn = cellLocation
                     .getPhysicalColumnNumber(); currentColumn <= lastColumnNumber; currentColumn++) {
                  shiftedCellLocation = getLocationValue(new CellLocation(sheetName,
                        currentColumn, cellLocation.getPhysicalRowNumber()));
                  if (shiftedCellLocation != null && !shiftedCellLocation.isEmpty()) {
                     break;
                  }
               }
               return shiftedCellLocation;
            case MM_SHIFT_DOWN :
               int lastRowNumber = sheet.getLastRowIndex() + 1;
               for (int currentRow = cellLocation
                     .getPhysicalRowNumber(); currentRow <= lastRowNumber; currentRow++) {
                  shiftedCellLocation = getLocationValue(new CellLocation(sheetName,
                        cellLocation.getPhysicalColumnNumber(), currentRow));
                  if (shiftedCellLocation != null && !shiftedCellLocation.isEmpty()) {
                     break;
                  }
               }
               return shiftedCellLocation;
            case MM_SHIFT_UP :
               int firstRowNumber = 1;
               for (int currentRow = cellLocation
                     .getPhysicalRowNumber(); currentRow >= firstRowNumber; currentRow--) {
                  shiftedCellLocation = getLocationValue(new CellLocation(sheetName,
                        cellLocation.getPhysicalColumnNumber(), currentRow));
                  if (shiftedCellLocation != null && !shiftedCellLocation.isEmpty()) {
                     break;
                  }
               }
               return shiftedCellLocation;
            default :
               throw new InternalRendererException(
                     "Unknown shift setting " + referenceNode.getActualShiftDirective());
         }
      } else {
         referenceNode.setShiftedLocation(cellLocation);
         return shiftedCellLocation;
      }
   }

   // TODO: Break down this method
   public CellLocation resolveLocation(SourceSpecificationNode sourceSpecification)
         throws RendererException {
      Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)");
      Matcher m = p.matcher(sourceSpecification.getLocation());
      Sheet sheet;
      CellLocation resolvedLocation;

      if (!currentCellLocation.isPresent()) {
         throw new RendererException("current location not set");
      }
      if (sourceSpecification.hasSource()) {
         String sheetName = sourceSpecification.getSource();
         sheet = getSheet(sheetName);
         if (sheet == null) {
            throw new RendererException("Sheet name '" + sheetName + "' does not exist");
         }
      } else {
         String sheetName = getCurrentCellLocation().get().getSheetName();
         sheet = getSheet(sheetName);
         if (sheet == null) {
            throw new RendererException("Sheet name '" + sheetName + "' does not exist");
         }
      }

      if (m.find()) {
         String columnSpecification = m.group(1);
         String rowSpecification = m.group(2);

         if (columnSpecification == null) {
            throw new RendererException(
                  "Missing column specification in location " + sourceSpecification);
         }
         if (rowSpecification == null) {
            throw new RendererException(
                  "Missing row specification in location " + sourceSpecification);
         }
         boolean isColumnWildcard = "*".equals(columnSpecification);
         boolean isRowWildcard = "*".equals(rowSpecification);
         int columnNumber, rowNumber;

         try {
            if (isColumnWildcard) {
               columnNumber = getCurrentCellLocation().get().getPhysicalColumnNumber();
            } else {
               columnNumber = WorkbookUtils.columnName2Number(columnSpecification);
            }
            if (isRowWildcard) {
               rowNumber = getCurrentCellLocation().get().getPhysicalRowNumber();
            } else {
               rowNumber = WorkbookUtils.rowLabel2Number(rowSpecification);
            }
         } catch (MappingMasterException e) {
            throw new RendererException(
                  "Invalid source specification " + sourceSpecification + " - " + e.getMessage());
         }
         resolvedLocation = new CellLocation(sheet.getName(), columnNumber, rowNumber);
      } else {
         throw new RendererException("Invalid source specification " + sourceSpecification);
      }
      return resolvedLocation;
   }
}
