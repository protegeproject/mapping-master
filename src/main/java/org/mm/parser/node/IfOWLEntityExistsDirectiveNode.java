package org.mm.parser.node;

import org.mm.parser.ASTIfExistsDirective;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class IfOWLEntityExistsDirectiveNode implements ReferenceDirectiveNode
{
   private final int ifOWLEntityExistsSetting;

   public IfOWLEntityExistsDirectiveNode(ASTIfExistsDirective node) throws ParseException
   {
      this.ifOWLEntityExistsSetting = node.ifExistsSetting;
   }

   public int getIfOWLEntityExistsSetting()
   {
      return this.ifOWLEntityExistsSetting;
   }

   public String getIfOWLEntityExistsSettingName()
   {
      return ParserUtil.getTokenName(this.ifOWLEntityExistsSetting);
   }

   public boolean isResolveIfOWLEntityExists()
   {
      return this.ifOWLEntityExistsSetting == MM_RESOLVE_IF_OWL_ENTITY_EXISTS;
   }

   public boolean isWarningIfOWLEntityExists()
   {
      return this.ifOWLEntityExistsSetting == MM_WARNING_IF_OWL_ENTITY_EXISTS;
   }

   public boolean isErrorIfOWLEntityExists()
   {
      return this.ifOWLEntityExistsSetting == MM_ERROR_IF_OWL_ENTITY_EXISTS;
   }

   public boolean isSkipIfOWLEntityExists()
   {
      return this.ifOWLEntityExistsSetting == MM_SKIP_IF_OWL_ENTITY_EXISTS;
   }

   @Override
   public String getNodeName()
   {
      return "IfExistsDirective";
   }

   public String toString()
   {
      return getIfOWLEntityExistsSettingName();
   }
}
