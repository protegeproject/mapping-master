package org.mm.core.settings;

public class ReferenceSettings
{
  public ReferenceTypeSetting referenceTypeSetting = ReferenceTypeSetting.OWL_CLASS;
  public ValueEncodingSetting valueEncodingSetting = ValueEncodingSetting.RDFS_LABEL;
  public PropertyTypeSetting propertyTypeSetting = PropertyTypeSetting.OWL_OBJECT_PROPERTY;
  public EmptyLocationSetting emptyLocationSetting = EmptyLocationSetting.PROCESS_IF_EMPTY_LOCATION;
  public EmptyLiteralSetting emptyLiteralSetting = EmptyLiteralSetting.PROCESS_IF_EMPTY_LITERAL;
  public EmptyRDFIDSetting emptyRDFIDSetting = EmptyRDFIDSetting.PROCESS_IF_EMPTY_ID;
  public EmptyRDFSLabelSetting emptyRDFSLabelSetting = EmptyRDFSLabelSetting.PROCESS_IF_EMPTY_LABEL;
  public IfOWLEntityDoesNotExistSetting ifOWLEntityDoesNotExistSetting = IfOWLEntityDoesNotExistSetting.CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST;
  public IfOWLEntityExistsSetting ifOWLEntityExistsSetting = IfOWLEntityExistsSetting.RESOLVE_IF_OWL_ENTITY_EXISTS;
  public ShiftSetting shiftSetting = ShiftSetting.NO_SHIFT;

  public String namespace = "";
  public String language = "";
};