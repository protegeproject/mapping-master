package org.mm.app;

public class ApplicationStartupException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public ApplicationStartupException(String message)
   {
      super(message);
   }

   public ApplicationStartupException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
