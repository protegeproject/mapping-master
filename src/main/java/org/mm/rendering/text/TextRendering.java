package org.mm.rendering.text;

import org.mm.rendering.Rendering;

public class TextRendering implements Rendering
{
   private String rendering;
   private StringBuffer comment = new StringBuffer();

   public TextRendering(String rendering)
   {
      this.rendering = rendering;
   }

   @Override
   public String getRendering()
   {
      return rendering;
   }

   public void addComment(String text)
   {
      if (!text.isEmpty()) {
         if (comment.length() != 0) {
            comment.append(", ").append(text);
         }
         comment.append(text);
      }
   }

   public String getComment()
   {
      return comment.toString();
   }

   public boolean hasComment()
   {
      return !getComment().isEmpty();
   }

   @Override
   public String toString()
   {
      return getRendering();
   }
}
