package org.mm.exceptions;

public class EntityNotFoundException extends MappingMasterException
{
   private static final long serialVersionUID = -4371140463863734874L;

   public EntityNotFoundException()
   {
      super();
   }

   public EntityNotFoundException(String message)
   {
      super(message);
   }

   public EntityNotFoundException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
