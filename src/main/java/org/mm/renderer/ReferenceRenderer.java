package org.mm.renderer;

import org.mm.parser.node.ReferenceNode;
import org.mm.rendering.ReferenceRendering;
import org.mm.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface ReferenceRenderer
{
  Optional<? extends ReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException;

  void setDataSource(SpreadSheetDataSource dataSource);

  int getDefaultValueEncoding();

  int getDefaultReferenceType();

  int getDefaultOWLPropertyType();

  int getDefaultOWLPropertyValueType();

  int getDefaultOWLDataPropertyValueType();

  int getDefaultEmptyLocationDirective();

  int getDefaultEmptyRDFIDDirective();

  int getDefaultEmptyRDFSLabelDirective();

  int getDefaultIfOWLEntityExistsDirective();

  int getDefaultIfOWLEntityDoesNotExistDirective();

  void setDefaultValueEncoding(int defaultValueEncoding);

  void setDefaultReferenceType(int defaultReferenceType);

  void setDefaultOWLPropertyType(int defaultOWLPropertyType);

  void setDefaultOWLPropertyValueType(int defaultOWLPropertyAssertionObjectType);

  void setDefaultOWLDataPropertyValueType(int defaultOWLDataPropertyValueType);

  void setDefaultEmptyLocationDirective(int defaultEmptyLocationDirective);

  void setDefaultEmptyRDFIDDirective(int defaultEmptyRDFIDDirective);

  void setDefaultEmptyRDFSLabelDirective(int defaultEmptyRDFSLabelDirective);

  void setDefaultIfOWLEntityExistsDirective(int defaultIfOWLEntityExistsDirective);

  void setDefaultIfOWNEntityDoesNotExistDirective(int defaultIfOWLEntityDoesNotExistDirective);
}
