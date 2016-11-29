package org.mm.renderer.owl;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class EntityCreationException extends Exception {

   private static final long serialVersionUID = -2136722917376258331L;

   public EntityCreationException() {
      super();
   }

   public EntityCreationException(String message) {
      super(message);
   }

   public EntityCreationException(String message, Throwable cause) {
      super(message, cause);
   }
}
