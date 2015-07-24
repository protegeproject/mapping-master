package org.mm.parser.node;

import org.mm.core.ReferenceDirectives;
import org.mm.parser.ASTDefaultDataValue;
import org.mm.parser.ASTDefaultID;
import org.mm.parser.ASTDefaultLabel;
import org.mm.parser.ASTDefaultLocationValue;
import org.mm.parser.ASTEmptyDataValueSetting;
import org.mm.parser.ASTEmptyLocationSetting;
import org.mm.parser.ASTEmptyRDFIDSetting;
import org.mm.parser.ASTEmptyRDFSLabelSetting;
import org.mm.parser.ASTIfExistsDirective;
import org.mm.parser.ASTIfNotExistsDirective;
import org.mm.parser.ASTLanguage;
import org.mm.parser.ASTNamespace;
import org.mm.parser.ASTPrefix;
import org.mm.parser.ASTReference;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.ASTShiftSetting;
import org.mm.parser.ASTSourceSpecification;
import org.mm.parser.ASTTypes;
import org.mm.parser.ASTValueEncoding;
import org.mm.parser.ASTValueExtractionFunction;
import org.mm.parser.InternalParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;
import org.mm.renderer.RendererException;
import org.mm.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;

public class ReferenceNode implements TypeNode, MappingMasterParserConstants
{
  private SourceSpecificationNode sourceSpecificationNode;
  private ReferenceTypeNode referenceTypeNode;
  private PrefixNode prefixNode;
  private NamespaceNode namespaceNode;
  private LanguageNode languageNode;
  private TypesNode typesNode;
  private DefaultLocationValueNode defaultLocationValueNode;
  private DefaultDataValueNode defaultDataValueNode;
  private DefaultIDNode defaultRDFIDNode;
  private DefaultLabelNode defaultRDFSLabelNode;
  private EmptyLocationDirectiveNode emptyLocationDirectiveNode;
  private EmptyDataValueDirectiveNode emptyDataValueDirectiveNode;
  private EmptyRDFIDDirectiveNode emptyRDFIDDirectiveNode;
  private EmptyRDFSLabelDirectiveNode emptyRDFSLabelDirectiveNode;
  private IfExistsDirectiveNode ifExistsDirectiveNode;
  private IfNotExistsDirectiveNode ifNotExistsDirectiveNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;
  private ShiftDirectiveNode shiftDirectiveNode;
  private final ReferenceDirectives referenceDirectives;
	private final List<ValueEncodingNode> valueEncodingsNodes = new ArrayList<>();

