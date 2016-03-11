package org.mm.rendering.owlapi;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.rendering.LiteralRendering;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

public abstract class OWLAnnotationValueRendering extends OWLRendering implements LiteralRendering, MappingMasterParserConstants
{
   private final OWLAnnotationValue annotationValue;

   public OWLAnnotationValueRendering(OWLAnnotationValue annotationValue)
   {
      super();
      this.annotationValue = annotationValue;
   }

   public OWLAnnotationValue getOWLAnnotationValue()
   {
      return annotationValue;
   }
}
