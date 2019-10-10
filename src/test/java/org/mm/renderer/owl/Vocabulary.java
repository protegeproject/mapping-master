package org.mm.renderer.owl;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Datatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class Vocabulary {

   public static final String ONTOLOGY_ID = "http://protege.stanford.edu/mapping-master/test/";

   public static final OWLClass CAR = Class(IRI(ONTOLOGY_ID, "Car"));
   public static final OWLClass MOTOR = Class(IRI(ONTOLOGY_ID, "Motor"));
   public static final OWLClass GASOLINE = Class(IRI(ONTOLOGY_ID, "Gasoline"));
   public static final OWLClass DIESEL = Class(IRI(ONTOLOGY_ID, "Diesel"));
   public static final OWLClass BATTERY = Class(IRI(ONTOLOGY_ID, "Battery"));
   public static final OWLClass PERSON = Class(IRI(ONTOLOGY_ID, "Person"));
   public static final OWLClass SHEET1_A1_UUID = Class(IRI(ONTOLOGY_ID, "71b3e3df-1b0e-3f34-869e-f25398971852"));
   public static final OWLClass STUDENT = Class(IRI(ONTOLOGY_ID, "Student"));
   public static final OWLClass CAR_OWNER = Class(IRI(ONTOLOGY_ID, "CarOwner"));
   public static final OWLClass BMW = Class(IRI(ONTOLOGY_ID, "BMW"));
   public static final OWLClass BARBARA_PUFFINS_CAMELCASE = Class(IRI(ONTOLOGY_ID, "Barbara'sPuffinsHoney-RiceCereal-10.5ozBox"));
   public static final OWLClass BARBARA_PUFFINS_SNAKECASE = Class(IRI(ONTOLOGY_ID, "Barbara's_Puffins_Honey-Rice_Cereal_-_10.5oz_box"));
   public static final OWLClass BARBARA_PUFFINS_UUID = Class(IRI(ONTOLOGY_ID, "dee34b3b-6646-3e80-b625-7e4a92bcc862"));
   public static final OWLClass BARBARA_PUFFINS_MD5 = Class(IRI(ONTOLOGY_ID, "290d31fea17d405a10b7a025dde55111"));
   public static final OWLClass CAR_LOWERCASE = Class(IRI(ONTOLOGY_ID, "car"));
   public static final OWLClass CAR_UPPERCASE = Class(IRI(ONTOLOGY_ID, "CAR"));

   public static final OWLObjectProperty HAS_ENGINE = ObjectProperty(IRI(ONTOLOGY_ID, "hasEngine"));
   public static final OWLObjectProperty USE_FUEL = ObjectProperty(IRI(ONTOLOGY_ID, "useFuel"));
   public static final OWLObjectProperty USE_ELECTRICITY = ObjectProperty(IRI(ONTOLOGY_ID, "useElectricity"));
   public static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI(ONTOLOGY_ID, "hasParent"));
   public static final OWLObjectProperty HAS_GENDER = ObjectProperty(IRI(ONTOLOGY_ID, "hasGender"));

   public static final OWLDataProperty HAS_NAME = DataProperty(IRI(ONTOLOGY_ID, "hasName"));
   public static final OWLDataProperty HAS_ALIAS = DataProperty(IRI(ONTOLOGY_ID, "hasAlias"));
   public static final OWLDataProperty HAS_AGE = DataProperty(IRI(ONTOLOGY_ID, "hasAge"));
   public static final OWLDataProperty HAS_VALUE = DataProperty(IRI(ONTOLOGY_ID, "hasValue"));
   public static final OWLDataProperty NUMBER_OF_CYLINDER = DataProperty(IRI(ONTOLOGY_ID, "numberOfCylinder"));

   public static final OWLAnnotationProperty FOAF_DEPICTION = AnnotationProperty(IRI(Namespaces.FOAF + "depiction"));
   public static final OWLAnnotationProperty RDFS_LABEL = AnnotationProperty(IRI(Namespaces.RDFS + "label"));
   public static final OWLAnnotationProperty RDFS_COMMENT = AnnotationProperty(IRI(Namespaces.RDFS + "comment"));
   public static final OWLAnnotationProperty RDFS_SEE_ALSO = AnnotationProperty(IRI(Namespaces.RDFS + "seeAlso"));
   public static final OWLAnnotationProperty RDFS_IS_DEFINED_BY = AnnotationProperty(IRI(Namespaces.RDFS + "isDefinedBy"));
   public static final OWLAnnotationProperty OWL_VERSION_INFO = AnnotationProperty(IRI(Namespaces.OWL + "versionInfo"));
   public static final OWLAnnotationProperty COMMENT = AnnotationProperty(IRI(ONTOLOGY_ID, "comment"));

   public static final OWLNamedIndividual P1 = NamedIndividual(IRI(ONTOLOGY_ID, "p1"));
   public static final OWLNamedIndividual MALE = NamedIndividual(IRI(ONTOLOGY_ID, "male"));
   public static final OWLNamedIndividual FEMALE = NamedIndividual(IRI(ONTOLOGY_ID, "female"));
   public static final OWLNamedIndividual OTHER = NamedIndividual(IRI(ONTOLOGY_ID, "other"));
   public static final OWLNamedIndividual FRED = NamedIndividual(IRI(ONTOLOGY_ID, "fred"));
   public static final OWLNamedIndividual BOB = NamedIndividual(IRI(ONTOLOGY_ID, "bob"));
   public static final OWLNamedIndividual BMW_MOTOR = NamedIndividual(IRI(ONTOLOGY_ID, "bmw-motor"));

   public static final OWLLiteral NICK = Literal("Nick");

   public static final OWLDatatype RDFS_LITERAL = Datatype(IRI(Namespaces.RDFS + "Literal"));
   public static final OWLDatatype RDF_PLAINLITERAL = Datatype(IRI(Namespaces.RDF + "PlainLiteral"));
   public static final OWLDatatype XSD_STRING = Datatype(IRI(Namespaces.XSD + "string"));
   public static final OWLDatatype XSD_BOOLEAN = Datatype(IRI(Namespaces.XSD + "boolean"));
   public static final OWLDatatype XSD_DOUBLE = Datatype(IRI(Namespaces.XSD + "double"));
   public static final OWLDatatype XSD_FLOAT = Datatype(IRI(Namespaces.XSD + "float"));
   public static final OWLDatatype XSD_LONG = Datatype(IRI(Namespaces.XSD + "long"));
   public static final OWLDatatype XSD_INTEGER = Datatype(IRI(Namespaces.XSD + "integer"));
   public static final OWLDatatype XSD_SHORT = Datatype(IRI(Namespaces.XSD + "short"));
   public static final OWLDatatype XSD_BYTE = Datatype(IRI(Namespaces.XSD + "byte"));
   public static final OWLDatatype XSD_DECIMAL = Datatype(IRI(Namespaces.XSD + "decimal"));
   public static final OWLDatatype XSD_DATETIME = Datatype(IRI(Namespaces.XSD + "dateTime"));
   public static final OWLDatatype XSD_TIME = Datatype(IRI(Namespaces.XSD + "time"));
   public static final OWLDatatype XSD_DATE = Datatype(IRI(Namespaces.XSD + "date"));
   public static final OWLDatatype XSD_DURATION = Datatype(IRI(Namespaces.XSD + "duration"));
}
