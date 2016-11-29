package org.mm.renderer.owl;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class EntityNotFoundException extends Exception {

   private static final long serialVersionUID = 1L;

   public EntityNotFoundException() {
      super();
   }

   public EntityNotFoundException(String message) {
      super(message);
   }

   public EntityNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
