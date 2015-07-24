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
  private boolean hasExplicitlySpecifiedDefaultDataValue;
  private boolean hasExplicitlySpecifiedDefaultRDFID;
  private boolean hasExplicitlySpecifiedDefaultRDFSLabel;
  private boolean hasExplicitlySpecifiedShiftDirective;
  private boolean hasExplicitlySpecifiedLanguage;
  private boolean hasExplicitlySpecifiedPrefix;
  private boolean hasExplicitlySpecifiedNamespace;
  private boolean hasExplicitlySpecifiedIfExistsDirective;
  private boolean hasExplicitlySpecifiedIfNotExistsDirective;
  private boolean hasExplicitlySpecifiedEmptyLocationDirective;
  private boolean hasExplicitlySpecifiedEmptyDataValueDirective;
  private boolean hasExplicitlySpecifiedEmptyRDFIDDirective;
  private boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective;
  private ReferenceType explicitlySpecifiedReferenceType;
  private String explicitlySpecifiedDefaultLocationValue;
  private String explicitlySpecifiedDefaultDataValue;
  private String explicitlySpecifiedDefaultRDFID;
  private String explicitlySpecifiedDefaultRDFSLabel;
  private int explicitlySpecifiedShiftDirective = -1;
  private String explicitlySpecifiedLanguage;
  private String explicitlySpecifiedPrefix;
  private String explicitlySpecifiedNamespace;
  private int explicitlySpecifiedIfExistsDirective = -1;
  private int explicitlySpecifiedIfNotExistsDirective = -1;
  private int explicitlySpecifiedEmptyLocationDirective = -1;
  private int explicitlySpecifiedEmptyDataValueDirective = -1;
  private int explicitlySpecifiedEmptyRDFIDDirective = -1;
  private int explicitlySpecifiedEmptyRDFSLabelDirective = -1;

  private boolean usesLocationEncoding;
  private boolean usesLocationWithDuplicatesEncoding;
  private boolean hasExplicitlySpecifiedTypes;
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

  public boolean isDefaultDataValueEncoding()
  {
    return this.defaultReferenceDirectives.getDefaultValueEncoding() == MM_DATA_VALUE;
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
    return hasExplicitlySpecifiedReferenceType() ?
      this.explicitlySpecifiedReferenceType :
      this.defaultReferenceDirectives.getDefaultReferenceType();
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
    return hasExplicitlySpecifiedValueEncodings() ?
      this.explicitlySpecifiedValueEncoding :
      this.defaultReferenceDirectives.getDefaultValueEncoding();
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
    return hasExplicitlySpecifiedDefaultLocationValue() ?
      this.explicitlySpecifiedDefaultLocationValue :
      this.defaultReferenceDirectives.getDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultDataValue()
  {
    return this.hasExplicitlySpecifiedDefaultDataValue;
  }

  public void setExplicitlySpecifiedDefaultDataValue(String dataValue)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedDefaultDataValue = true;
    this.explicitlySpecifiedDefaultDataValue = dataValue;
  }

  public String getActualDefaultDataValue()
  {
    return hasExplicitlySpecifiedDefaultDataValue() ?
      this.explicitlySpecifiedDefaultDataValue :
      this.defaultReferenceDirectives.getDefaultDataValue();
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
    return hasExplicitlySpecifiedDefaultID() ?
      this.explicitlySpecifiedDefaultRDFID :
      this.defaultReferenceDirectives.getDefaultRDFID();
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
    return hasExplicitlySpecifiedDefaultLabel() ?
      this.explicitlySpecifiedDefaultRDFSLabel :
      this.defaultReferenceDirectives.getDefaultRDFSLabel();
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
    return hasExplicitlySpecifiedShiftDirective() ?
      this.explicitlySpecifiedShiftDirective :
      this.defaultReferenceDirectives.getDefaultShiftDirective();
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
    return hasExplicitlySpecifiedLanguage() ?
      this.explicitlySpecifiedLanguage :
      this.defaultReferenceDirectives.getDefaultLanguage();
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
    return hasExplicitlySpecifiedPrefix() ?
      this.explicitlySpecifiedPrefix : this.defaultReferenceDirectives.getDefaultPrefix();
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
    return hasExplicitlySpecifiedNamespace() ?
      this.explicitlySpecifiedNamespace :
      this.defaultReferenceDirectives.getDefaultNamespace();
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

  public int getActualIfExistsDirective()
  {
    return hasExplicitlySpecifiedIfExistsDirective() ?
      this.explicitlySpecifiedIfExistsDirective :
      this.defaultReferenceDirectives.getDefaultIfExistsDirective();
  }

  public boolean actualIfExistsDirectiveIsSkip()
  {
    return getActualIfExistsDirective() == MM_SKIP_IF_EXISTS;
  }

  public boolean actualIfExistsDirectiveIsWarning()
  {
    return getActualIfExistsDirective() == MM_WARNING_IF_EXISTS;
  }

  public boolean actualIfExistsDirectiveIsError()
  {
    return getActualIfExistsDirective() == MM_ERROR_IF_EXISTS;
  }

  public boolean hasExplicitlySpecifiedIfNotExistsDirective()
  {
    return this.hasExplicitlySpecifiedIfNotExistsDirective;
  }

  public void setHasExplicitlySpecifiedIfNotExistsDirective(int ifNotExistsDirective)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedIfNotExistsDirective = true;
    this.explicitlySpecifiedIfNotExistsDirective = ifNotExistsDirective;
  }

  public int getActualIfNotExistsDirective()
  {
    return hasExplicitlySpecifiedIfNotExistsDirective() ?
      this.explicitlySpecifiedIfNotExistsDirective :
      this.defaultReferenceDirectives.getDefaultIfNotExistsDirective();
  }

  public boolean actualIfNotExistsDirectiveIsSkip()
  {
    return getActualIfNotExistsDirective() == MM_SKIP_IF_NOT_EXISTS;
  }

  public boolean actualIfNotExistsDirectiveIsWarning()
  {
    return getActualIfNotExistsDirective() == MM_WARNING_IF_NOT_EXISTS;
  }

  public boolean actualIfNotExistsDirectiveIsError()
  {
    return getActualIfNotExistsDirective() == MM_ERROR_IF_NOT_EXISTS;
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
    return hasExplicitlySpecifiedEmptyLocationDirective() ?
      this.explicitlySpecifiedEmptyLocationDirective :
      this.defaultReferenceDirectives.getDefaultEmptyLocationDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyDataValueDirective()
  {
    return this.hasExplicitlySpecifiedEmptyDataValueDirective;
  }

  public void setHasExplicitlySpecifiedEmptyDataValueDirective(int emptyDataValueDirective)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyDataValueDirective = true;
    this.explicitlySpecifiedEmptyDataValueDirective = emptyDataValueDirective;
  }

  public int getActualEmptyDataValueDirective()
  {
    return hasExplicitlySpecifiedEmptyDataValueDirective() ?
      this.explicitlySpecifiedEmptyDataValueDirective :
      this.defaultReferenceDirectives.getDefaultEmptyDataValueDirective();
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
    return hasExplicitlySpecifiedEmptyRDFIDDirective() ?
      this.explicitlySpecifiedEmptyRDFIDDirective :
      this.defaultReferenceDirectives.getDefaultEmptyRDFIDDirective();
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
    return hasExplicitlySpecifiedEmptyRDFSLabelDirective() ?
      this.explicitlySpecifiedEmptyRDFSLabelDirective :
      this.defaultReferenceDirectives.getDefaultEmptyRDFSLabelDirective();
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