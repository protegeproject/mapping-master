package org.mm.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.mm.parser.node.MappingMasterParserTreeConstants;
import org.mm.parser.node.Node;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ParserUtils implements MappingMasterParserConstants {

   private final static Set<String> nodeNames;

   static {
      nodeNames = new HashSet<>();
      Collections.addAll(nodeNames, MappingMasterParserTreeConstants.jjtNodeName);
   }

   public static Node getChild(@Nonnull Node parent) {
      return parent.jjtGetChild(0);
   }

   public static <T extends Node> T getChild(@Nonnull Node parent, NodeType<T> type) {
      Node childNode = getChild(parent, type.getName());
      return type.getActualClass().cast(childNode);
   }

   public static <T extends Node> Set<T> getChildren(@Nonnull Node parent, NodeType<T> type) {
      Set<T> childrenNodes = new HashSet<>();
      for (Node childNode : getChildren(parent, type.getName())) {
         T castedChildNode = type.getActualClass().cast(childNode);
         childrenNodes.add(castedChildNode);
      }
      return childrenNodes;
   }

   private static Node getChild(@Nonnull Node parent, @Nonnull String childName) {
      for (int i = 0; i < parent.jjtGetNumChildren(); i++) {
         Node childNode = parent.jjtGetChild(i);
         if (hasName(childNode, childName)) {
            return childNode;
         }
      }
      String errorMessage = String.format("Unknown child '%s' from parent '%s'", childName, parent);
      throw new RuntimeException(errorMessage);
   }

   private static Set<Node> getChildren(@Nonnull Node parent, @Nonnull String childName) {
      Set<Node> childrenNodes = new HashSet<>();
      for (int i = 0; i < parent.jjtGetNumChildren(); i++) {
         Node child = parent.jjtGetChild(i);
         if (hasName(child, childName)) {
            childrenNodes.add(child);
         }
      }
      return childrenNodes;
   }

   private static boolean hasName(@Nonnull Node node, @Nonnull String name) {
      return node.toString().equals(name);
   }

   public static String getTokenName(int tokenId) {
      return tokenImage[tokenId].substring(1, tokenImage[tokenId].length() - 1);
   }

   // Returns -1 if name is invalid
   public static int getTokenId(String tokenName) {
      int tokenId = -1;
      for (int i = 0; i < tokenImage.length; i++) {
         if (getTokenName(i).equals(tokenName)) {
            tokenId = i;
            break;
         }
      }
      return tokenId;
   }
}
