package org.mm.directive;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DefaultDirectives extends ReferenceDirectives {

   public DefaultDirectives() {
      super("",                              // default prefix
            "",                              // default namespace
            "",                              // default language
            "",                              // default label value
            "",                              // default literal value
            MM_UNTYPED,                      // default entity type
            OWL_DATA_PROPERTY,               // default property type
            XSD_STRING,                      // default value datatype
            RDF_ID,                          // default value encoding
            MM_CAMELCASE_ENCODE,             // default IRI encoding
            MM_NO_SHIFT,                     // default shift direction
            MM_IGNORE_IF_CELL_EMPTY,         // default order if value is empty
            MM_CREATE_IF_ENTITY_ABSENT);     // default order if OWL entity is absent
   }
}
