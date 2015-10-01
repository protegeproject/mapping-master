package org.mm.core;

public class TransformationRule
{
   public static final String EndWildcard = "+";

   private String sheetName;
   private String startColumn;
   private String endColumn;
   private String startRow;
   private String endRow;
   private String comment;
   private String rule;
   private boolean active;

   public TransformationRule(String sheetName, String startColumn, String endColumn, String startRow, String endRow,
         String comment, String rule)
   {
      this.active = true;
      this.sheetName = sheetName;
      this.startColumn = startColumn;
      this.endColumn = endColumn;
      this.startRow = startRow;
      this.endRow = endRow;
      this.comment = comment;
      this.rule = rule;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public boolean isActive()
   {
      return active;
   }

   public String getRuleString()
   {
      return rule;
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
      return "TransformationRule [" + "sheetName=" + sheetName + ", " + "startColumn=" + startColumn + ", "
            + "endColumn=" + endColumn + ", " + "startRow=" + startRow + ", " + "endRow=" + endRow + ", "
            + "expression=" + rule + ", " + "comment=" + comment + ", " + "active=" + active + "]";
   }
}