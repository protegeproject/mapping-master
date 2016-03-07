package org.mm.renderer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class NameUtil
{
   public static boolean isValidUriConstruct(String input)
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

   public static String toUpperCamel(final String text)
   {
      final StringBuilder sb = new StringBuilder();
      for (final String word : text.split("\\s+")) {
         if (!word.isEmpty()) {
            if (word.matches("\\p{javaLowerCase}*")) {
               /*
                * If all the characters are lower-case then upper-case the first
                * letter to conform with CamelCasing.
                */
               sb.append(word.substring(0, 1).toUpperCase());
               sb.append(word.substring(1).toLowerCase());
            } else {
               sb.append(word);
            }
         }
      }
      return sb.toString();
   }

   public static String toLowerCamel(final String text)
   {
      final StringBuilder sb = new StringBuilder();
      
      boolean isFirstWord = true;
      for (final String word : text.split("\\s+")) {
         if (!word.isEmpty()) {
            if (word.matches("\\p{javaLowerCase}*")) {
               if (isFirstWord) {
                  sb.append(word);
               } else {
                  /*
                   * If all the characters are lower-case then upper-case the first
                   * letter to conform with CamelCasing.
                   */
                  sb.append(word.substring(0, 1).toUpperCase());
                  sb.append(word.substring(1).toLowerCase());
               }
            } else {
               sb.append(word);
            }
            isFirstWord = false;
         }
      }
      return sb.toString();
   }

   public static String toSnakeCase(final String text)
   {
      return text.replaceAll("\\s+", "_");
   }

   public static String toUUID()
   {
      return UUID.randomUUID().toString();
   }

   public static String toMD5(final String text)
   {
      return DigestUtils.md5Hex(text);
   }

   public static String removeNonWordCharacters(final String text)
   {
      return removeNonWordCharacters(text, "");
   }

   public static String removeNonWordCharacters(final String text, final String replacement)
   {
      return text.replaceAll("[^\\s^\\p{L}\\p{Nd}]+", replacement);
   }
}
