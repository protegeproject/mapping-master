package org.mm.renderer;

import java.util.HashSet;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.renderer.owlapi.OWLRenderer;

public class ReferenceRendererOptionsManager implements MappingMasterParserConstants
{
   private final ReferenceRendererConfiguration referenceRenderer;

   public ReferenceRendererOptionsManager(ReferenceRendererConfiguration referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   public String getDefaultValueEncoding()
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
      return getSettingName(this.referenceRenderer.getDefaultOWLPropertyValueType());
   }

   public String getDefaultDataPropertyValueType()
   {
      return getSettingName(this.referenceRenderer.getDefaultOWLDataPropertyValueType());
   }

   public String getDefaultEmptyLocation()
   {
      return getSettingName(this.referenceRenderer.getDefaultEmptyLocation());
   }

   public String getDefaultEmptyRDFSLabel()
   {
      return getSettingName(this.referenceRenderer.getDefaultEmptyRDFSLabel());
   }

   public String getDefaultEmptyRDFID()
   {
      return getSettingName(this.referenceRenderer.getDefaultEmptyRDFID());
   }

   public String getDefaultIfOWLEntityExists()
   {
      return getSettingName(this.referenceRenderer.getDefaultIfOWLEntityExists());
   }

   public String getDefaultIfOWLEntityDoesNotExist()
   {
      return getSettingName(this.referenceRenderer.getDefaultIfOWLEntityDoesNotExist());
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

   public String getDefaultEmptyLocationOptionName()
   {
      return getOptionName(MM_DEFAULT_EMPTY_LOCATION);
   }

   public String getDefaultEmptyRDFSLabelOptionName()
   {
      return getOptionName(MM_DEFAULT_EMPTY_RDFS_LABEL);
   }

   public String getDefaultEmptyRDFIDOptionName()
   {
      return getOptionName(MM_DEFAULT_EMPTY_RDF_ID);
   }

   public String getDefaultIfOWLEntityExistsOptionName()
   {
      return getOptionName(MM_DEFAULT_IF_OWL_ENTITY_EXISTS);
   }

   public String getDefaultIfOWLEntityDoesNotExistOptionName()
   {
      return getOptionName(MM_DEFAULT_IF_OWL_ENTITY_DOES_NOT_EXIST);
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
         return getSettingName(this.referenceRenderer.getDefaultOWLPropertyValueType());
      else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
         return getSettingName(this.referenceRenderer.getDefaultOWLDataPropertyValueType());
      else if (optionID == MM_DEFAULT_EMPTY_LOCATION)
         return getSettingName(this.referenceRenderer.getDefaultEmptyLocation());
      else if (optionID == MM_DEFAULT_EMPTY_RDF_ID)
         return getSettingName(this.referenceRenderer.getDefaultEmptyRDFID());
      else if (optionID == MM_DEFAULT_EMPTY_RDFS_LABEL)
         return getSettingName(this.referenceRenderer.getDefaultEmptyRDFSLabel());
      else if (optionID == MM_DEFAULT_IF_OWL_ENTITY_EXISTS)
         return getSettingName(this.referenceRenderer.getDefaultIfOWLEntityExists());
      else if (optionID == MM_DEFAULT_IF_OWL_ENTITY_DOES_NOT_EXIST)
         return getSettingName(this.referenceRenderer.getDefaultIfOWLEntityDoesNotExist());
      else return "unknown option: " + optionName;
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
            this.referenceRenderer.setDefaultOWLPropertyValueType(settingID);
         else if (optionID == MM_DEFAULT_DATA_PROPERTY_VALUE_TYPE)
            this.referenceRenderer.setDefaultOWLDataPropertyValueType(settingID);
         else if (optionID == MM_DEFAULT_EMPTY_LOCATION)
            this.referenceRenderer.setDefaultEmptyLocation(settingID);
         else if (optionID == MM_DEFAULT_EMPTY_RDF_ID)
            this.referenceRenderer.setDefaultEmptyRDFID(settingID);
         else if (optionID == MM_DEFAULT_EMPTY_RDFS_LABEL)
            this.referenceRenderer.setDefaultEmptyRDFSLabel(settingID);
         else if (optionID == MM_DEFAULT_IF_OWL_ENTITY_EXISTS)
            this.referenceRenderer.setDefaultIfOWLEntityExists(settingID);
         else if (optionID == MM_DEFAULT_IF_OWL_ENTITY_DOES_NOT_EXIST)
            this.referenceRenderer.setDefaultIfOWNEntityDoesNotExistDirective(settingID);
      }
   }

   public Set<String> getNameEncodings()
   {
      Set<String> nameEncodings = new HashSet<>();

      for (int i = 0; i < OWLRenderer.NameEncodings.length; i++) {
         nameEncodings.add(ParserUtil.getTokenName(OWLRenderer.NameEncodings[i]));
      }
      return nameEncodings;
   }

   public Set<String> getReferenceValueTypes()
   {
      Set<String> referenceValueTypes = new HashSet<>();

      for (int i = 0; i < OWLRenderer.ReferenceValueTypes.length; i++) {
         referenceValueTypes.add(ParserUtil.getTokenName(OWLRenderer.ReferenceValueTypes[i]));
      }

      return referenceValueTypes;
   }

   public Set<String> getPropertyTypes()
   {
      Set<String> propertyTypes = new HashSet<>();

      for (int i = 0; i < OWLRenderer.PropertyTypes.length; i++) {
         propertyTypes.add(ParserUtil.getTokenName(OWLRenderer.PropertyTypes[i]));
      }

      return propertyTypes;
   }

   public Set<String> getPropertyValueTypes()
   {
      Set<String> propertyValueTypes = new HashSet<>();

      for (int i = 0; i < OWLRenderer.PropertyValueTypes.length; i++) {
         propertyValueTypes.add(ParserUtil.getTokenName(OWLRenderer.PropertyValueTypes[i]));
      }

      return propertyValueTypes;
   }

   public Set<String> getDataPropertyValueTypes()
   {
      Set<String> dataPropertyValueTypes = new HashSet<>();

      for (int i = 0; i < OWLRenderer.DataPropertyValueTypes.length; i++) {
         dataPropertyValueTypes.add(ParserUtil.getTokenName(OWLRenderer.DataPropertyValueTypes[i]));
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
