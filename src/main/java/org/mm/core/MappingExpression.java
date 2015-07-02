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

  public boolean isActive() { return active; }

  public String getExpression() { return expression; }

  public String getComment() { return comment; }

  public String getSourceSheetName() { return sourceSheetName; }

  public String getStartColumn() { return startColumn; }

  public String getFinishColumn() { return finishColumn; }

  public String getStartRow() { return startRow; }

  public String getFinishRow() { return finishRow; }

  public boolean hasFinishColumnWildcard() { return finishColumn.equals(FinishRowOrColumnWildcard); }

  public boolean hasFinishRowWildcard() { return finishRow.equals(FinishRowOrColumnWildcard); }

  public String toString()
  {
    return "(expression: " + expression + ", comment: " + comment +
      ", sourceSheetName: " + sourceSheetName + ", startColumn: " + startColumn + ", finishColumn: " + finishColumn +
      ", startRow: " + startRow + ", finishRow: " + finishRow + ", active: " + active + ")";
  }

}

  
