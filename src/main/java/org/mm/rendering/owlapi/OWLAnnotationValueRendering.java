package org.mm.rendering.owlapi;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnnotationValue;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class OWLAnnotationValueRendering extends OWLRendering {

   public OWLAnnotationValueRendering(@Nonnull OWLAnnotationValue annotationValue) {
      super(annotationValue);
   }
}
