package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.OWLObject;

import com.google.common.base.Objects;

public abstract class OWLObjectRendering implements OWLRendering {

   private final OWLObject object;

   public OWLObjectRendering(@Nonnull OWLObject object) {
      this.object = checkNotNull(object);
   }

   public abstract OWLObject getOWLObject();

   @Override
   public String getRendering() {
      return new ManchesterOWLSyntaxOWLObjectRendererImpl().render(object);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(object);
   }

   @Override
   public String toString() {
      return getRendering();
   }
}
