package org.mm.parser.node;

import java.util.ArrayList;
import java.util.List;

import org.mm.core.ReferenceDirectives;
import org.mm.core.ReferenceType;
import org.mm.parser.ASTDefaultID;
import org.mm.parser.ASTDefaultLabel;
import org.mm.parser.ASTDefaultLiteral;
import org.mm.parser.ASTDefaultLocationValue;
import org.mm.parser.ASTEmptyLiteralSetting;
import org.mm.parser.ASTEmptyLocationSetting;
import org.mm.parser.ASTEmptyRDFIDSetting;
import org.mm.parser.ASTEmptyRDFSLabelSetting;
import org.mm.parser.ASTIRIEncoding;
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

public class ReferenceNode implements MMNode, MappingMasterParserConstants
{
   private SourceSpecificationNode sourceSpecificationNode;
   private ReferenceTypeNode referenceTypeNode;
   private PrefixDirectiveNode prefixDirectiveNode;
   private NamespaceDirectiveNode namespaceDirectiveNode;
   private LanguageDirectiveNode languageDirectiveNode;
   private TypesNode typesNode;
   private DefaultLocationValueDirectiveNode defaultLocationValueDirectiveNode;
   private DefaultLiteralDirectiveNode defaultLiteralDirectiveNode;
   private DefaultIDDirectiveNode defaultRDFIDNode;
   private DefaultLabelDirectiveNode defaultRDFSLabelNode;
   private EmptyLocationDirectiveNode emptyLocationDirectiveNode;
   private EmptyLiteralDirectiveNode emptyLiteralDirectiveNode;
   private EmptyRDFIDDirectiveNode emptyRDFIDDirectiveNode;
   private EmptyRDFSLabelDirectiveNode emptyRDFSLabelDirectiveNode;
   private IfOWLEntityExistsDirectiveNode ifExistsDirectiveNode;
   private IfOWLEntityDoesNotExistDirectiveNode ifNotExistsDirectiveNode;
   private ValueExtractionFunctionNode valueExtractionFunctionNode;
   private ShiftDirectiveNode shiftDirectiveNode;
   private final ReferenceDirectives referenceDirectives;
   private final List<ValueEncodingDirectiveNode> valueEncodingsNodes = new ArrayList<>();
   private IRIEncodingDirectiveNode iriEncodingDirectiveNode;

