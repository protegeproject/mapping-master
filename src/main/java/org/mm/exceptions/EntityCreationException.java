package org.mm.exceptions;

public class EntityCreationException extends MappingMasterException
{
   private static final long serialVersionUID = -2136722917376258331L;

   public EntityCreationException()
   {
      super();
   }

   public EntityCreationException(String message)
   {
      super(message);
   }

   public EntityCreationException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
