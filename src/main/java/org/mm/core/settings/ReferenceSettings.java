package org.mm.core.settings;

public class ReferenceSettings
{
   private String prefix = "";
   private String namespace = "";
   private String language = "";

   private ReferenceTypeSetting referenceTypeSetting = ReferenceTypeSetting.OWL_CLASS;
   private PropertyTypeSetting propertyTypeSetting = PropertyTypeSetting.OWL_DATA_PROPERTY;
   private ValueEncodingSetting valueEncodingSetting = ValueEncodingSetting.RDF_ID;
   private IRIEncodingSetting iriEncodingSetting = IRIEncodingSetting.MM_CAMELCASE_ENCODE;
   private EmptyLocationSetting emptyLocationSetting = EmptyLocationSetting.WARNING_IF_EMPTY_LOCATION;
   private EmptyLiteralSetting emptyLiteralSetting = EmptyLiteralSetting.WARNING_IF_EMPTY_LITERAL;
   private EmptyRDFIDSetting emptyRDFIDSetting = EmptyRDFIDSetting.WARNING_IF_EMPTY_ID;
   private EmptyRDFSLabelSetting emptyRDFSLabelSetting = EmptyRDFSLabelSetting.WARNING_IF_EMPTY_LABEL;
   private IfOWLEntityDoesNotExistSetting ifOWLEntityDoesNotExistSetting = IfOWLEntityDoesNotExistSetting.CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST;
   private IfOWLEntityExistsSetting ifOWLEntityExistsSetting = IfOWLEntityExistsSetting.RESOLVE_IF_OWL_ENTITY_EXISTS;
   private ShiftSetting shiftSetting = ShiftSetting.NO_SHIFT;

   public String getPrefix()
   {
      return prefix;
   }

   public void setPrefix(String value)
   {
      this.prefix = value;
   }

   public String getNamespace()
   {
      return namespace;
   }

   public void setNamespace(String value)
   {
      this.namespace = value;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String value)
   {
      this.language = value;
   }

   public ReferenceTypeSetting getReferenceTypeSetting()
   {
      return referenceTypeSetting;
   }

   public void setReferenceTypeSetting(ReferenceTypeSetting value)
   {
      referenceTypeSetting = value;
   }

   public ValueEncodingSetting getValueEncodingSetting()
   {
      return valueEncodingSetting;
   }

   public void setValueEncodingSetting(ValueEncodingSetting value)
   {
      valueEncodingSetting = value;
   }

   public IRIEncodingSetting getIRIEncodingSetting()
   {
      return iriEncodingSetting;
   }

   public void setIRIEncodingSetting(IRIEncodingSetting value)
   {
      iriEncodingSetting = value;
   }

   public PropertyTypeSetting getPropertyTypeSetting()
   {
      return propertyTypeSetting;
   }

   public void setPropertyTypeSetting(PropertyTypeSetting value)
   {
      propertyTypeSetting = value;
   }

   public EmptyLocationSetting getEmptyLocationSetting()
   {
      return emptyLocationSetting;
   }

   public void setEmptyLocationSetting(EmptyLocationSetting value)
   {
      this.emptyLocationSetting = value;
   }

   public EmptyLiteralSetting getEmptyLiteralSetting()
   {
      return emptyLiteralSetting;
   }

   public void setEmptyLiteralSetting(EmptyLiteralSetting value)
   {
      this.emptyLiteralSetting = value;
   }

   public EmptyRDFIDSetting getEmptyRDFIDSetting()
   {
      return emptyRDFIDSetting;
   }

   public void setEmptyRDFIDSetting(EmptyRDFIDSetting value)
   {
      this.emptyRDFIDSetting = value;
   }

   public EmptyRDFSLabelSetting getEmptyRDFSLabelSetting()
   {
      return emptyRDFSLabelSetting;
   }

   public void setEmptyRDFSLabelSetting(EmptyRDFSLabelSetting valye)
   {
      this.emptyRDFSLabelSetting = valye;
   }

   public IfOWLEntityDoesNotExistSetting getIfOWLEntityDoesNotExistSetting()
   {
      return ifOWLEntityDoesNotExistSetting;
   }

   public void setIfOWLEntityDoesNotExistSetting(IfOWLEntityDoesNotExistSetting value)
   {
      this.ifOWLEntityDoesNotExistSetting = value;
   }

   public IfOWLEntityExistsSetting getIfOWLEntityExistsSetting()
   {
      return ifOWLEntityExistsSetting;
   }

   public void setIfOWLEntityExistsSetting(IfOWLEntityExistsSetting value)
   {
      this.ifOWLEntityExistsSetting = value;
   }

   public ShiftSetting getShiftSetting()
   {
      return shiftSetting;
   }

   public void setShiftSetting(ShiftSetting value)
   {
      this.shiftSetting = value;
   }
}