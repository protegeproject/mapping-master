package org.mm.rendering.owlapi;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class OWLClassExpressionRendering extends OWLRendering {

   public OWLClassExpressionRendering(@Nonnull OWLClassExpression classExpression) {
      super(classExpression);
   }
}
