package org.mm.renderer.owlapi;

import java.util.Optional;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.semanticweb.owlapi.model.OWLLiteral;

public class OWLAPILiteralRenderer implements OWLLiteralRenderer
{
   private final OWLAPIObjectHandler objectFactory;

   public OWLAPILiteralRenderer(OWLAPIObjectHandler objectFactory)
   {
      this.objectFactory = objectFactory;
   }

   @Override
   public Optional<OWLAPILiteralRendering> renderOWLLiteral(OWLLiteralNode node) throws RendererException
   {
      OWLLiteral lit = createOWLLiteral(node);
      return Optional.of(new OWLAPILiteralRendering(lit));
   }

   private OWLLiteral createOWLLiteral(OWLLiteralNode node) throws RendererException
   {
      if (node.isString()) {
         return objectFactory.getOWLLiteralString(node.getStringLiteralNode().getValue());
      } else if (node.isInt()) {
         return objectFactory.getOWLLiteralInteger(node.getIntLiteralNode().getValue() + "");
      } else if (node.isFloat()) {
         return objectFactory.getOWLLiteralFloat(node.getFloatLiteralNode().getValue() + "");
      } else if (node.isBoolean()) {
         return objectFactory.getOWLLiteralBoolean(node.getBooleanLiteralNode().getValue() + "");
      } else {
         throw new InternalRendererException("Unsupported datatype for node " + node);
      }
   }
}
