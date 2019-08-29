package org.mm.renderer.internal;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class PropertyName extends ReferencedEntityName {

   public PropertyName(String prefixedName) {
      super(prefixedName);
   }

   public abstract boolean isDataProperty();
   public abstract boolean isObjectProperty();
   public abstract boolean isAnnotationProperty();
}
