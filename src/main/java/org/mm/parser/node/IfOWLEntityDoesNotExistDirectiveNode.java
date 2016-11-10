package org.mm.parser.node;

import org.mm.parser.ASTIfNotExistsDirective;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class IfOWLEntityDoesNotExistDirectiveNode implements ReferenceDirectiveNode
{
   private final int ifOWLEntityDoesNotExistSetting;

   public IfOWLEntityDoesNotExistDirectiveNode(ASTIfNotExistsDirective node) throws ParseException
   {
      this.ifOWLEntityDoesNotExistSetting = node.ifNotExistsSetting;
   }

   public int getIfOWLEntityDoesNotExistSetting()
   {
      return this.ifOWLEntityDoesNotExistSetting;
   }

   public String getIfOWLEntityDoesNotExistSettingName()
   {
      return ParserUtil.getTokenName(this.ifOWLEntityDoesNotExistSetting);
   }

   public boolean isCreateIfOWLEntityDoesNotExist()
   {
      return this.ifOWLEntityDoesNotExistSetting == MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean isWarningIfOWLEntityDoesNotExist()
   {
      return this.ifOWLEntityDoesNotExistSetting == MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean isErrorIfOWLEntityDoesNotExist()
   {
      return this.ifOWLEntityDoesNotExistSetting == MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean isSkipIfOWLEntityDoesNotExist()
   {
      return this.ifOWLEntityDoesNotExistSetting == MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   @Override
   public String getNodeName()
   {
      return "IfNotExistsDirective";
   }

   public String toString()
   {
      return getIfOWLEntityDoesNotExistSettingName();
   }
}
