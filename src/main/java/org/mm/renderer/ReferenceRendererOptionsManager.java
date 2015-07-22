package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.renderer.owlapi.OWLAPICoreRenderer;

import java.util.HashSet;
import java.util.Set;

public class ReferenceRendererOptionsManager implements MappingMasterParserConstants
{
  private final ReferenceRenderer referenceRenderer;

  public ReferenceRendererOptionsManager(ReferenceRenderer referenceRenderer)
  {
    this.referenceRenderer = referenceRenderer;
  }

  public String getDefaultNameEncoding()
  {
    return getSettingName(referenceRenderer.getDefaultValueEncoding());
  }

  public String getDefaultReferenceType()
  {
    return getSettingName(referenceRenderer.getDefaultReferenceType());
  }

  public String getDefaultPropertyType()
  {
    return getSettingName(referenceRenderer.getDefaultOWLPropertyType());
  }

  public String getDefaultPropertyValueType()
  {
    return getSettingName(referenceRenderer.getDefaultOWLPropertyAssertionObjectType());
  }

  public String getDefaultDataPropertyValueType()
  {
    return getSettingName(referenceRenderer.getDefaultOWLDataPropertyValueType());
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
      return getSettingName(referenceRenderer.getDefaultValueEncoding());
    else if (optionID == MM_DEFAULT_REFERENCE_TYPE)
      return getSettingName(referenceRenderer.getDefaultReferenceType());
    else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
      return getSettingName(referenceRenderer.getDefaultOWLPropertyType());
    else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
      return getSettingName(referenceRenderer.getDefaultOWLPropertyAssertionObjectType());
    else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
      return getSettingName(referenceRenderer.getDefaultOWLDataPropertyValueType());
    else
      return "unknown option: " + optionName;
  }

  // TODO: test for valid settings for each option: checkOption
  public void setMappingConfigurationOption(String optionName, String settingName)
  {
    int settingID = getSettingID(settingName);
    int optionID = getOptionID(optionName);

    System.err.println(
      "ReferenceRendererOptionsManager.setMappingConfigurationOption: optionName: " + optionName + ", settingName: "
        + settingName);

    if (settingID != -1) {
      if (optionID == MM_DEFAULT_VALUE_ENCODING)
        referenceRenderer.setDefaultValueEncoding(settingID);
      else if (optionID == MM_DEFAULT_REFERENCE_TYPE)
        referenceRenderer.setDefaultReferenceType(settingID);
      else if (optionID == MM_DEFAULT_PROPERTY_TYPE)
        referenceRenderer.setDefaultOWLPropertyType(settingID);
      else if (optionID == MM_DEFAULT_PROPERTY_VALUE_TYPE)
        referenceRenderer.setDefaultOWLPropertyAssertionObjectType(settingID);
      else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
        referenceRenderer.setDefaultOWLDataPropertyValueType(settingID);
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
    Set<String> referenceValueTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPICoreRenderer.ReferenceValueTypes.length; i++) {
      referenceValueTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.ReferenceValueTypes[i]));
    }

    return referenceValueTypes;
  }

  public Set<String> getPropertyTypes()
  {
    Set<String> propertyTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPICoreRenderer.PropertyTypes.length; i++) {
      propertyTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.PropertyTypes[i]));
    }

    return propertyTypes;
  }

  public Set<String> getPropertyValueTypes()
  {
    Set<String> propertyValueTypes = new HashSet<String>();

    for (int i = 0; i < OWLAPICoreRenderer.PropertyValueTypes.length; i++) {
      propertyValueTypes.add(ParserUtil.getTokenName(OWLAPICoreRenderer.PropertyValueTypes[i]));
    }

    return propertyValueTypes;
  }

  public Set<String> getDataPropertyValueTypes()
  {
    Set<String> dataPropertyValueTypes = new HashSet<String>();

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
