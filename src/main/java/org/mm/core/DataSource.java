package org.mm.core;

import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.renderer.RendererException;
import org.mm.ss.SpreadsheetLocation;

import java.util.List;
import java.util.Optional;

/**
 * Interface describing a data source seen by a Mapping Master renderer.
 * Currently this supports spreadsheets only.
 */
public interface DataSource
{
	String getLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException;

	String getLocationValue(SpreadsheetLocation location) throws RendererException;

	String getLocationValueWithShifting(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException;

	void setCurrentLocation(SpreadsheetLocation location);

	Optional<SpreadsheetLocation> getCurrentLocation();

	boolean hasCurrentLocation();

	SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecification) throws RendererException;

	List<String> getSheetNames();
}
