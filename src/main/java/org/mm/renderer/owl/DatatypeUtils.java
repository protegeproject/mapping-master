package org.mm.renderer.owl;

import org.mm.parser.MappingMasterParserConstants;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DatatypeUtils implements MappingMasterParserConstants {

   public static OWLDatatype getOWLDatatype(int datatype) {
      IRI datatypeIri = null;
      if (datatype == XSD_STRING) { datatypeIri = XSDVocabulary.STRING.getIRI(); }
      else if (datatype == XSD_DECIMAL) { datatypeIri = XSDVocabulary.DECIMAL.getIRI(); }
      else if (datatype == XSD_BYTE) { datatypeIri = XSDVocabulary.BYTE.getIRI(); }
      else if (datatype == XSD_SHORT) { datatypeIri = XSDVocabulary.SHORT.getIRI(); }
      else if (datatype == XSD_INTEGER) { datatypeIri = XSDVocabulary.INTEGER.getIRI(); }
      else if (datatype == XSD_LONG) { datatypeIri = XSDVocabulary.LONG.getIRI(); }
      else if (datatype == XSD_FLOAT) { datatypeIri = XSDVocabulary.FLOAT.getIRI(); }
      else if (datatype == XSD_DOUBLE) { datatypeIri = XSDVocabulary.DOUBLE.getIRI(); }
      else if (datatype == XSD_BOOLEAN) { datatypeIri = XSDVocabulary.BOOLEAN.getIRI(); }
      else if (datatype == XSD_DATETIME) { datatypeIri = XSDVocabulary.DATE_TIME.getIRI(); }
      else if (datatype == XSD_TIME) { datatypeIri = XSDVocabulary.TIME.getIRI(); }
      else if (datatype == XSD_DURATION) { datatypeIri = XSDVocabulary.DURATION.getIRI(); }
      else if (datatype == XSD_DATE) { datatypeIri = XSDVocabulary.DATE.getIRI(); }
      else if (datatype == RDF_XMLLITERAL) { datatypeIri = OWLRDFVocabulary.RDF_XML_LITERAL.getIRI(); }
      else if (datatype == RDF_PLAINLITERAL) { datatypeIri = OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI(); }
      if (datatypeIri != null) {
         return new OWLDatatypeImpl(datatypeIri);
      }
      throw new RuntimeException("Internal programming error: datatype code (" + datatype + ") does not exist");
   }
}
