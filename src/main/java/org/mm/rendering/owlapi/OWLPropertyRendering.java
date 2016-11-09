package org.mm.rendering.owlapi;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class OWLPropertyRendering extends OWLRendering {

   public OWLPropertyRendering(@Nonnull OWLProperty property) {
      super(property);
   }
}