   public ReferenceNode(ASTReference node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "SourceSpecification")) {
            this.sourceSpecificationNode = new SourceSpecificationNode((ASTSourceSpecification) child);
         } else if (ParserUtil.hasName(child, "ReferenceType")) {
            this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType) child);
         } else if (ParserUtil.hasName(child, "Prefix")) {
            if (this.prefixDirectiveNode != null)
               throw new RendererException("only one prefix directive can be specified for a Reference");
            this.prefixDirectiveNode = new PrefixDirectiveNode((ASTPrefix) child);
         } else if (ParserUtil.hasName(child, "Language")) {
            if (this.languageDirectiveNode != null)
               throw new RendererException("only one language directive can be specified for a Reference");
            this.languageDirectiveNode = new LanguageDirectiveNode((ASTLanguage) child);
            this.referenceTypeNode = new ReferenceTypeNode(RDF_PLAINLITERAL);
         } else if (ParserUtil.hasName(child, "Namespace")) {
            if (this.namespaceDirectiveNode != null)
               throw new RendererException("only one namespace directive can be specified for a Reference");
            this.namespaceDirectiveNode = new NamespaceDirectiveNode((ASTNamespace) child);
         } else if (ParserUtil.hasName(child, "ValueEncoding")) {
            this.valueEncodingsNodes.add(new ValueEncodingDirectiveNode((ASTValueEncoding) child));
         } else if (ParserUtil.hasName(child, "IRIEncoding")) {
            this.iriEncodingDirectiveNode = new IRIEncodingDirectiveNode((ASTIRIEncoding) child);
         } else if (ParserUtil.hasName(child, "DefaultLocationValue")) {
            if (this.defaultLocationValueDirectiveNode != null) throw new RendererException(
                  "only one default location value directive can be specified for a Reference");
            this.defaultLocationValueDirectiveNode = new DefaultLocationValueDirectiveNode(
                  (ASTDefaultLocationValue) child);
         } else if (ParserUtil.hasName(child, "DefaultLiteral")) {
            if (this.defaultLiteralDirectiveNode != null)
               throw new RendererException("only one default literal directive can be specified for a Reference");
            this.defaultLiteralDirectiveNode = new DefaultLiteralDirectiveNode((ASTDefaultLiteral) child);
         } else if (ParserUtil.hasName(child, "DefaultID")) {
            if (this.defaultRDFIDNode != null)
               throw new RendererException("only one default ID directive can be specified for a Reference");
            this.defaultRDFIDNode = new DefaultIDDirectiveNode((ASTDefaultID) child);
         } else if (ParserUtil.hasName(child, "DefaultLabel")) {
            if (this.defaultRDFSLabelNode != null)
               throw new RendererException("only one default label directive can be specified for a Reference");
            this.defaultRDFSLabelNode = new DefaultLabelDirectiveNode((ASTDefaultLabel) child);
         } else if (ParserUtil.hasName(child, "EmptyLocationSetting")) {
            if (this.emptyLocationDirectiveNode != null)
               throw new RendererException("only one empty location directive can be specified for a Reference");
            this.emptyLocationDirectiveNode = new EmptyLocationDirectiveNode((ASTEmptyLocationSetting) child);
         } else if (ParserUtil.hasName(child, "IfExistsDirective")) {
            if (this.ifExistsDirectiveNode != null)
               throw new RendererException("only one if exists directive can be specified for a Reference");
            this.ifExistsDirectiveNode = new IfOWLEntityExistsDirectiveNode((ASTIfExistsDirective) child);
         } else if (ParserUtil.hasName(child, "IfNotExistsDirective")) {
            if (this.ifNotExistsDirectiveNode != null)
               throw new RendererException("only one if not exists directive can be specified for a Reference");
            this.ifNotExistsDirectiveNode = new IfOWLEntityDoesNotExistDirectiveNode((ASTIfNotExistsDirective) child);
         } else if (ParserUtil.hasName(child, "EmptyLiteralSetting")) {
            if (this.emptyLiteralDirectiveNode != null)
               throw new RendererException("only one empty literal directive can be specified for a Reference");
            this.emptyLiteralDirectiveNode = new EmptyLiteralDirectiveNode((ASTEmptyLiteralSetting) child);
         } else if (ParserUtil.hasName(child, "EmptyRDFIDSetting")) {
            if (this.emptyRDFIDDirectiveNode != null)
               throw new RendererException("only one empty rdf:ID directive can be specified for a Reference");
            this.emptyRDFIDDirectiveNode = new EmptyRDFIDDirectiveNode((ASTEmptyRDFIDSetting) child);
         } else if (ParserUtil.hasName(child, "EmptyRDFSLabelSetting")) {
            if (this.emptyRDFSLabelDirectiveNode != null)
               throw new RendererException("only one empty rdfs:Label directive can be specified for a Reference");
            this.emptyRDFSLabelDirectiveNode = new EmptyRDFSLabelDirectiveNode((ASTEmptyRDFSLabelSetting) child);
         } else if (ParserUtil.hasName(child, "ShiftSetting")) {
            if (this.shiftDirectiveNode != null)
               throw new RendererException("only one shift setting directive can be specified for a Reference");
            this.shiftDirectiveNode = new ShiftDirectiveNode((ASTShiftSetting) child);
         } else if (ParserUtil.hasName(child, "ValueExtractionFunction")) {
            if (this.valueExtractionFunctionNode != null)
               throw new RendererException("only one value extraction directive can be specified for a Reference");
            this.valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction) child);
         } else if (ParserUtil.hasName(child, "Types")) {
            this.typesNode = new TypesNode((ASTTypes) child);
         } else throw new InternalParseException("invalid child node " + child + " for ReferenceNode");
      }

      this.referenceDirectives = new ReferenceDirectives(node.defaultReferenceDirectives);

      if (this.sourceSpecificationNode == null)
         throw new RendererException("missing source specification in reference " + toString());

      if (this.referenceTypeNode == null) { // No entity type specified by the user - use default type
         this.referenceTypeNode = new ReferenceTypeNode(node.defaultReferenceDirectives.getDefaultReferenceType());
      } else this.referenceDirectives.setExplicitlySpecifiedReferenceType(this.referenceTypeNode.getReferenceType());

      if (this.valueEncodingsNodes.isEmpty()) {
         this.valueEncodingsNodes
               .add(new ValueEncodingDirectiveNode(node.defaultReferenceDirectives.getDefaultValueEncoding()));
      } else {
         this.referenceDirectives.setHasExplicitlySpecifiedValueEncodings();
         for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes()) {
            if (valueEncoding.useLocationEncoding()) this.referenceDirectives.setUsesLocationEncoding();
            if (valueEncoding.hasLocationWithDuplicatesEncoding())
               this.referenceDirectives.setUsesLocationWithDuplicatesEncoding();
         }
      }

      if (this.iriEncodingDirectiveNode != null)
         this.referenceDirectives.setExplicitlySpecifiedIRIEncoding(this.iriEncodingDirectiveNode.getValueEncodingType());

      if (this.defaultLocationValueDirectiveNode != null)
         this.referenceDirectives.setExplicitlySpecifiedDefaultLocationValue(
               this.defaultLocationValueDirectiveNode.getDefaultLocationValue());

      if (this.defaultLiteralDirectiveNode != null) this.referenceDirectives
            .setExplicitlySpecifiedDefaultLiteral(this.defaultLiteralDirectiveNode.getDefaultLiteral());

      if (this.defaultRDFIDNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedDefaultID(this.defaultRDFIDNode.getDefaultRDFID());

      if (this.defaultRDFSLabelNode != null) this.referenceDirectives
            .setHasExplicitlySpecifiedDefaultLabel(this.defaultRDFSLabelNode.getDefaultRDFSLabel());

      if (this.languageDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedLanguage(this.languageDirectiveNode.getLanguage());

      if (this.prefixDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedPrefix(this.prefixDirectiveNode.getPrefix());

      if (this.namespaceDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedNamespace(this.namespaceDirectiveNode.getNamespace());

      if (this.emptyLocationDirectiveNode != null) this.referenceDirectives
            .setHasExplicitlySpecifiedEmptyLocationDirective(this.emptyLocationDirectiveNode.getEmptyLocationSetting());

      if (this.emptyLiteralDirectiveNode != null) this.referenceDirectives
            .setHasExplicitlySpecifiedEmptyLiteralDirective(this.emptyLiteralDirectiveNode.getEmptyLiteralSetting());

      if (this.emptyRDFIDDirectiveNode != null) this.referenceDirectives
            .setHasExplicitlySpecifiedEmptyRDFIDDirective(this.emptyRDFIDDirectiveNode.getEmptyRDFIDSetting());

      if (this.emptyRDFSLabelDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedEmptyRDFSLabelDirective(
               this.emptyRDFSLabelDirectiveNode.getEmptyRDFSLabelSetting());

      if (this.shiftDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedShiftDirective(this.shiftDirectiveNode.getShiftSetting());

      if (this.ifExistsDirectiveNode != null) this.referenceDirectives
            .setHasExplicitlySpecifiedIfExistsDirective(this.ifExistsDirectiveNode.getIfOWLEntityExistsSetting());

      if (this.ifNotExistsDirectiveNode != null)
         this.referenceDirectives.setHasExplicitlySpecifiedIfOWLEntitiDoesNotExistDirective(
               this.ifNotExistsDirectiveNode.getIfOWLEntityDoesNotExistSetting());

      if (this.typesNode != null) this.referenceDirectives.setHasExplicitlySpecifiedTypes();

      checkValueEncodings();
      checkInvalidExplicitDirectives();
   }

   @Override
   public String getNodeName()
   {
      return "Reference";
   }

   public boolean hasLiteralType()
   {
      return getReferenceType().isOWLLiteral();
   }

   public boolean hasEntityType()
   {
      return getReferenceType().isOWLEntity();
   }

   public ReferenceType getReferenceType()
   {
      return getReferenceTypeNode().getReferenceType();
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

   public List<ValueEncodingDirectiveNode> getValueEncodingNodes()
   {
      return this.valueEncodingsNodes;
   }

   public IRIEncodingDirectiveNode getIRIEncodingNode()
   {
      return this.iriEncodingDirectiveNode;
   }

   public TypesNode getTypesNode()
   {
      return this.typesNode;
   }

   public DefaultLiteralDirectiveNode getDefaultLiteralDirectiveNode()
   {
      return this.defaultLiteralDirectiveNode;
   }

   public DefaultIDDirectiveNode getDefaultRDFIDDirectiveNode()
   {
      return this.defaultRDFIDNode;
   }

   public DefaultLabelDirectiveNode getDefaultRDFSLabelDirectiveNode()
   {
      return this.defaultRDFSLabelNode;
   }

   public ShiftDirectiveNode getShiftDirectiveNode()
   {
      return this.shiftDirectiveNode;
   }

   public IfOWLEntityExistsDirectiveNode getIfExistsDirectiveNode()
   {
      return this.ifExistsDirectiveNode;
   }

   public IfOWLEntityDoesNotExistDirectiveNode getIfNotExistsDirectiveNode()
   {
      return this.ifNotExistsDirectiveNode;
   }

   public EmptyLiteralDirectiveNode getEmptyLiteralDirectiveNode()
   {
      return this.emptyLiteralDirectiveNode;
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

   public PrefixDirectiveNode getPrefixDirectiveNode()
   {
      return this.prefixDirectiveNode;
   }

   public LanguageDirectiveNode getLanguageDirectiveNode()
   {
      return this.languageDirectiveNode;
   }

   public NamespaceDirectiveNode getNamespaceDirectiveNode()
   {
      return this.namespaceDirectiveNode;
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

   public boolean hasExplicitlySpecifiedDefaultLiteral()
   {
      return this.referenceDirectives.hasExplicitlySpecifiedDefaultLiteral();
   }

   public boolean hasExplicitlySpecifiedDefaultRDFID()
   {
      return this.referenceDirectives.hasExplicitlySpecifiedDefaultID();
   }

   public boolean hasExplicitlySpecifiedDefaultRDFSLabel()
   {
      return this.referenceDirectives.hasExplicitlySpecifiedDefaultLabel();
   }

   public boolean hasExplicitlySpecifiedEmptyLiteralDirective()
   {
      return this.referenceDirectives.hasExplicitlySpecifiedEmptyLiteralDirective();
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
      return this.referenceDirectives.hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective();
   }

   public String getActualLanguage()
   {
      return this.referenceDirectives.getActualLanguage();
   }

   public int getActualEmptyLocationDirective()
   {
      return this.referenceDirectives.getActualEmptyLocationDirective();
   }

   public int getActualEmptyLiteralDirective()
   {
      return this.referenceDirectives.getActualEmptyLiteralDirective();
   }

   public int getActualEmptyRDFSLabelDirective()
   {
      return this.referenceDirectives.getActualEmptyRDFSLabelDirective();
   }

   public int getActualEmptyRDFIDDirective()
   {
      return this.referenceDirectives.getActualEmptyRDFIDDirective();
   }

   public int getActualShiftDirective()
   {
      return this.referenceDirectives.getActualShiftDirective();
   }

   public String getActualDefaultLocationValue()
   {
      return this.referenceDirectives.getActualDefaultLocationValue();
   }

   public String getActualDefaultLiteral()
   {
      return this.referenceDirectives.getActualDefaultLiteral();
   }

   public String getActualDefaultRDFID()
   {
      return this.referenceDirectives.getActualDefaultRDFID();
   }

   public String getActualDefaultRDFSLabel()
   {
      return this.referenceDirectives.getActualDefaultRDFSLabel();
   }

   public DefaultLocationValueDirectiveNode getDefaultLocationValueDirectiveNode()
   {
      return this.defaultLocationValueDirectiveNode;
   }

   public boolean hasValueExtractionFunctionNode()
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
      } else return this.referenceDirectives.isDefaultRDFSLabelValueEncoding();
   }

   public boolean hasRDFIDValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         return hasExplicitlySpecifiedRDFIDValueEncoding();
      } else return this.referenceDirectives.isDefaultRDFIDValueEncoding();
   }

   public boolean hasLiteralValueEncoding()
   {
      if (getReferenceTypeNode().getReferenceType().isOWLLiteral())
         return true;
      else if (hasExplicitlySpecifiedValueEncodings())
         return hasExplicitlySpecifiedLiteralValueEncoding();
      else return this.referenceDirectives.isDefaultLiteralEncoding();
   }

   public boolean hasLocationValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         return hasExplicitlySpecifiedLocationValueEncoding();
      } else return this.referenceDirectives.isDefaultLocationValueEncoding();
   }

   public boolean hasExplicitlySpecifiedRDFSLabelValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
            if (valueEncoding.hasRDFSLabelEncoding()) return true;
         return false;
      } else return false;
   }

   public boolean hasExplicitlySpecifiedRDFIDValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
            if (valueEncoding.hasRDFIDEncoding()) return true;
         return false;
      } else return false;
   }

   public boolean hasExplicitlySpecifiedLiteralValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
            if (valueEncoding.hasLiteralEncoding()) return true;
         return false;
      } else return false;
   }

   private boolean hasExplicitlySpecifiedLocationValueEncoding()
   {
      if (hasExplicitlySpecifiedValueEncodings()) {
         for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
            if (valueEncoding.useLocationEncoding()) return true;
         return false;
      } else return false;
   }

   public ValueEncodingDirectiveNode getRDFIDValueEncodingNode()
   {
      for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
         if (valueEncoding.hasRDFIDEncoding()) return valueEncoding;

      return null;
   }

   public ValueEncodingDirectiveNode getRDFSLabelValueEncodingNode()
   {
      for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
         if (valueEncoding.hasRDFSLabelEncoding()) return valueEncoding;

      return null;
   }

   public ValueEncodingDirectiveNode getLiteralValueEncodingNode()
   {
      for (ValueEncodingDirectiveNode valueEncoding : getValueEncodingNodes())
         if (valueEncoding.hasLiteralEncoding()) return valueEncoding;

      return null;
   }

   @Override
   public String toString()
   {
      String representation = "";
      boolean atLeastOneOptionProcessed = false;

      representation += getSourceSpecificationNode();

      if (hasExplicitOptions()) representation += "(";

      if (hasExplicitlySpecifiedReferenceType()) {
         representation += this.referenceTypeNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasValueExtractionFunctionNode()) {
         if (atLeastOneOptionProcessed)
            representation += " ";
         else atLeastOneOptionProcessed = true;
         representation += this.valueExtractionFunctionNode;
      }

      if (hasExplicitlySpecifiedValueEncodings()) {
         boolean isFirst = true;
         if (atLeastOneOptionProcessed) representation += " ";
         for (ValueEncodingDirectiveNode valueEncoding : this.valueEncodingsNodes) {
            if (!isFirst) representation += " ";
            representation += valueEncoding;
            isFirst = false;
         }
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedDefaultLocationValue()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.defaultLocationValueDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedDefaultLiteral()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.defaultLiteralDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedDefaultRDFID()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.defaultRDFIDNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedDefaultRDFSLabel()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.defaultRDFSLabelNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedLanguage()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.languageDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedPrefix()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.prefixDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedNamespace()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.namespaceDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedEmptyLocationDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.emptyLocationDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedEmptyLiteralDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.emptyLiteralDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedEmptyRDFIDDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.emptyRDFIDDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.emptyRDFSLabelDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedIfExistsDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.ifExistsDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedIfNotExistsDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.ifNotExistsDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedShiftDirective()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += this.shiftDirectiveNode;
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitlySpecifiedTypes()) {
         if (atLeastOneOptionProcessed) representation += " ";
         representation += getTypesNode();
         atLeastOneOptionProcessed = true;
      }

      if (hasExplicitOptions()) representation += ")";

      return representation;
   }

   private void checkValueEncodings() throws RendererException
   {
      boolean location = false, literal = false, locationWithDuplicates = false, rdfID = false, rdfsLabel = false;

      if (this.valueEncodingsNodes.isEmpty()) throw new RendererException("empty value encoding in reference");

      for (ValueEncodingDirectiveNode valueEncoding : this.valueEncodingsNodes) {

         if (valueEncoding.hasLiteralEncoding()) literal = true;

         if (valueEncoding.useLocationEncoding()) location = true;

         if (valueEncoding.hasLocationWithDuplicatesEncoding()) locationWithDuplicates = true;

         if (valueEncoding.hasRDFIDEncoding()) rdfID = true;

         if (valueEncoding.hasRDFSLabelEncoding()) rdfsLabel = true;
      }

      if (literal && (location || locationWithDuplicates)) throw new RendererException(
            "location encoding in reference " + toString() + " invalid because it also uses literal encoding");

      if ((location || locationWithDuplicates) && (rdfID || rdfsLabel))
         throw new RendererException("location encoding in reference " + toString()
               + " invalid because it also uses rdf:ID or rdfs:label encoding");

      if (literal && (rdfID || rdfsLabel)) throw new RendererException("literal encoding in reference " + toString()
            + " invalid because it also uses rdf:ID or rdfs:label encoding");
   }

   private void checkInvalidExplicitDirectives() throws ParseException
   {
      if (this.referenceDirectives.hasExplicitlySpecifiedLanguage()
            && this.referenceTypeNode.getReferenceType().isTypedLiteral())
         throw new ParseException(
               "use of language specification in reference " + toString() + " invalid because it is a typed literal");

      if (this.referenceDirectives.hasExplicitlySpecifiedPrefix()
            && this.referenceTypeNode.getReferenceType().isOWLLiteral())
         throw new ParseException("use of prefix in reference " + toString() + " invalid because it is an OWL literal");

      if (this.referenceDirectives.hasExplicitlySpecifiedNamespace()
            && this.referenceTypeNode.getReferenceType().isOWLLiteral())
         throw new ParseException(
               "use of namespace in reference " + toString() + " invalid because it is an OWL literal");

      if (this.referenceDirectives.hasExplicitlySpecifiedEmptyLiteralDirective()
            && !this.referenceTypeNode.getReferenceType().isOWLLiteral())
         throw new ParseException(
               "use of empty literal setting in reference " + toString() + " invalid because it is not an OWL literal");

      if (this.referenceDirectives.hasExplicitlySpecifiedReferenceType()
            && this.referenceTypeNode.getReferenceType().isOWLLiteral() && hasExplicitlySpecifiedTypes())
         throw new ParseException("entity type " + this.referenceTypeNode.getReferenceType().getTypeName()
               + " in reference " + toString() + " should not have defining types because it is an OWL literal");
   }
}
