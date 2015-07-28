package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.renderer.owlapi.OWLAPICoreRenderer;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO This is not very principles.
 */
public class ReferenceRendererOptionsManager implements MappingMasterParserConstants
{
  private final ReferenceRenderer referenceRenderer;

  public ReferenceRendererOptionsManager(ReferenceRenderer referenceRenderer)
  {
    this.referenceRenderer = referenceRenderer;
  }

  public String getDefaultNameEncoding()
  {
    return getSettingName(this.referenceRenderer.getDefaultValueEncoding());
  }

  public String getDefaultReferenceType()
  {
    return getSettingName(this.referenceRenderer.getDefaultReferenceType());
  }

  public String getDefaultPropertyType()
  {
    return getSettingName(this.referenceRenderer.getDefaultOWLPropertyType());
  }

  public String getDefaultPropertyValueType()
  {
    return getSettingName(this.referenceRenderer.getDefaultOWLPropertyAssertionObjectType());
  }

  public String getDefaultDataPropertyValueType()
  {
    return getSettingName(this.referenceRenderer.getDefaultOWLDataPropertyValueType());
  }

  public String getDefaultValueEncodingOptionName()
  {
    return getOptionName(MM_DEFAULT_VALUE_ENCODING);
  }

  public String getDefaultReferenceTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_REFERENCE_TYPE);
  }

  public String getDefaultPropertyTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_PROPERTY_TYPE);
  }

  public String getDefaultPropertyValueTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_PROPERTY_VALUE_TYPE);
  }

  public String getDefaultDataPropertyValueTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE);
  }

  public String getMappingConfigurationOption(String optionName)
  {
    int optionID = getOptionID(optionName);

    if (optionID == MM_DEFAULT_VALUE_ENCODING)
      return getSettingName(this.referenceRenderer.getDefaultValueEncoding());
    else if (optionID == MM_DEFAULT_REFERENCE_TYPE)
      return getSettingName(this.referenceRenderer.getDefaultReferenceType());
    else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
      return getSettingName(this.referenceRenderer.getDefaultOWLPropertyType());
    else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
      return getSettingName(this.referenceRenderer.getDefaultOWLPropertyAssertionObjectType());
    else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
      return getSettingName(this.referenceRenderer.getDefaultOWLDataPropertyValueType());
    else
      return "unknown option: " + optionName;
  }

  // TODO Test for valid settings for each option
  public void setMappingConfigurationOption(String optionName, String settingName)
  {
    int settingID = getSettingID(settingName);
    int optionID = getOptionID(optionName);

    if (settingID != -1) {
      if (optionID == MM_DEFAULT_VALUE_ENCODING)
        this.referenceRenderer.setDefaultValueEncoding(settingID);
      else if (optionID == MM_DEFAULT_REFERENCE_TYPE)
        this.referenceRenderer.setDefaultReferenceType(settingID);
      else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
        this.referenceRenderer.setDefaultOWLPropertyType(settingID);
      else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
        this.referenceRenderer.setDefaultOWLPropertyAssertionObjectType(settingID);
      else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
        this.referenceRenderer.setDefaultOWLDataPropertyValueType(settingID);
    }
  }

  public Set<String> getNameEncodings()
  {
    Set<String> nameEncodings = new HashSet<>();

    for (int i = 0; i < OWLAPICoreRenderer.NameEncodings.length; i++) {
      nameEncodings.add(ParserUtil.getTokenName(OWLAPICoreRenderer.NameEncodings[i]));
    }
    return nameEncodings;
  }

  public Set<String> getReferenceValueTypes()
  {
    Set<String> referenceValueTypes = new HashSet<>();

    for (int i = 0; i < OWLAPICoreRenderer.ReferenceValueTypes.length; i++) {
      referenceValueTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.ReferenceValueTypes[i]));
    }

    return referenceValueTypes;
  }

  public Set<String> getPropertyTypes()
  {
    Set<String> propertyTypes = new HashSet<>();

    for (int i = 0; i < OWLAPICoreRenderer.PropertyTypes.length; i++) {
      propertyTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.PropertyTypes[i]));
    }

    return propertyTypes;
  }

  public Set<String> getPropertyValueTypes()
  {
    Set<String> propertyValueTypes = new HashSet<>();

    for (int i = 0; i < OWLAPICoreRenderer.PropertyValueTypes.length; i++) {
      propertyValueTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.PropertyValueTypes[i]));
    }

    return propertyValueTypes;
  }

  public Set<String> getDataPropertyValueTypes()
  {
    Set<String> dataPropertyValueTypes = new HashSet<>();

    for (int i = 0; i < OWLAPICoreRenderer.DataPropertyValueTypes.length; i++) {
      dataPropertyValueTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.DataPropertyValueTypes[i]));
    }

    return dataPropertyValueTypes;
  }

  private String getOptionName(int optionID)
  {
    return ParserUtil.getTokenName(optionID);
  }

  private int getSettingID(String settingName)
  {
    return ParserUtil.getTokenID(settingName);
  }

  private int getOptionID(String optionName)
  {
    return ParserUtil.getTokenID(optionName);
  }

  private String getSettingName(int settingID)
  {
    return ParserUtil.getTokenName(settingID);
  }
}
