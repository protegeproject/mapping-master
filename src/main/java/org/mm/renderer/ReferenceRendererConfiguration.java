package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;

/**
 * Contains common functionality for rendering a mapping master reference that
 * may be used by renderer implementations.
 */
public abstract class ReferenceRendererConfiguration implements MappingMasterParserConstants
{
	// Configuration options
	public int defaultValueEncoding = RDFS_LABEL;
	public int defaultReferenceType = OWL_CLASS;
	public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
	public int defaultOWLPropertyValueType = XSD_STRING;
	public int defaultOWLDataPropertyValueType = XSD_STRING;
	public int defaultEmptyLocation = MM_PROCESS_IF_EMPTY_LOCATION;
	public int defaultEmptyRDFID = MM_PROCESS_IF_EMPTY_ID;
	public int defaultEmptyRDFSLabel = MM_PROCESS_IF_EMPTY_LABEL;
	public int defaultIfOWLEntityExists = MM_RESOLVE_IF_OWL_ENTITY_EXISTS;
	public int defaultIfOWLEntityDoesNotExist = MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST;

	public int getDefaultValueEncoding()
	{
		return this.defaultValueEncoding;
	}

	public int getDefaultReferenceType()
	{
		return this.defaultReferenceType;
	}

	public int getDefaultOWLPropertyType()
	{
		return this.defaultOWLPropertyType;
	}

	public int getDefaultOWLPropertyValueType()
	{
		return this.defaultOWLPropertyValueType;
	}

	public int getDefaultOWLDataPropertyValueType()
	{
		return this.defaultOWLDataPropertyValueType;
	}

	public int getDefaultEmptyLocation()
	{
		return this.defaultEmptyLocation;
	}

	public int getDefaultEmptyRDFID()
	{
		return this.defaultEmptyRDFID;
	}

	public int getDefaultEmptyRDFSLabel()
	{
		return this.defaultEmptyRDFSLabel;
	}

	public int getDefaultIfOWLEntityExists()
	{
		return this.defaultIfOWLEntityExists;
	}

	public int getDefaultIfOWLEntityDoesNotExist()
	{
		return this.defaultIfOWLEntityDoesNotExist;
	}

	public void setDefaultValueEncoding(int defaultValueEncoding)
	{
		this.defaultValueEncoding = defaultValueEncoding;
	}

	public void setDefaultReferenceType(int defaultReferenceType)
	{
		this.defaultReferenceType = defaultReferenceType;
	}

	public void setDefaultOWLPropertyType(int defaultOWLPropertyType)
	{
		this.defaultOWLDataPropertyValueType = defaultOWLPropertyType;
	}

	public void setDefaultOWLPropertyValueType(int defaultOWLPropertyValueType)
	{
		this.defaultOWLPropertyValueType = defaultOWLPropertyValueType;
	}

	public void setDefaultOWLDataPropertyValueType(int defaultOWLDataPropertyValueType)
	{
		this.defaultOWLDataPropertyValueType = defaultOWLDataPropertyValueType;
	}

	public void setDefaultEmptyLocation(int defaultEmptyLocation)
	{
		this.defaultEmptyLocation = defaultEmptyLocation;
	}

	public void setDefaultEmptyRDFID(int defaultEmptyRDFID)
	{
		this.defaultEmptyRDFID = defaultEmptyRDFID;
	}

	public void setDefaultEmptyRDFSLabel(int defaultEmptyRDFSLabel)
	{
		this.defaultEmptyRDFSLabel = defaultEmptyRDFSLabel;
	}

	public void setDefaultIfOWLEntityExists(int defaultIfOWLEntityExists)
	{
		this.defaultIfOWLEntityExists = defaultIfOWLEntityExists;
	}

	public void setDefaultIfOWNEntityDoesNotExistDirective(int defaultIfOWLEntityDoesNotExistDirective)
	{
		this.defaultIfOWLEntityDoesNotExist = defaultIfOWLEntityDoesNotExistDirective;
	}
}