  public ReferenceNode(ASTReference node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "SourceSpecification")) {
        this.sourceSpecificationNode = new SourceSpecificationNode((ASTSourceSpecification)child);
      } else if (ParserUtil.hasName(child, "ReferenceType")) {
        this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType)child);
      } else if (ParserUtil.hasName(child, "Prefix")) {
        if (this.prefixNode != null)
          throw new RendererException("only one prefix directive can be specified for a Reference");
        this.prefixNode = new PrefixNode((ASTPrefix)child);
      } else if (ParserUtil.hasName(child, "Language")) {
        if (this.languageNode != null)
          throw new RendererException("only one language directive can be specified for a Reference");
        this.languageNode = new LanguageNode((ASTLanguage)child);
      } else if (ParserUtil.hasName(child, "Namespace")) {
        if (this.namespaceNode != null)
          throw new RendererException("only one namespace directive can be specified for a Reference");
        this.namespaceNode = new NamespaceNode((ASTNamespace)child);
      } else if (ParserUtil.hasName(child, "ValueEncoding")) {
        this.valueEncodingsNodes.add(new ValueEncodingNode((ASTValueEncoding)child));
      } else if (ParserUtil.hasName(child, "DefaultLocationValue")) {
        if (this.defaultLocationValueNode != null)
          throw new RendererException("only one default location value directive can be specified for a Reference");
        this.defaultLocationValueNode = new DefaultLocationValueNode((ASTDefaultLocationValue)child);
      } else if (ParserUtil.hasName(child, "DefaultDataValue")) {
        if (this.defaultDataValueNode != null)
          throw new RendererException("only one default data value directive can be specified for a Reference");
        this.defaultDataValueNode = new DefaultDataValueNode((ASTDefaultDataValue)child);
      } else if (ParserUtil.hasName(child, "DefaultID")) {
        if (this.defaultRDFIDNode != null)
          throw new RendererException("only one default ID directive can be specified for a Reference");
        this.defaultRDFIDNode = new DefaultIDNode((ASTDefaultID)child);
      } else if (ParserUtil.hasName(child, "DefaultLabel")) {
        if (this.defaultRDFSLabelNode != null)
          throw new RendererException("only one default label directive can be specified for a Reference");
        this.defaultRDFSLabelNode = new DefaultLabelNode((ASTDefaultLabel)child);
      } else if (ParserUtil.hasName(child, "EmptyLocationSetting")) {
        if (this.emptyLocationDirectiveNode != null)
          throw new RendererException("only one empty location directive can be specified for a Reference");
        this.emptyLocationDirectiveNode = new EmptyLocationDirectiveNode((ASTEmptyLocationSetting)child);
      } else if (ParserUtil.hasName(child, "IfExistsDirective")) {
        if (this.ifExistsDirectiveNode != null)
          throw new RendererException("only one if exists directive can be specified for a Reference");
        this.ifExistsDirectiveNode = new IfExistsDirectiveNode((ASTIfExistsDirective)child);
      } else if (ParserUtil.hasName(child, "IfNotExistsDirective")) {
        if (this.ifNotExistsDirectiveNode != null)
          throw new RendererException("only one if not exists directive can be specified for a Reference");
        this.ifNotExistsDirectiveNode = new IfNotExistsDirectiveNode((ASTIfNotExistsDirective)child);
      } else if (ParserUtil.hasName(child, "EmptyDataValueSetting")) {
        if (this.emptyDataValueDirectiveNode != null)
          throw new RendererException("only one empty data value directive can be specified for a Reference");
        this.emptyDataValueDirectiveNode = new EmptyDataValueDirectiveNode((ASTEmptyDataValueSetting)child);
      } else if (ParserUtil.hasName(child, "EmptyRDFIDSetting")) {
        if (this.emptyRDFIDDirectiveNode != null)
          throw new RendererException("only one empty rdf:ID directive can be specified for a Reference");
        this.emptyRDFIDDirectiveNode = new EmptyRDFIDDirectiveNode((ASTEmptyRDFIDSetting)child);
      } else if (ParserUtil.hasName(child, "EmptyRDFSLabelSetting")) {
        if (this.emptyRDFSLabelDirectiveNode != null)
          throw new RendererException("only one empty rdfs:Label directive can be specified for a Reference");
        this.emptyRDFSLabelDirectiveNode = new EmptyRDFSLabelDirectiveNode((ASTEmptyRDFSLabelSetting)child);
      } else if (ParserUtil.hasName(child, "ShiftSetting")) {
        if (this.shiftDirectiveNode != null)
          throw new RendererException("only one shift setting directive can be specified for a Reference");
        this.shiftDirectiveNode = new ShiftDirectiveNode((ASTShiftSetting)child);
      } else if (ParserUtil.hasName(child, "ValueExtractionFunction")) {
        if (this.valueExtractionFunctionNode != null)
          throw new RendererException("only one value extraction directive can be specified for a Reference");
        this.valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
      } else if (ParserUtil.hasName(child, "Types")) {
        this.typesNode = new TypesNode((ASTTypes)child);
      } else
        throw new InternalParseException("invalid child node " + child + " for ReferenceNode");
    }

    this.referenceDirectives = new ReferenceDirectives(node.defaultReferenceDirectives);

    if (this.sourceSpecificationNode == null)
      throw new RendererException("missing source specification in reference " + toString());

    if (this.referenceTypeNode == null) { // No entity type specified by the user - use default type
      this.referenceTypeNode = new ReferenceTypeNode(node.defaultReferenceDirectives.getDefaultReferenceType());
    } else
      this.referenceDirectives.setExplicitlySpecifiedReferenceType(this.referenceTypeNode.getReferenceType());

    if (this.valueEncodingsNodes.isEmpty()) {
      this.valueEncodingsNodes.add(new ValueEncodingNode(node.defaultReferenceDirectives.getDefaultValueEncoding()));
    } else {
      this.referenceDirectives.setHasExplicitlySpecifiedValueEncodings();
      for (ValueEncodingNode valueEncoding : getValueEncodingNodes()) {
        if (valueEncoding.useLocationEncoding())
          this.referenceDirectives.setUsesLocationEncoding();
        if (valueEncoding.hasLocationWithDuplicatesEncoding())
          this.referenceDirectives.setUsesLocationWithDuplicatesEncoding();
      }
    }

    if (this.defaultLocationValueNode != null)
      this.referenceDirectives
        .setExplicitlySpecifiedDefaultLocationValue(this.defaultLocationValueNode.getDefaultLocationValue());

    if (this.defaultDataValueNode != null)
      this.referenceDirectives.setExplicitlySpecifiedDefaultDataValue(this.defaultDataValueNode.getDefaultDataValue());

    if (this.defaultRDFIDNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedDefaultID(this.defaultRDFIDNode.getDefaultRDFID());

    if (this.defaultRDFSLabelNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedDefaultLabel(this.defaultRDFSLabelNode.getDefaultRDFSLabel());

    if (this.languageNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedLanguage(this.languageNode.getLanguage());

    if (this.prefixNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedPrefix(this.prefixNode.getPrefix());

    if (this.namespaceNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedNamespace(this.namespaceNode.getNamespace());

    if (this.emptyLocationDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyLocationDirective(this.emptyLocationDirectiveNode.getEmptyLocationSetting());

    if (this.emptyDataValueDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyDataValueDirective(this.emptyDataValueDirectiveNode.getEmptyDataValueSetting());

    if (this.emptyRDFIDDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyRDFIDDirective(this.emptyRDFIDDirectiveNode.getEmptyRDFIDSetting());

    if (this.emptyRDFSLabelDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyRDFSLabelDirective(this.emptyRDFSLabelDirectiveNode.getEmptyRDFSLabelSetting());

    if (this.shiftDirectiveNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedShiftDirective(this.shiftDirectiveNode.getShiftSetting());

    if (this.ifExistsDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedIfExistsDirective(this.ifExistsDirectiveNode.getIfExistsSetting());

    if (this.ifNotExistsDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedIfNotExistsDirective(this.ifNotExistsDirectiveNode.getIfNotExistsSetting());

    if (this.typesNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedTypes();

    checkValueEncodings();
    checkInvalidExplicitDirectives();
  }

  @Override public String getNodeName()
  {
    return "Reference";
  }

  public ReferenceDirectives getReferenceDirectives()
  {
    return this.referenceDirectives;
  }

  public SourceSpecificationNode getSourceSpecificationNode()
  {
    return this.sourceSpecificationNode;
  }

  public void updateReferenceType(int type)
  {
    this.referenceTypeNode = new ReferenceTypeNode(type);
  }

  public ReferenceTypeNode getReferenceTypeNode()
  {
    return this.referenceTypeNode;
  }

  public List<ValueEncodingNode> getValueEncodingNodes()
  {
    return this.valueEncodingsNodes;
  }

  public TypesNode getTypesNode()
  {
    return this.typesNode;
  }

  public DefaultDataValueNode getDefaultDataValueNode()
  {
    return this.defaultDataValueNode;
  }

  public DefaultIDNode getDefaultRDFIDNode()
  {
    return this.defaultRDFIDNode;
  }

  public DefaultLabelNode getDefaultRDFSLabelNode()
  {
    return this.defaultRDFSLabelNode;
  }

  public ShiftDirectiveNode getShiftDirectiveNode()
  {
    return this.shiftDirectiveNode;
  }

  public IfExistsDirectiveNode getIfExistsDirectiveNode()
  {
    return this.ifExistsDirectiveNode;
  }

  public IfNotExistsDirectiveNode getIfNotExistsDirectiveNode()
  {
    return this.ifNotExistsDirectiveNode;
  }

  public EmptyDataValueDirectiveNode getEmptyDataValueDirectiveNode()
  {
    return this.emptyDataValueDirectiveNode;
  }

  public EmptyLocationDirectiveNode getEmptyLocationDirectiveNode()
  {
    return this.emptyLocationDirectiveNode;
  }

  public EmptyRDFIDDirectiveNode getEmptyRDFIDDirectiveNode()
  {
    return this.emptyRDFIDDirectiveNode;
  }

  public EmptyRDFSLabelDirectiveNode getEmptyRDFSLabelDirectiveNode()
  {
    return this.emptyRDFSLabelDirectiveNode;
  }

  public PrefixNode getPrefixNode()
  {
    return this.prefixNode;
  }

  public LanguageNode getLanguageNode()
  {
    return this.languageNode;
  }

  public NamespaceNode getNamespaceNode()
  {
    return this.namespaceNode;
  }

  public boolean hasExplicitlySpecifiedReferenceType()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedReferenceType();
  }

  public boolean hasExplicitlySpecifiedPrefix()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedPrefix();
  }

  public boolean hasExplicitlySpecifiedNamespace()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedNamespace();
  }

  public boolean hasExplicitlySpecifiedLanguage()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedLanguage();
  }

  public boolean hasExplicitlySpecifiedValueEncodings()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedValueEncodings();
  }

  public boolean hasExplicitlySpecifiedDefaultLocationValue()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultDataValue()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultDataValue();
  }

  public boolean hasExplicitlySpecifiedDefaultRDFID()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultID();
  }

  public boolean hasExplicitlySpecifiedDefaultRDFSLabel()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultLabel();
  }

  public boolean hasExplicitlySpecifiedEmptyDataValueDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyDataValueDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyLocationDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyLocationDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyRDFIDDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyRDFIDDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyRDFSLabelDirective();
  }

  public boolean hasExplicitlySpecifiedShiftDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedShiftDirective();
  }

  public boolean hasExplicitlySpecifiedTypes()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedTypes();
  }

  public boolean hasExplicitlySpecifiedIfExistsDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedIfExistsDirective();
  }

  public boolean hasExplicitlySpecifiedIfNotExistsDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedIfNotExistsDirective();
  }

  public String getActualLanguage()
  {
    return this.referenceDirectives.getActualLanguage();
  }

  public int getActualEmptyLocationDirective()
  {
    return this.referenceDirectives.getActualEmptyLocationDirective();
  }

  public int getActualEmptyDataValueDirective()
  {
    return this.referenceDirectives.getActualEmptyDataValueDirective();
  }

  public int getActualEmptyRDFSLabelDirective()
  {
    return this.referenceDirectives.getActualEmptyRDFSLabelDirective();
  }

  public int getActualShiftDirective()
  {
    return this.referenceDirectives.getActualShiftDirective();
  }

  public String getActualDefaultLocationValue()
  {
    return this.referenceDirectives.getActualDefaultLocationValue();
  }

  public String getActualDefaultDataValue()
  {
    return this.referenceDirectives.getActualDefaultDataValue();
  }

  public String getActualDefaultRDFID()
  {
    return this.referenceDirectives.getActualDefaultRDFID();
  }

  public String getActualDefaultRDFSLabel()
  {
    return this.referenceDirectives.getActualDefaultRDFSLabel();
  }

  public DefaultLocationValueNode getDefaultLocationValueNode()
  {
    return this.defaultLocationValueNode;
  }

  public boolean hasValueExtractionFunction()
  {
    return this.valueExtractionFunctionNode != null;
  }

  public ValueExtractionFunctionNode getValueExtractionFunctionNode()
  {
    return this.valueExtractionFunctionNode;
  }

  public boolean hasShiftedLocation()
  {
    return this.referenceDirectives.getShiftedLocation() != null;
  }

  public void setShiftedLocation(SpreadsheetLocation location)
  {
    this.referenceDirectives.setShiftedLocation(location);
  }

  public SpreadsheetLocation getShiftedLocation()
  {
    return this.referenceDirectives.getShiftedLocation();
  }

  public void setDefaultShiftSetting(int defaultShiftSetting)
  {
    this.referenceDirectives.setDefaultShiftDirective(defaultShiftSetting);
  }

  public boolean hasExplicitOptions()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedOptions();
  }

  public boolean hasRDFSLabelValueEncoding()
  {
    if (hasExplicitlySpecifiedValueEncodings()) {
      return hasExplicitlySpecifiedRDFSLabelValueEncoding();
    } else
      return this.referenceDirectives.isDefaultRDFSLabelValueEncoding();
  }

  public boolean hasRDFIDValueEncoding()
  {
    if (hasExplicitlySpecifiedValueEncodings()) {
      return hasExplicitlySpecifiedRDFIDValueEncoding();
    } else
      return this.referenceDirectives.isDefaultRDFIDValueEncoding();
  }

  public boolean hasLiteralValueEncoding()
  {
    if (getReferenceTypeNode().getReferenceType().isOWLLiteral())
      return true;
    else if (hasExplicitlySpecifiedValueEncodings())
      return hasExplicitlySpecifiedLiteralValueEncoding();
    else
      return this.referenceDirectives.isDefaultDataValueEncoding();
  }

  public boolean hasExplicitlySpecifiedRDFSLabelValueEncoding()
  {
    if (hasExplicitlySpecifiedValueEncodings()) {
      for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
        if (valueEncoding.hasRDFSLabelEncoding())
          return true;
      return false;
    } else
      return false;
  }

  public boolean hasExplicitlySpecifiedRDFIDValueEncoding()
  {
    if (hasExplicitlySpecifiedValueEncodings()) {
      for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
        if (valueEncoding.hasRDFIDEncoding())
          return true;
      return false;
    } else
      return false;
  }

  public boolean hasExplicitlySpecifiedLiteralValueEncoding()
  {
    if (hasExplicitlySpecifiedValueEncodings()) {
      for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
        if (valueEncoding.hasDataValueEncoding())
          return true;
      return false;
    } else
      return false;
  }

  public ValueEncodingNode getRDFIDValueEncodingNode()
  {
    for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
      if (valueEncoding.hasRDFIDEncoding())
        return valueEncoding;

    return null;
  }

  public ValueEncodingNode getRDFSLabelValueEncodingNode()
  {
    for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
      if (valueEncoding.hasRDFSLabelEncoding())
        return valueEncoding;

    return null;
  }

  public ValueEncodingNode getLiteralValueEncodingNode()
  {
    for (ValueEncodingNode valueEncoding : getValueEncodingNodes())
      if (valueEncoding.hasDataValueEncoding())
        return valueEncoding;

    return null;
  }

	@Override public boolean isReferenceNode() { return true; }

	@Override public boolean isOWLClassNode() { return false; }

	@Override public boolean isOWLPropertyNode() { return false; }

	@Override public String toString()
  {
    String representation = "";
    boolean atLeastOneOptionProcessed = false;

    representation += getSourceSpecificationNode();

    if (hasExplicitOptions())
      representation += "(";

    if (hasExplicitlySpecifiedReferenceType()) {
      representation += this.referenceTypeNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasValueExtractionFunction()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      else
        atLeastOneOptionProcessed = true;
      representation += this.valueExtractionFunctionNode;
    }

    if (hasExplicitlySpecifiedValueEncodings()) {
      boolean isFirst = true;
      if (atLeastOneOptionProcessed)
        representation += " ";
      for (ValueEncodingNode valueEncoding : this.valueEncodingsNodes) {
        if (!isFirst)
          representation += " ";
        representation += valueEncoding;
        isFirst = false;
      }
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultLocationValue()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultLocationValueNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultDataValue()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultDataValueNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultRDFID()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultRDFIDNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultRDFSLabel()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultRDFSLabelNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedLanguage()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.languageNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedPrefix()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.prefixNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedNamespace()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.namespaceNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyLocationDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyLocationDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyDataValueDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyDataValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyRDFIDDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyRDFIDDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyRDFSLabelDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedIfExistsDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.ifExistsDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedIfNotExistsDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.ifNotExistsDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedShiftDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.shiftDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedTypes()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += getTypesNode();
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitOptions())
      representation += ")";

    return representation;
  }

  private void checkValueEncodings() throws RendererException
  {
    boolean location = false, dataValue = false, locationWithDuplicates = false, rdfID = false, rdfsLabel = false;

    if (this.valueEncodingsNodes.isEmpty())
      throw new RendererException("empty value encoding in reference");

    for (ValueEncodingNode valueEncoding : this.valueEncodingsNodes) {

      if (valueEncoding.hasDataValueEncoding())
        dataValue = true;

      if (valueEncoding.useLocationEncoding())
        location = true;

      if (valueEncoding.hasLocationWithDuplicatesEncoding())
        locationWithDuplicates = true;

      if (valueEncoding.hasRDFIDEncoding())
        rdfID = true;

      if (valueEncoding.hasRDFSLabelEncoding())
        rdfsLabel = true;
    }

    if (dataValue && (location || locationWithDuplicates))
      throw new RendererException(
        "location encoding in reference " + toString() + " invalid because it also uses data value encoding");

    if ((location || locationWithDuplicates) && (rdfID || rdfsLabel))
      throw new RendererException(
        "location encoding in reference " + toString() + " invalid because it also uses rdf:ID or rdfs:label encoding");

    if (dataValue && (rdfID || rdfsLabel))
      throw new RendererException("data value encoding in reference " + toString()
        + " invalid because it also uses rdf:ID or rdfs:label encoding");
  }

  private void checkInvalidExplicitDirectives() throws ParseException
  {
    if (this.referenceDirectives.hasExplicitlySpecifiedLanguage() && this.referenceTypeNode.getReferenceType().isOWLLiteral())
      throw new ParseException(
        "use of language specification in reference " + toString() + " invalid because it is an OWL data value");

    if (this.referenceDirectives.hasExplicitlySpecifiedPrefix() && this.referenceTypeNode.getReferenceType().isOWLLiteral())
      throw new ParseException("use of prefix in reference " + toString() + " invalid because it is an OWL data value");

    if (this.referenceDirectives.hasExplicitlySpecifiedNamespace() && this.referenceTypeNode.getReferenceType().isOWLLiteral())
      throw new ParseException(
        "use of namespace in reference " + toString() + " invalid because it is an OWL data value");

    if (this.referenceDirectives.hasExplicitlySpecifiedEmptyDataValueDirective() && !this.referenceTypeNode
      .getReferenceType()
      .isOWLLiteral())
      throw new ParseException(
        "use of empty data value setting in reference " + toString() + " invalid because it is not an OWL data value");

    if (this.referenceDirectives.hasExplicitlySpecifiedReferenceType() && this.referenceTypeNode.getReferenceType().isOWLLiteral()
      && hasExplicitlySpecifiedTypes())
      throw new ParseException(
        "entity type " + this.referenceTypeNode.getReferenceType().getTypeName() + " in reference " + toString()
          + " should not have defining types because it is an OWL data value");
  }
}
