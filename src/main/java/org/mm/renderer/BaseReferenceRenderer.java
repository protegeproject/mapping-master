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
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Contains common functionality for rendering a mapping master reference that may be used by renderer implementations.
 */
public abstract class BaseReferenceRenderer
  implements ReferenceRenderer, OWLLiteralRenderer, MappingMasterParserConstants
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

  private SpreadSheetDataSource dataSource;

  public BaseReferenceRenderer(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public void setDataSource(SpreadSheetDataSource dataSource)
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

  protected SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecificationNode)
    throws RendererException
  {
    return this.dataSource.resolveLocation(sourceSpecificationNode);
  }

  protected String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
  {
    if (referenceNode.hasExplicitlySpecifiedLanguage())
      return referenceNode.getActualLanguage();
    else
      return getDefaultLanguage(); // Which might be null or empty
  }

  protected String getDefaultNamespace()
  {
    return this.defaultNamespace;
  }

  protected String getDefaultLanguage()
  {
    return this.defaultLanguage;
  }

  protected boolean hasDefaultNamespace()
  {
    return !this.defaultNamespace.isEmpty();
  }

  protected String resolveReferenceValue(SpreadsheetLocation location, ReferenceNode referenceNode)
    throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    String referenceValue;

    if (sourceSpecificationNode.hasLiteral()) {
      // Reference is a literal, e.g., @"Person", @"http://a.com#Person"
      referenceValue = sourceSpecificationNode.getLiteral();
    } else { // Reference to data source location
      String rawLocationValue = this.dataSource.getLocationValue(location, referenceNode); // Deals with shifting

      if (rawLocationValue == null || rawLocationValue.isEmpty())
        referenceValue = referenceNode.getActualDefaultLocationValue();
      else
        referenceValue = rawLocationValue;

      if (referenceValue.isEmpty() && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
        throw new RendererException("empty location " + location + " in reference " + referenceNode);

      if (referenceValue.isEmpty() && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION) {
      }
    }
    return referenceValue;
  }

  protected String processLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
    ReferenceNode referenceNode) throws RendererException
  {
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
    String locationValue = rawLocationValue.replace("\"", "\\\"");
    String processedLocationValue;

    if (referenceNode.hasLiteralValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
        processedLocationValue = processValueEncoding(locationValue, referenceNode.getLiteralValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction()) {
        ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
        processedLocationValue = processValueExtractionFunction(locationValue, valueExtractionFunctionNode);
      } else
        processedLocationValue = locationValue;
    } else
      processedLocationValue = "";

    if (processedLocationValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
      processedLocationValue = referenceNode.getActualDefaultLiteral();

    if (processedLocationValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective()
      == MM_ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

    if (processedLocationValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective()
      == MM_WARNING_IF_EMPTY_LITERAL) {
      //logLine(
      //  "processReference: WARNING: empty literal in reference " + referenceNode + " at location " + location);
    }

    return processedLocationValue;
  }

  protected String getReferenceRDFID(String referenceLocationValue, ReferenceNode referenceNode)
    throws RendererException
  {
    String rdfIDValue;

    if (referenceNode.hasRDFIDValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
        rdfIDValue = processValueEncoding(referenceLocationValue, referenceNode.getRDFIDValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfIDValue = processValueExtractionFunction(referenceLocationValue,
          referenceNode.getValueExtractionFunctionNode());
      else
        rdfIDValue = referenceLocationValue;
    } else
      rdfIDValue = "";

    if (rdfIDValue.isEmpty() && !referenceNode.getActualDefaultRDFID().isEmpty())
      rdfIDValue = referenceNode.getActualDefaultRDFID();

    if (rdfIDValue.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_ID)
      throw new RendererException("empty RDF ID in reference " + referenceNode);

    if (rdfIDValue.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_ID) {
      //logLine("processReference: WARNING: empty RDF ID in reference");
    }

    return rdfIDValue;
  }

  protected String getReferenceRDFSLabel(String referenceLocationValue, ReferenceNode referenceNode)
    throws RendererException
  {
    String rdfsLabelText;

    if (referenceNode.hasRDFSLabelValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
        rdfsLabelText = processValueEncoding(referenceLocationValue, referenceNode.getRDFSLabelValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfsLabelText = processValueExtractionFunction(referenceLocationValue,
          referenceNode.getValueExtractionFunctionNode());
      else
        rdfsLabelText = referenceLocationValue;
    } else
      rdfsLabelText = "";

    if (rdfsLabelText.isEmpty() && !referenceNode.getActualDefaultRDFSLabel().isEmpty())
      rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

    if (rdfsLabelText.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
      throw new RendererException("empty RDFS label in reference " + referenceNode);

    if (rdfsLabelText.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL) {
      // logLine("processReference: WARNING: empty RDFS label in reference");
    }
    return rdfsLabelText;
  }

  protected String processValueEncoding(String value, ValueEncodingNode valueEncodingNode, ReferenceNode referenceNode)
    throws RendererException
  {
    if (valueEncodingNode != null) {
      if (valueEncodingNode.hasValueSpecificationNode())
        return processValueSpecification(value, valueEncodingNode.getValueSpecificationNode(), referenceNode);
      else
        return value;
    } else
      return value;
  }

  protected String processValueSpecification(String value, ValueSpecificationNode valueSpecificationNode,
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
        processedValue += processValueExtractionFunction(value, valueExtractionFunction);
      } else if (valueSpecificationItemNode.hasCapturingExpression() && value != null) {
        String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
        processedValue += processCapturingExpression(value, capturingExpression);
      }
    }
    return processedValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

  protected String processValueExtractionFunction(String value, ValueExtractionFunctionNode valueExtractionFunctionNode)
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
          //  TODO arguments.add(literal.getLiteral());
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
      throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName());
    }
    return processedValue;
  }

  private Optional<? extends Rendering> renderValueExtractionFunctionArgument(
    ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isOWLLiteralNode())
      return renderOWLLiteral(valueExtractionFunctionArgumentNode.getOWLLiteralNode());
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
      throw new InternalRendererException("unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
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
          result += m.group(groupIndex);
      }
      return result;
    } catch (PatternSyntaxException e) {
      throw new RendererException("invalid capturing expression " + capturingExpression + ": " + e.getMessage());
    }
  }

  private String reverse(String source)
  {
    int i, len = source.length();
    StringBuilder dest = new StringBuilder(len);

    for (i = len - 1; i >= 0; i--)
      dest.append(source.charAt(i));

    return dest.toString();
  }
}
