package org.mm.core;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.DefaultReferenceDirectives;
import org.mm.ss.SpreadsheetLocation;

public class ReferenceDirectives implements MappingMasterParserConstants
{
  private final DefaultReferenceDirectives defaultReferenceDirectives;
  private final int explicitlySpecifiedValueEncoding = -1;
  private boolean hasExplicitlySpecifiedEntityType = false;
  private boolean hasExplicitlySpecifiedValueEncodings = false;
  private boolean hasExplicitlySpecifiedDefaultLocationValue = false;
  private boolean hasExplicitlySpecifiedDefaultDataValue = false;
  private boolean hasExplicitlySpecifiedDefaultRDFID = false;
  private boolean hasExplicitlySpecifiedDefaultRDFSLabel = false;
  private boolean hasExplicitlySpecifiedShiftDirective = false;
  private boolean hasExplicitlySpecifiedLanguage = false;
  private boolean hasExplicitlySpecifiedPrefix = false;
  private boolean hasExplicitlySpecifiedNamespace = false;
  private boolean hasExplicitlySpecifiedIfExistsDirective = false;
  private boolean hasExplicitlySpecifiedIfNotExistsDirective = false;
  private boolean hasExplicitlySpecifiedEmptyLocationDirective = false;
  private boolean hasExplicitlySpecifiedEmptyDataValueDirective = false;
  private boolean hasExplicitlySpecifiedEmptyRDFIDDirective = false;
  private boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective = false;
  private ReferenceType explicitlySpecifiedEntityType = null;
  private String explicitlySpecifiedDefaultLocationValue = null;
  private String explicitlySpecifiedDefaultDataValue = null;
  private String explicitlySpecifiedDefaultRDFID = null;
  private String explicitlySpecifiedDefaultRDFSLabel = null;
  private int explicitlySpecifiedShiftDirective = -1;
  private String explicitlySpecifiedLanguage = null;
  private String explicitlySpecifiedPrefix = null;
  private String explicitlySpecifiedNamespace = null;
  private int explicitlySpecifiedIfExistsDirective = -1;
  private int explicitlySpecifiedIfNotExistsDirective = -1;
  private int explicitlySpecifiedEmptyLocationDirective = -1;
  private int explicitlySpecifiedEmptyDataValueDirective = -1;
  private int explicitlySpecifiedEmptyRDFIDDirective = -1;
  private int explicitlySpecifiedEmptyRDFSLabelDirective = -1;

  private boolean usesLocationEncoding = false;
  private boolean usesLocationWithDuplicatesEncoding = false;
  private boolean hasExplicitlySpecifiedTypes = false;
  private SpreadsheetLocation shiftedLocation;

  private boolean hasExplicitlySpecifiedOptions = false;

  public ReferenceDirectives(DefaultReferenceDirectives defaultReferenceDirectives)
  {
    this.defaultReferenceDirectives = defaultReferenceDirectives;
  }

  public boolean hasExplicitlySpecifiedOptions()
  {
    return hasExplicitlySpecifiedOptions;
  }

  public void setUsesLocationEncoding()
  {
    usesLocationEncoding = true;
  }

  public void setUsesLocationWithDuplicatesEncoding()
  {
    usesLocationWithDuplicatesEncoding = true;
  }

  public boolean usesLocationEncoding()
  {
    return usesLocationEncoding;
  }

  public boolean usesLocationWithDuplicatesEncoding()
  {
    return usesLocationWithDuplicatesEncoding;
  }

  public boolean isDefaultDataValueEncoding()
  {
    return defaultReferenceDirectives.getDefaultValueEncoding() == MM_DATA_VALUE;
  }

  public boolean isDefaultLocationValueEncoding()
  {
    return defaultReferenceDirectives.getDefaultValueEncoding() == MM_LOCATION;
  }

  public boolean isDefaultRDFIDValueEncoding()
  {
    return defaultReferenceDirectives.getDefaultValueEncoding() == RDF_ID;
  }

  public boolean isDefaultRDFSLabelValueEncoding()
  {
    return defaultReferenceDirectives.getDefaultValueEncoding() == RDFS_LABEL;
  }

  public int getDefaultShiftDirective()
  {
    return defaultReferenceDirectives.getDefaultShiftDirective();
  }

