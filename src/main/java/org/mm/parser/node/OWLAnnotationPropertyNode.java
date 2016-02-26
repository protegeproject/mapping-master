package org.mm.parser.node;

import org.mm.parser.ASTOWLAnnotationProperty;
import org.mm.parser.ParseException;

public class OWLAnnotationPropertyNode extends OWLPropertyNode
{
   public OWLAnnotationPropertyNode(ASTOWLAnnotationProperty node) throws ParseException
   {
      super(node);
   }

   @Override
   public String getNodeName()
   {
      return "OWLAnnotationProperty";
   }
}
