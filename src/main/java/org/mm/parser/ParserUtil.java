package org.mm.parser;

import java.util.HashSet;
import java.util.Set;

public class ParserUtil implements MappingMasterParserConstants
{
  private static Set<String> nodeNames;

  static {
    nodeNames = new HashSet<String>();

    for (int i = 0; i < MappingMasterParserTreeConstants.jjtNodeName.length; i++)
      nodeNames.add(MappingMasterParserTreeConstants.jjtNodeName[i]);
  }

  public static boolean hasName(Node node, String name) throws ParseException
  {
    if (!nodeNames.contains(name))
      throw new ParseException("internal processor error: invalid node name '" + name + "'");

    return node.toString().equals(name);
  }

  public static String getTokenName(int tokenID)
  {
    return tokenImage[tokenID].substring(1, tokenImage[tokenID].length() - 1);
  }

  // Returns -1 if name is invalid
  public static int getTokenID(String tokenName)
  {
    int tokenID = -1;

    for (int i = 0; i < tokenImage.length; i++)
      if (getTokenName(i).equals(tokenName)) {
        tokenID = i;
        break;
      }

    return tokenID;
  }

  /**
   * Get a default type based on the type of the entity.
   */
  public static int getDefaultType(int referenceType)
  {
    if (referenceType == OWL_CLASS)
      return OWL_CLASS;
    else if (referenceType == OWL_NAMED_INDIVIDUAL)
      return OWL_NAMED_INDIVIDUAL;
    else if (referenceType == RDFS_CLASS)
      return RDFS_CLASS;
    else if (referenceType == OWL_OBJECT_PROPERTY)
      return OWL_OBJECT_PROPERTY;
    else if (referenceType == OWL_DATA_PROPERTY)
      return OWL_DATA_PROPERTY;
    else if (referenceType == RDF_PROPERTY)
      return RDF_PROPERTY;
    else
      return referenceType;
  }
}
    