  public void setDefaultShiftDirective(int shiftDirective)
  {
    defaultReferenceDirectives.setDefaultShiftDirective(shiftDirective);
  }

  public String getDefaultLocationValue()
  {
    return defaultReferenceDirectives.getDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedEntityType()
  {
    return hasExplicitlySpecifiedEntityType;
  }

  public void setExplicitlySpecifiedEntityType(ReferenceType entityType)
  {
    this.explicitlySpecifiedEntityType = entityType;
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEntityType = true;
  }

  public ReferenceType getActualEntityType()
  {
    return hasExplicitlySpecifiedEntityType() ?
      explicitlySpecifiedEntityType :
      defaultReferenceDirectives.getDefaultEntityType();
  }

  public boolean hasExplicitlySpecifiedValueEncodings()
  {
    return hasExplicitlySpecifiedValueEncodings;
  }

  public void setHasExplicitlySpecifiedValueEncodings()
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedValueEncodings = true;
  }

  public int getActualValueEncoding()
  {
    return hasExplicitlySpecifiedValueEncodings() ?
      explicitlySpecifiedValueEncoding :
      defaultReferenceDirectives.getDefaultValueEncoding();
  }

  public boolean hasExplicitlySpecifiedDefaultLocationValue()
  {
    return hasExplicitlySpecifiedDefaultLocationValue;
  }

