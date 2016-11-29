package org.mm.app;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Workbook;
import org.mm.transformationrule.TransformationRule;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface ApplicationModel
{
   /**
    * Returns the ontology.
    */
   OWLOntology getOntology();

   /**
    * Returns the workbook
    */
   Workbook getWorkbook();

   /**
    * Returns the transformation rules.
    */
   Collection<TransformationRule> getTransformationRules();
}
