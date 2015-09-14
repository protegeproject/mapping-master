package org.mm.core;

public class MappingExpression
{
   public static final String EndWildcard = "+";

   private String sheetName;
   private String startColumn;
   private String endColumn;
   private String startRow;
   private String endRow;
   private String comment;
   private String expression;
   private boolean active;

   public MappingExpression(String sheetName, String startColumn, String endColumn, String startRow, String endRow,
         String comment, String expression)
   {
      this.active = true;
      this.sheetName = sheetName;
      this.startColumn = startColumn;
      this.endColumn = endColumn;
      this.startRow = startRow;
      this.endRow = endRow;
      this.comment = comment;
      this.expression = expression;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public boolean isActive()
   {
      return active;
   }

   public String getExpressionString()
   {
      return expression;
   }

   public String getComment()
   {
      return comment;
   }

   public String getSheetName()
   {
      return sheetName;
   }

   public String getStartColumn()
   {
      return startColumn;
   }

   public String getEndColumn()
   {
      return endColumn;
   }

   public String getStartRow()
   {
      return startRow;
   }

   public String getEndRow()
   {
      return endRow;
   }

   public boolean hasEndColumnWildcard()
   {
      return endColumn.equals(EndWildcard);
   }

   public boolean hasEndRowWildcard()
   {
      return endRow.equals(EndWildcard);
   }

   public String toString()
   {
      return "MappingExpression [" + "sheetName=" + sheetName + ", " + "startColumn=" + startColumn + ", "
            + "endColumn=" + endColumn + ", " + "startRow=" + startRow + ", " + "endRow=" + endRow + ", "
            + "expression=" + expression + ", " + "comment=" + comment + ", " + "active=" + active + "]";
   }
}