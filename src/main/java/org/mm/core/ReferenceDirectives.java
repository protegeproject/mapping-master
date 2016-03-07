package org.mm.core;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.DefaultReferenceDirectives;
import org.mm.ss.SpreadsheetLocation;

public class ReferenceDirectives implements MappingMasterParserConstants
{
   private final DefaultReferenceDirectives defaultReferenceDirectives;
   private final int explicitlySpecifiedValueEncoding = -1;
   private boolean hasExplicitlySpecifiedReferenceType;
   private boolean hasExplicitlySpecifiedValueEncodings;
   private boolean hasExplicitlySpecifiedDefaultLocationValue;
   private boolean hasExplicitlySpecifiedDefaultLiteral;
   private boolean hasExplicitlySpecifiedDefaultRDFID;
   private boolean hasExplicitlySpecifiedDefaultRDFSLabel;
   private boolean hasExplicitlySpecifiedShiftDirective;
   private boolean hasExplicitlySpecifiedLanguage;
   private boolean hasExplicitlySpecifiedPrefix;
   private boolean hasExplicitlySpecifiedNamespace;
   private boolean hasExplicitlySpecifiedIfExistsDirective;
   private boolean hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective;
   private boolean hasExplicitlySpecifiedEmptyLocationDirective;
   private boolean hasExplicitlySpecifiedEmptyLiteralDirective;
   private boolean hasExplicitlySpecifiedEmptyRDFIDDirective;
   private boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective;
   private ReferenceType explicitlySpecifiedReferenceType;
   private String explicitlySpecifiedDefaultLocationValue;
   private String explicitlySpecifiedDefaultLiteral;
   private String explicitlySpecifiedDefaultRDFID;
   private String explicitlySpecifiedDefaultRDFSLabel;
   private int explicitlySpecifiedShiftDirective = -1;
   private String explicitlySpecifiedLanguage;
   private String explicitlySpecifiedPrefix;
   private String explicitlySpecifiedNamespace;
   private int explicitlySpecifiedIfExistsDirective = -1;
   private int explicitlySpecifiedIfOWLEntityDoesNotExistDirective = -1;
   private int explicitlySpecifiedEmptyLocationDirective = -1;
   private int explicitlySpecifiedEmptyLiteralDirective = -1;
   private int explicitlySpecifiedEmptyRDFIDDirective = -1;
   private int explicitlySpecifiedEmptyRDFSLabelDirective = -1;
   private int explicitlySpecifiedIRIEncoding = -1;

   private boolean usesLocationEncoding;
   private boolean usesLocationWithDuplicatesEncoding;
   private boolean hasExplicitlySpecifiedTypes;
   private boolean hasExplicitlySpecifiedIRIEncoding;

   private SpreadsheetLocation shiftedLocation;

   private boolean hasExplicitlySpecifiedOptions;

   public ReferenceDirectives(DefaultReferenceDirectives defaultReferenceDirectives)
   {
      this.defaultReferenceDirectives = defaultReferenceDirectives;
   }

   public boolean hasExplicitlySpecifiedOptions()
   {
      return this.hasExplicitlySpecifiedOptions;
   }

   public void setUsesLocationEncoding()
   {
      this.usesLocationEncoding = true;
   }

   public void setUsesLocationWithDuplicatesEncoding()
   {
      this.usesLocationWithDuplicatesEncoding = true;
   }

   public boolean usesLocationEncoding()
   {
      return this.usesLocationEncoding;
   }

   public boolean usesLocationWithDuplicatesEncoding()
   {
      return this.usesLocationWithDuplicatesEncoding;
   }

   public boolean isDefaultLiteralEncoding()
   {
      return this.defaultReferenceDirectives.getDefaultValueEncoding() == MM_LITERAL;
   }

   public boolean isDefaultLocationValueEncoding()
   {
      return this.defaultReferenceDirectives.getDefaultValueEncoding() == MM_LOCATION;
   }

   public boolean isDefaultRDFIDValueEncoding()
   {
      return this.defaultReferenceDirectives.getDefaultValueEncoding() == RDF_ID;
   }

   public boolean isDefaultRDFSLabelValueEncoding()
   {
      return this.defaultReferenceDirectives.getDefaultValueEncoding() == RDFS_LABEL;
   }

