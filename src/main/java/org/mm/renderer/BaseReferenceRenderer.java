package org.mm.renderer;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class BaseReferenceRenderer implements ReferenceRenderer, MappingMasterParserConstants
{
  // Configuration options
  public int defaultEmptyLocationDirective = MM_PROCESS_IF_EMPTY_LOCATION;
  public int defaultEmptyRDFIDDirective = MM_PROCESS_IF_EMPTY_ID;
  public int defaultEmptyRDFSLabelDirective = MM_PROCESS_IF_EMPTY_LABEL;
  public int defaultIfExistsDirective = MM_RESOLVE_IF_EXISTS;
  public int defaultIfNotExistsDirective = MM_CREATE_IF_NOT_EXISTS;

  // Configuration options
  public int defaultValueEncoding = RDFS_LABEL;
  public int defaultReferenceType = OWL_CLASS;
  public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
  public int defaultOWLPropertyAssertionObjectType = XSD_STRING;
  public int defaultOWLDataPropertyValueType = XSD_STRING;

  private String defaultNamespace = "";
  private String defaultLanguage = "";

  private final OWLLiteralRenderer literalRenderer;

  private SpreadSheetDataSource dataSource;

  public BaseReferenceRenderer(SpreadSheetDataSource dataSource, OWLLiteralRenderer literalRenderer)
  {
    this.dataSource = dataSource;
    this.literalRenderer = literalRenderer;
  }

  public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public int getDefaultValueEncoding()
  {
    return this.defaultValueEncoding;
  }

  @Override public int getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  @Override public int getDefaultOWLPropertyType()
  {
    return this.defaultOWLPropertyType;
  }

  @Override public int getDefaultOWLPropertyAssertionObjectType()
  {
    return this.defaultOWLPropertyAssertionObjectType;
  }

  @Override public int getDefaultOWLDataPropertyValueType()
  {
    return this.defaultOWLDataPropertyValueType;
  }

  @Override public void setDefaultValueEncoding(int defaultValueEncoding)
  {
    this.defaultValueEncoding = defaultValueEncoding;
  }

  @Override public void setDefaultReferenceType(int defaultReferenceType)
  {
    this.defaultReferenceType = defaultReferenceType;
  }

  @Override public void setDefaultOWLPropertyType(int defaultOWLPropertyType)
  {
    this.defaultOWLDataPropertyValueType = defaultOWLPropertyType;
  }

  @Override public void setDefaultOWLPropertyAssertionObjectType(int defaultOWLPropertyAssertionObjectType)
  {
    this.defaultOWLPropertyAssertionObjectType = defaultOWLPropertyAssertionObjectType;
  }

  @Override public void setDefaultOWLDataPropertyValueType(int defaultOWLDataPropertyValueType)
  {
    this.defaultOWLDataPropertyValueType = defaultOWLDataPropertyValueType;
  }

  private SpreadsheetLocation getLocation(SourceSpecificationNode sourceSpecificationNode) throws RendererException
  {
    if (sourceSpecificationNode.hasLiteral())
      return null;
    else
      return dataSource.resolveLocation(sourceSpecificationNode);
  }

  private String getReferenceRDFIDValue(String locationValue, ReferenceNode referenceNode) throws RendererException
  {
    String rdfIDValue;

    if (referenceNode.hasRDFIDValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
        rdfIDValue = processValueEncoding(locationValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfIDValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
      else
        rdfIDValue = locationValue;
    } else
      rdfIDValue = "";

    if (rdfIDValue.equals("") && !referenceNode.getActualDefaultRDFID().equals(""))
      rdfIDValue = referenceNode.getActualDefaultRDFID();

    if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_ID)
      throw new RendererException("empty RDF ID in reference " + referenceNode);

    if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_ID) {
      //logLine("processReference: WARNING: empty RDF ID in reference");
    }

    return rdfIDValue;
  }

  private String getReferenceRDFSLabelText(String locationValue, ReferenceNode referenceNode) throws RendererException
  {
    String rdfsLabelText;

    if (referenceNode.hasRDFSLabelValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
        rdfsLabelText = processValueEncoding(locationValue, referenceNode.getRDFSLabelValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfsLabelText = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
      else
        rdfsLabelText = locationValue;
    } else
      rdfsLabelText = "";

    if (rdfsLabelText.equals("") && !referenceNode.getActualDefaultRDFSLabel().equals(""))
      rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

    if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
      throw new RendererException("empty RDFS label in reference " + referenceNode);

    if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL) {
      // logLine("processReference: WARNING: empty RDFS label in reference");
    }

    return rdfsLabelText;
  }

  private String processValueEncoding(String value, ValueEncodingNode valueEncodingNode, ReferenceNode referenceNode)
    throws RendererException
  {
    if (valueEncodingNode != null) {
      if (valueEncodingNode.hasValueSpecification())
        return processValueSpecification(value, valueEncodingNode.getValueSpecification(), referenceNode);
      else
        return value;
    } else
      return value;
  }

  private String processValueSpecification(String value, ValueSpecificationNode valueSpecificationNode,
    ReferenceNode referenceNode) throws RendererException
  {
    String processedValue = "";

    for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
      .getValueSpecificationItemNodes()) {
      if (valueSpecificationItemNode.hasStringLiteral())
        processedValue += valueSpecificationItemNode.getStringLiteral();
      else if (valueSpecificationItemNode.hasReferenceNode()) {
        ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
        valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
        Optional<? extends ReferenceRendering> referenceRendering = renderReference(
          valueSpecificationItemReferenceNode);
        if (referenceRendering.isPresent()) {
          if (referenceRendering.get().isOWLLiteral()) {
            processedValue += referenceRendering.get().getRawValue();
          } else
            throw new RendererException(
              "expecting OWL literal for value specification, got " + referenceRendering.get());
        }
      } else if (valueSpecificationItemNode.hasValueExtractionFunctionNode()) {
        ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
          .getValueExtractionFunctionNode();
        processedValue += processValueExtractionFunction(valueExtractionFunction, value);
      } else if (valueSpecificationItemNode.hasCapturingExpression() && value != null) {
        String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
        processedValue += processCapturingExpression(value, capturingExpression);
      }
    }
    return processedValue;
  }

  private Optional<? extends Rendering> renderValueExtractionFunctionArgument(
    ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isOWLLiteralNode())
      return this.literalRenderer.renderOWLLiteral(valueExtractionFunctionArgumentNode.getOWLLiteralNode());
    else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
      ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
      Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLLiteral()) {
          return referenceRendering;
        } else
          throw new RendererException("expecting literal reference for value extraction function argument, got "
            + valueExtractionFunctionArgumentNode);
      } else
        return Optional.empty();
    } else
      throw new RendererException("unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }

  private String processCapturingExpression(String locationValue, String capturingExpression) throws RendererException
  {
    try {
      Pattern p = Pattern.compile(capturingExpression); // Pull the value out of the location
      Matcher m = p.matcher(locationValue);
      boolean matchFound = m.find();
      String result = "";
      if (matchFound) {
        for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
          result += (m.group(groupIndex));
      }
      return result;
    } catch (PatternSyntaxException e) {
      throw new RendererException("invalid capturing expression: " + capturingExpression + ": " + e.getMessage());
    }
  }

  // Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

  private String processValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode, String value)
    throws RendererException
  {
    List<String> arguments = new ArrayList<>();
    String functionName = valueExtractionFunctionNode.getFunctionName();
    boolean hasExplicitArguments = valueExtractionFunctionNode.hasArguments();
    String processedValue;

    if (valueExtractionFunctionNode.hasArguments()) {
      for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        Optional<? extends Rendering> argumentRendering = renderValueExtractionFunctionArgument(argumentNode);
        if (argumentRendering.isPresent()) {
          // TODO arguments.add(literal.getLiteral());
        }
      }
    }

    switch (valueExtractionFunctionNode.getFunctionID()) {
    case MM_TO_UPPER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
        processedValue = arguments.get(0).toUpperCase();
      } else
        processedValue = value.toUpperCase();
      break;
    case MM_TO_LOWER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = arguments.get(0).toLowerCase();
      } else
        processedValue = value.toLowerCase();
      break;
    case MM_TRIM:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = arguments.get(0).trim();
      } else
        processedValue = value.trim();
      break;
    case MM_REVERSE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = reverse(arguments.get(0));
      } else
        processedValue = reverse(value);
      break;
    case MM_CAPTURING:
      if (arguments.size() == 1) {
        processedValue = processCapturingExpression(value, arguments.get(0));
      } else if (arguments.size() == 2) {
        processedValue = processCapturingExpression(arguments.get(0), arguments.get(1));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_PREPEND:
      if (arguments.size() == 1) {
        processedValue = arguments.get(0) + value;
      } else if (arguments.size() == 2) {
        processedValue = arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_APPEND:
      if (arguments.size() == 1) {
        processedValue = value + arguments.get(0);
      } else if (arguments.size() == 2) {
        processedValue = arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE:
      if (arguments.size() == 2) {
        processedValue = value.replace(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE_ALL:
      if (arguments.size() == 2) {
        processedValue = value.replaceAll(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE_FIRST:
      if (arguments.size() == 2) {
        processedValue = value.replaceFirst(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    default:
      throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName() + ")");
    }
    return processedValue;
  }

  private String getReferenceValue(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    String referenceValue;

    if (sourceSpecificationNode.hasLiteral()) {
      // Reference is a literal, e.g., @"Person", @"http://a.com#Person"
      referenceValue = sourceSpecificationNode.getLiteral();
      // log("processReference: literal");
    } else { // Reference to data source location

      // log("--processReference: specified location " + location);
      String rawLocationValue = dataSource.getLocationValue(location, referenceNode); // Deals with shifting

      if ((rawLocationValue == null || rawLocationValue.equals("")))
        referenceValue = referenceNode.getActualDefaultLocationValue();
      else
        referenceValue = rawLocationValue;

      if (referenceValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
        throw new RendererException("empty location " + location + " in reference " + referenceNode);

      if (referenceValue.equals("")
        && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION) {
        // logLine("processReference: WARNING: empty location " + location + " in reference " + referenceNode);
      }
      // log(", location value [" + referenceValue + "], entity type " + referenceNode.getReferenceTypeNode());
    }

    if (!referenceNode.getReferenceTypeNode().getReferenceType().isOWLLiteral()) {
      // log(", namespace " + getReferenceNamespace(referenceNode));
      String language = getReferenceLanguage(referenceNode);
      displayLanguage(language);
      // log(", valueEncoding");
      //for (ValueEncodingNode valueEncodingNode : referenceNode.getValueEncodingNodes())
      //  log(" " + valueEncodingNode);
    }

    if (!sourceSpecificationNode.hasLiteral()) { // Determine if originally specified location has been shifted
      //if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT && referenceNode.hasShiftedLocation())
      // log(", shifted location " + referenceNode.getShiftedLocation());
    }
    //rendering.logLine("");

    return referenceValue;
  }

  private String processLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
    ReferenceNode referenceNode) throws RendererException
  {
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
    String locationValue = rawLocationValue.replace("\"", "\\\"");
    String processedLocationValue;

    if (referenceNode.hasLiteralValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
        processedLocationValue = processValueEncoding(locationValue, referenceNode.getLiteralValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        processedLocationValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(),
          locationValue);
      else
        processedLocationValue = locationValue;
    } else
      processedLocationValue = "";

    if (processedLocationValue.equals("") && !referenceNode.getActualDefaultDataValue().equals(""))
      processedLocationValue = referenceNode.getActualDefaultDataValue();

    if (processedLocationValue.equals("")
      && referenceNode.getActualEmptyDataValueDirective() == MM_ERROR_IF_EMPTY_DATA_VALUE)
      throw new RendererException("empty data value in reference " + referenceNode + " at location " + location);

    if (processedLocationValue.equals("")
      && referenceNode.getActualEmptyDataValueDirective() == MM_WARNING_IF_EMPTY_DATA_VALUE)
      //logLine(
      //  "processReference: WARNING: empty data value in reference " + referenceNode + " at location " + location);

      if (referenceType.isQuotedOWLDataValue())
        processedLocationValue = "\"" + processedLocationValue + "\"";

    return processedLocationValue;
  }

  private String displayLanguage(String language)
  {
    String display = "";

    if (language != null) {
      display += ", xml:lang";
      if (language.equals(""))
        display += "=mm:null";
      else if (!language.equals("*"))
        display += "=*";
      else if (language.equals("+"))
        display += "!=mm:null";
      else
        display += language;
    }
    return display;
  }

  private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
  {
    // A reference will not have both a prefix and a namespace specified
    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      String prefix = referenceNode.getPrefixNode().getPrefix();
      String namespace = getNamespaceForPrefix(prefix);
      if (namespace == null)
        throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
      return namespace;
    } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      return referenceNode.getNamespaceNode().getNamespace();
    } else {
      if (!hasDefaultNamespace())
        throw new RendererException(
          "ontology has no default namespace and no namespace specified by reference " + referenceNode);

      return getDefaultNamespace();
    }
  }

  private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
  {
    if (referenceNode.hasExplicitlySpecifiedLanguage())
      return referenceNode.getActualLanguage();
    else
      return getDefaultLanguage(); // Which might be null or empty
  }

  private String getDefaultNamespace()
  {
    return this.defaultNamespace;
  }

  private String getDefaultLanguage()
  {
    return this.defaultLanguage;
  }

  private boolean hasDefaultNamespace()
  {
    return this.defaultNamespace.length() != 0;
  }

  private String reverse(String source)
  {
    int i, len = source.length();
    StringBuffer dest = new StringBuffer(len);

    for (i = (len - 1); i >= 0; i--)
      dest.append(source.charAt(i));

    return dest.toString();
  }
}
