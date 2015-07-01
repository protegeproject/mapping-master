package org.protege.owl.mm;

import org.protege.owl.mm.parser.node.ReferenceNode;
import org.protege.owl.mm.parser.node.SourceSpecificationNode;
import org.protege.owl.mm.renderer.RendererException;
import org.protege.owl.mm.ss.SpreadsheetLocation;

import java.util.List;

/**
 * Interface describing a data source seen by a Mapping Master renderer. Currently this supports spreadsheets only.
 */
public interface DataSource
{
  String getLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException;

  String getLocationValue(SpreadsheetLocation location) throws RendererException;

  String getLocationValueWithShifting(SpreadsheetLocation location, ReferenceNode referenceNode)
    throws RendererException;

  void setCurrentLocation(SpreadsheetLocation location);

  SpreadsheetLocation getCurrentLocation();

  boolean hasCurrentLocation();

  SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecification) throws RendererException;

  List<String> getSubSourceNames();
}
