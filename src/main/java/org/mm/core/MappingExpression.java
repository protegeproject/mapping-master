package org.mm.core;

public class MappingExpression
{
  public static final String FinishRowOrColumnWildcard = "+";

  private String comment, expression, sourceSheetName;
  private String startColumn, finishColumn;
  private String startRow, finishRow;
  private boolean active;

  public MappingExpression(String comment, String expression, String sourceSheetName, String startColumn,
    String finishColumn, String startRow, String finishRow)
  {
    this.active = true;
    this.expression = expression;
    this.comment = comment;
    this.sourceSheetName = sourceSheetName;
    this.startColumn = startColumn;
    this.finishColumn = finishColumn;
    this.startRow = startRow;
    this.finishRow = finishRow;
  }

  public void update(String comment, String expression, String sourceSheetName, String startColumn, String finishColumn,
    String startRow, String finishRow)
  {
    this.comment = comment;
    this.expression = expression;
    this.sourceSheetName = sourceSheetName;
    this.startColumn = startColumn;
    this.finishColumn = finishColumn;
    this.startRow = startRow;
    this.finishRow = finishRow;
  }

  public void setActive(boolean active) { this.active = active; }

  public boolean isActive() { return this.active; }

  public String getExpression() { return this.expression; }

  public String getComment() { return this.comment; }

  public String getSourceSheetName() { return this.sourceSheetName; }

  public String getStartColumn() { return this.startColumn; }

  public String getFinishColumn() { return this.finishColumn; }

  public String getStartRow() { return this.startRow; }

  public String getFinishRow() { return this.finishRow; }

  public boolean hasFinishColumnWildcard() { return this.finishColumn.equals(FinishRowOrColumnWildcard); }

  public boolean hasFinishRowWildcard() { return this.finishRow.equals(FinishRowOrColumnWildcard); }

  public String toString()
  {
    return "(expression: " + this.expression + ", comment: " + this.comment +
      ", sourceSheetName: " + this.sourceSheetName + ", startColumn: " + this.startColumn
      + ", finishColumn: " + this.finishColumn +
      ", startRow: " + this.startRow + ", finishRow: " + this.finishRow + ", active: " + this.active + ")";
  }

}

  
