package org.mm.app;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ApplicationStartupException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public ApplicationStartupException(String message) {
      super(message);
   }

   public ApplicationStartupException(String message, Throwable cause) {
      super(message, cause);
   }
}