   public int getDefaultShiftDirective()
   {
      return this.defaultReferenceDirectives.getDefaultShiftDirective();
   }

   public void setDefaultShiftDirective(int shiftDirective)
   {
      this.defaultReferenceDirectives.setDefaultShiftDirective(shiftDirective);
   }

   public String getDefaultLocationValue()
   {
      return this.defaultReferenceDirectives.getDefaultLocationValue();
   }

   public boolean hasExplicitlySpecifiedReferenceType()
   {
      return this.hasExplicitlySpecifiedReferenceType;
   }

   public void setExplicitlySpecifiedReferenceType(ReferenceType referenceType)
   {
      this.explicitlySpecifiedReferenceType = referenceType;
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedReferenceType = true;
   }

   public ReferenceType getActualReferenceType()
   {
      return hasExplicitlySpecifiedReferenceType() ? this.explicitlySpecifiedReferenceType
            : this.defaultReferenceDirectives.getDefaultReferenceType();
   }

   public boolean hasExplicitlySpecifiedIRIEncoding()
   {
      return this.hasExplicitlySpecifiedIRIEncoding;
   }

   public void setExplicitlySpecifiedIRIEncoding(int iriEncodingType)
   {
      this.explicitlySpecifiedIRIEncoding = iriEncodingType;
      this.hasExplicitlySpecifiedIRIEncoding = true;
   }

   public int getActualIRIEncoding()
   {
      return hasExplicitlySpecifiedIRIEncoding() ? this.explicitlySpecifiedIRIEncoding
            : this.defaultReferenceDirectives.getDefaultIRIEncoding();
   }

   public boolean hasExplicitlySpecifiedValueEncodings()
   {
      return this.hasExplicitlySpecifiedValueEncodings;
   }

