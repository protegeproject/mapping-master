package org.mm.renderer.owlapi;

import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

public class OWLAPILiteralRenderer implements OWLLiteralRenderer
{
   private final OWLAPIObjectHandler handler;

   public OWLAPILiteralRenderer(OWLOntology ontology)
   {
      handler = new OWLAPIObjectHandler(ontology);
   }

   @Override
   public Optional<OWLAPILiteralRendering> renderOWLLiteral(OWLLiteralNode node) throws RendererException
   {
      return Optional.of(new OWLAPILiteralRendering(createOWLLiteral(node)));
   }

   public OWLLiteral createOWLLiteral(OWLLiteralNode node) throws RendererException
   {
      if (node.isString()) {
         return handler.getOWLLiteralString(node.getStringLiteralNode().getValue());
      } else if (node.isInt()) {
         return handler.getOWLLiteralInteger(node.getIntLiteralNode().getValue() + "");
      } else if (node.isFloat()) {
         return handler.getOWLLiteralFloat(node.getFloatLiteralNode().getValue() + "");
      } else if (node.isBoolean()) {
         return handler.getOWLLiteralBoolean(node.getBooleanLiteralNode().getValue() + "");
      } else {
         throw new InternalRendererException("unsupported datatype for node " + node);
      }
   }

   public OWLLiteral createOWLLiteral(String value, ReferenceType type) throws RendererException
   {
      if (type.isXSDBoolean())
         return handler.getOWLLiteralBoolean(value);
      else if (type.isXSDString())
         return handler.getOWLLiteralString(value);
      else if (type.isXSDDecimal())
         return handler.getOWLLiteralDecimal(value);
      else if (type.isXSDByte())
         return handler.getOWLLiteralByte(value);
      else if (type.isXSDShort())
         return handler.getOWLLiteralShort(value);
      else if (type.isXSDInt())
         return handler.getOWLLiteralInteger(value);
      else if (type.isXSDLong())
         return handler.getOWLLiteralLong(value);
      else if (type.isXSDFloat())
         return handler.getOWLLiteralFloat(value);
      else if (type.isXSDDouble())
         return handler.getOWLLiteralDouble(value);
      else if (type.isXSDDateTime())
         return handler.getOWLLiteralDateTime(value);
      else if (type.isXSDDate())
         return handler.getOWLLiteralDate(value);
      else if (type.isXSDTime())
         return handler.getOWLLiteralTime(value);
      else if (type.isXSDDuration())
         return handler.getOWLLiteralDuration(value);
      else throw new RendererException("unknown type " + type.getTypeName() + " for literal " + value);
   }

   public OWLLiteral createOWLLiteral(String value) throws RendererException
   {
      return handler.getOWLLiteralString(value);
   }
}