  public void setExplicitlySpecifiedDefaultLocationValue(String locationValue)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedDefaultLocationValue = true;
    this.explicitlySpecifiedDefaultLocationValue = locationValue;
  }

  public String getActualDefaultLocationValue()
  {
    return hasExplicitlySpecifiedDefaultLocationValue() ?
      explicitlySpecifiedDefaultLocationValue :
      defaultReferenceDirectives.getDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultDataValue()
  {
    return hasExplicitlySpecifiedDefaultDataValue;
  }

  public void setExplicitlySpecifiedDefaultDataValue(String dataValue)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedDefaultDataValue = true;
    this.explicitlySpecifiedDefaultDataValue = dataValue;
  }

  public String getActualDefaultDataValue()
  {
    return hasExplicitlySpecifiedDefaultDataValue() ?
      explicitlySpecifiedDefaultDataValue :
      defaultReferenceDirectives.getDefaultDataValue();
  }

  public boolean hasExplicitlySpecifiedDefaultID()
  {
    return hasExplicitlySpecifiedDefaultRDFID;
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
      explicitlySpecifiedDefaultRDFID :
      defaultReferenceDirectives.getDefaultRDFID();
  }

  public boolean hasExplicitlySpecifiedDefaultLabel()
  {
    return hasExplicitlySpecifiedDefaultRDFSLabel;
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
      explicitlySpecifiedDefaultRDFSLabel :
      defaultReferenceDirectives.getDefaultRDFSLabel();
  }

  public boolean hasExplicitlySpecifiedShiftDirective()
  {
    return hasExplicitlySpecifiedShiftDirective;
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
      explicitlySpecifiedShiftDirective :
      defaultReferenceDirectives.getDefaultShiftDirective();
  }

  public boolean hasExplicitlySpecifiedLanguage()
  {
    return hasExplicitlySpecifiedLanguage;
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
      explicitlySpecifiedLanguage :
      defaultReferenceDirectives.getDefaultLanguage();
  }

  public boolean hasExplicitlySpecifiedPrefix()
  {
    return hasExplicitlySpecifiedPrefix;
  }

  public void setHasExplicitlySpecifiedPrefix(String prefix)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedPrefix = true;
    this.explicitlySpecifiedPrefix = prefix;
  }

  public String getActualPrefix()
  {
    return hasExplicitlySpecifiedPrefix() ? explicitlySpecifiedPrefix : defaultReferenceDirectives.getDefaultPrefix();
  }

  public boolean hasExplicitlySpecifiedNamespace()
  {
    return hasExplicitlySpecifiedNamespace;
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
      explicitlySpecifiedNamespace :
      defaultReferenceDirectives.getDefaultNamespace();
  }

  public boolean hasExplicitlySpecifiedIfExistsDirective()
  {
    return hasExplicitlySpecifiedIfExistsDirective;
  }

  public void setHasExplicitlySpecifiedIfExistsDirective(int ifExistsDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedIfExistsDirective = true;
    this.explicitlySpecifiedIfExistsDirective = ifExistsDirective;
  }

  public int getActualIfExistsDirective()
  {
    return hasExplicitlySpecifiedIfExistsDirective() ?
      explicitlySpecifiedIfExistsDirective :
      defaultReferenceDirectives.getDefaultIfExistsDirective();
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
    return hasExplicitlySpecifiedIfNotExistsDirective;
  }

  public void setHasExplicitlySpecifiedIfNotExistsDirective(int ifNotExistsDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedIfNotExistsDirective = true;
    this.explicitlySpecifiedIfNotExistsDirective = ifNotExistsDirective;
  }

  public int getActualIfNotExistsDirective()
  {
    return hasExplicitlySpecifiedIfNotExistsDirective() ?
      explicitlySpecifiedIfNotExistsDirective :
      defaultReferenceDirectives.getDefaultIfNotExistsDirective();
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
    return hasExplicitlySpecifiedEmptyLocationDirective;
  }

  public void setHasExplicitlySpecifiedEmptyLocationDirective(int emptyLocationDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyLocationDirective = true;
    this.explicitlySpecifiedEmptyLocationDirective = emptyLocationDirective;
  }

  public int getActualEmptyLocationDirective()
  {
    return hasExplicitlySpecifiedEmptyLocationDirective() ?
      explicitlySpecifiedEmptyLocationDirective :
      defaultReferenceDirectives.getDefaultEmptyLocationDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyDataValueDirective()
  {
    return hasExplicitlySpecifiedEmptyDataValueDirective;
  }

  public void setHasExplicitlySpecifiedEmptyDataValueDirective(int emptyDataValueDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyDataValueDirective = true;
    this.explicitlySpecifiedEmptyDataValueDirective = emptyDataValueDirective;
  }

  public int getActualEmptyDataValueDirective()
  {
    return hasExplicitlySpecifiedEmptyDataValueDirective() ?
      explicitlySpecifiedEmptyDataValueDirective :
      defaultReferenceDirectives.getDefaultEmptyDataValueDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyRDFIDDirective()
  {
    return hasExplicitlySpecifiedEmptyRDFIDDirective;
  }

  public void setHasExplicitlySpecifiedEmptyRDFIDDirective(int emptyRDFIDDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyRDFIDDirective = true;
    this.explicitlySpecifiedEmptyRDFIDDirective = emptyRDFIDDirective;
  }

  public int getActualEmptyRDFIDDirective()
  {
    return hasExplicitlySpecifiedEmptyRDFIDDirective() ?
      explicitlySpecifiedEmptyRDFIDDirective :
      defaultReferenceDirectives.getDefaultEmptyRDFIDDirective();
  }

  public boolean actualEmptyRDFIDDirectiveIsSkipIfEmpty()
  {
    return getActualEmptyRDFIDDirective() == MM_SKIP_IF_EMPTY_ID;
  }

  public boolean hasExplicitlySpecifiedEmptyRDFSLabelDirective()
  {
    return hasExplicitlySpecifiedEmptyRDFSLabelDirective;
  }

  public void setHasExplicitlySpecifiedEmptyRDFSLabelDirective(int emptyRDFSLabelDirective)
  {
    hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyRDFSLabelDirective = true;
    this.explicitlySpecifiedEmptyRDFSLabelDirective = emptyRDFSLabelDirective;
  }

  public int getActualEmptyRDFSLabelDirective()
  {
    return hasExplicitlySpecifiedEmptyRDFSLabelDirective() ?
      explicitlySpecifiedEmptyRDFSLabelDirective :
      defaultReferenceDirectives.getDefaultEmptyRDFSLabelDirective();
  }

  public boolean actualEmptyRDFSLabelDirectiveIsSkipIfEmpty()
  {
    return getActualEmptyRDFSLabelDirective() == MM_SKIP_IF_EMPTY_LABEL;
  }

  public boolean hasExplicitlySpecifiedTypes()
  {
    return hasExplicitlySpecifiedTypes;
  }

  public void setHasExplicitlySpecifiedTypes()
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedTypes = true;
  }

  public SpreadsheetLocation getShiftedLocation()
  {
    return shiftedLocation;
  }

  public void setShiftedLocation(SpreadsheetLocation shiftedLocation)
  {
    this.shiftedLocation = shiftedLocation;
  }
}