   public void setHasExplicitlySpecifiedValueEncodings()
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedValueEncodings = true;
   }

   public int getActualValueEncoding()
   {
      return hasExplicitlySpecifiedValueEncodings() ? this.explicitlySpecifiedValueEncoding
            : this.defaultReferenceDirectives.getDefaultValueEncoding();
   }

   public boolean hasExplicitlySpecifiedDefaultLocationValue()
   {
      return this.hasExplicitlySpecifiedDefaultLocationValue;
   }

   public void setExplicitlySpecifiedDefaultLocationValue(String locationValue)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedDefaultLocationValue = true;
      this.explicitlySpecifiedDefaultLocationValue = locationValue;
   }

   public String getActualDefaultLocationValue()
   {
      return hasExplicitlySpecifiedDefaultLocationValue() ? this.explicitlySpecifiedDefaultLocationValue
            : this.defaultReferenceDirectives.getDefaultLocationValue();
   }

   public boolean hasExplicitlySpecifiedDefaultLiteral()
   {
      return this.hasExplicitlySpecifiedDefaultLiteral;
   }

   public void setExplicitlySpecifiedDefaultLiteral(String literal)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedDefaultLiteral = true;
      this.explicitlySpecifiedDefaultLiteral = literal;
   }

   public String getActualDefaultLiteral()
   {
      return hasExplicitlySpecifiedDefaultLiteral() ? this.explicitlySpecifiedDefaultLiteral
            : this.defaultReferenceDirectives.getDefaultLiteral();
   }

   public boolean hasExplicitlySpecifiedDefaultID()
   {
      return this.hasExplicitlySpecifiedDefaultRDFID;
   }

   public void setHasExplicitlySpecifiedDefaultID(String id)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedDefaultRDFID = true;
      this.explicitlySpecifiedDefaultRDFID = id;
   }

   public String getActualDefaultRDFID()
   {
      return hasExplicitlySpecifiedDefaultID() ? this.explicitlySpecifiedDefaultRDFID
            : this.defaultReferenceDirectives.getDefaultRDFID();
   }

   public boolean hasExplicitlySpecifiedDefaultLabel()
   {
      return this.hasExplicitlySpecifiedDefaultRDFSLabel;
   }

   public void setHasExplicitlySpecifiedDefaultLabel(String label)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedDefaultRDFSLabel = true;
      this.explicitlySpecifiedDefaultRDFSLabel = label;
   }

   public String getActualDefaultRDFSLabel()
   {
      return hasExplicitlySpecifiedDefaultLabel() ? this.explicitlySpecifiedDefaultRDFSLabel
            : this.defaultReferenceDirectives.getDefaultRDFSLabel();
   }

   public boolean hasExplicitlySpecifiedShiftDirective()
   {
      return this.hasExplicitlySpecifiedShiftDirective;
   }

   public void setHasExplicitlySpecifiedShiftDirective(int shiftDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedShiftDirective = true;
      this.explicitlySpecifiedShiftDirective = shiftDirective;
   }

   public int getActualShiftDirective()
   {
      return hasExplicitlySpecifiedShiftDirective() ? this.explicitlySpecifiedShiftDirective
            : this.defaultReferenceDirectives.getDefaultShiftDirective();
   }

   public boolean hasExplicitlySpecifiedLanguage()
   {
      return this.hasExplicitlySpecifiedLanguage;
   }

   public void setHasExplicitlySpecifiedLanguage(String language)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedLanguage = true;
      this.explicitlySpecifiedLanguage = language;
   }

   public String getActualLanguage()
   {
      return hasExplicitlySpecifiedLanguage() ? this.explicitlySpecifiedLanguage
            : this.defaultReferenceDirectives.getDefaultLanguage();
   }

   public boolean hasExplicitlySpecifiedPrefix()
   {
      return this.hasExplicitlySpecifiedPrefix;
   }

   public void setHasExplicitlySpecifiedPrefix(String prefix)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedPrefix = true;
      this.explicitlySpecifiedPrefix = prefix;
   }

   public String getActualPrefix()
   {
      return hasExplicitlySpecifiedPrefix() ? this.explicitlySpecifiedPrefix
            : this.defaultReferenceDirectives.getDefaultPrefix();
   }

   public boolean hasExplicitlySpecifiedNamespace()
   {
      return this.hasExplicitlySpecifiedNamespace;
   }

   public void setHasExplicitlySpecifiedNamespace(String namespace)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedNamespace = true;
      this.explicitlySpecifiedNamespace = namespace;
   }

   public String getActualNamespace()
   {
      return hasExplicitlySpecifiedNamespace() ? this.explicitlySpecifiedNamespace
            : this.defaultReferenceDirectives.getDefaultNamespace();
   }

   public boolean hasExplicitlySpecifiedIfExistsDirective()
   {
      return this.hasExplicitlySpecifiedIfExistsDirective;
   }

   public void setHasExplicitlySpecifiedIfExistsDirective(int ifExistsDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedIfExistsDirective = true;
      this.explicitlySpecifiedIfExistsDirective = ifExistsDirective;
   }

   public int getActualIfOWLEntityExistsDirective()
   {
      return hasExplicitlySpecifiedIfExistsDirective() ? this.explicitlySpecifiedIfExistsDirective
            : this.defaultReferenceDirectives.getDefaultIfExistsDirective();
   }

   public boolean actualIfOWLEntityExistsDirectiveIsSkip()
   {
      return getActualIfOWLEntityExistsDirective() == MM_SKIP_IF_OWL_ENTITY_EXISTS;
   }

   public boolean actualIfExistsDirectiveIsWarning()
   {
      return getActualIfOWLEntityExistsDirective() == MM_WARNING_IF_OWL_ENTITY_EXISTS;
   }

   public boolean actualIfExistsDirectiveIsError()
   {
      return getActualIfOWLEntityExistsDirective() == MM_ERROR_IF_OWL_ENTITY_EXISTS;
   }

   public boolean hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective()
   {
      return this.hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective;
   }

   public void setHasExplicitlySpecifiedIfOWLEntitiDoesNotExistDirective(int ifOWLEntityDoesNotExistDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective = true;
      this.explicitlySpecifiedIfOWLEntityDoesNotExistDirective = ifOWLEntityDoesNotExistDirective;
   }

   public int getActualIfOWLEntityDoesNotExistDirective()
   {
      return hasExplicitlySpecifiedIfOWLEntityDoesNotExistDirective()
            ? this.explicitlySpecifiedIfOWLEntityDoesNotExistDirective
            : this.defaultReferenceDirectives.getDefaultIfNotExistsDirective();
   }

   public boolean actualIfOWLEntityDoesNotExistDirectiveIsSkip()
   {
      return getActualIfOWLEntityDoesNotExistDirective() == MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean actualIfOWLEntityDoesNotExistDirectiveIsWarning()
   {
      return getActualIfOWLEntityDoesNotExistDirective() == MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean actualIfOWLEntityDoesNotExistDirectiveIsError()
   {
      return getActualIfOWLEntityDoesNotExistDirective() == MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST;
   }

   public boolean hasExplicitlySpecifiedEmptyLocationDirective()
   {
      return this.hasExplicitlySpecifiedEmptyLocationDirective;
   }

   public void setHasExplicitlySpecifiedEmptyLocationDirective(int emptyLocationDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedEmptyLocationDirective = true;
      this.explicitlySpecifiedEmptyLocationDirective = emptyLocationDirective;
   }

   public int getActualEmptyLocationDirective()
   {
      return hasExplicitlySpecifiedEmptyLocationDirective() ? this.explicitlySpecifiedEmptyLocationDirective
            : this.defaultReferenceDirectives.getDefaultEmptyLocationDirective();
   }

   public boolean hasExplicitlySpecifiedEmptyLiteralDirective()
   {
      return this.hasExplicitlySpecifiedEmptyLiteralDirective;
   }

   public void setHasExplicitlySpecifiedEmptyLiteralDirective(int emptyLiteralDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedEmptyLiteralDirective = true;
      this.explicitlySpecifiedEmptyLiteralDirective = emptyLiteralDirective;
   }

   public int getActualEmptyLiteralDirective()
   {
      return hasExplicitlySpecifiedEmptyLiteralDirective() ? this.explicitlySpecifiedEmptyLiteralDirective
            : this.defaultReferenceDirectives.getDefaultEmptyLiteralDirective();
   }

   public boolean hasExplicitlySpecifiedEmptyRDFIDDirective()
   {
      return this.hasExplicitlySpecifiedEmptyRDFIDDirective;
   }

   public void setHasExplicitlySpecifiedEmptyRDFIDDirective(int emptyRDFIDDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedEmptyRDFIDDirective = true;
      this.explicitlySpecifiedEmptyRDFIDDirective = emptyRDFIDDirective;
   }

   public int getActualEmptyRDFIDDirective()
   {
      return hasExplicitlySpecifiedEmptyRDFIDDirective() ? this.explicitlySpecifiedEmptyRDFIDDirective
            : this.defaultReferenceDirectives.getDefaultEmptyRDFIDDirective();
   }

   public boolean actualEmptyRDFIDDirectiveIsSkipIfEmpty()
   {
      return getActualEmptyRDFIDDirective() == MM_SKIP_IF_EMPTY_ID;
   }

   public boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective()
   {
      return this.hasExplicitlySpecifiedEmptyRDFSLabelDirective;
   }

   public void setHasExplicitlySpecifiedEmptyRDFSLabelDirective(int emptyRDFSLabelDirective)
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedEmptyRDFSLabelDirective = true;
      this.explicitlySpecifiedEmptyRDFSLabelDirective = emptyRDFSLabelDirective;
   }

   public int getActualEmptyRDFSLabelDirective()
   {
      return hasExplicitlySpecifiedEmptyRDFSLabelDirective() ? this.explicitlySpecifiedEmptyRDFSLabelDirective
            : this.defaultReferenceDirectives.getDefaultEmptyRDFSLabelDirective();
   }

   public boolean actualEmptyRDFSLabelDirectiveIsSkipIfEmpty()
   {
      return getActualEmptyRDFSLabelDirective() == MM_SKIP_IF_EMPTY_LABEL;
   }

   public boolean hasExplicitlySpecifiedTypes()
   {
      return this.hasExplicitlySpecifiedTypes;
   }

   public void setHasExplicitlySpecifiedTypes()
   {
      this.hasExplicitlySpecifiedOptions = true;
      this.hasExplicitlySpecifiedTypes = true;
   }

   public SpreadsheetLocation getShiftedLocation()
   {
      return this.shiftedLocation;
   }

   public void setShiftedLocation(SpreadsheetLocation shiftedLocation)
   {
      this.shiftedLocation = shiftedLocation;
   }
}