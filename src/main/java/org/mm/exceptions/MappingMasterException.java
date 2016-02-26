package org.mm.exceptions;

@SuppressWarnings("serial")
public class MappingMasterException extends Exception
{
   public MappingMasterException()
   {
      super();
   }

   public MappingMasterException(String message)
   {
      super(message);
   }

   public MappingMasterException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
