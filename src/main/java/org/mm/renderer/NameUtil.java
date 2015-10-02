package org.mm.renderer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class NameUtil
{
   public static boolean isValidIriString(String input)
   {
      URL u = null;

      /*
       * Parse based on URL construct
       */
      try {
         u = new URL(input);
      } catch (MalformedURLException e) {
         return false;
      }

      /*
       * Check if it complies with RFC 2396 about URI Generic Syntax
       */
      try {
         u.toURI();
      } catch (URISyntaxException e) {
         return false;
      }
      
      return true;
   }
}
