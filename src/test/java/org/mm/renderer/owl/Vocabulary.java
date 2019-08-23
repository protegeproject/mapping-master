package org.mm.renderer.owl;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Datatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
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
   public static final OWLClass CATAMARAN = Class(IRI(ONTOLOGY_ID, "Catamaran"));
   public static final OWLClass VEHICLE = Class(IRI(ONTOLOGY_ID, "Vehicle"));
   public static final OWLClass AUTOMOBILE = Class(IRI(ONTOLOGY_ID, "Automobile"));
   public static final OWLClass AUTO = Class(IRI(ONTOLOGY_ID, "Auto"));
   public static final OWLClass PHYSICIAN = Class(IRI(ONTOLOGY_ID, "Physician"));
   public static final OWLClass CHILD_OF_DOCTOR = Class(IRI(ONTOLOGY_ID, "ChildOfDoctor"));
   public static final OWLClass PERSON = Class(IRI(ONTOLOGY_ID, "Person"));
   public static final OWLClass HUMAN = Class(IRI(ONTOLOGY_ID, "Human"));
   public static final OWLClass A = Class(IRI(ONTOLOGY_ID, "A"));
   public static final OWLClass SHEET1_A1_UUID = Class(IRI(ONTOLOGY_ID, "71b3e3df-1b0e-3f34-869e-f25398971852"));
   public static final OWLClass STUDENT = Class(IRI(ONTOLOGY_ID, "Student"));
   public static final OWLClass CAR_OWNER = Class(IRI(ONTOLOGY_ID, "CarOwner"));
   public static final OWLClass BMW = Class(IRI(ONTOLOGY_ID, "BMW"));
   public static final OWLClass BARBARA_PUFFINS_CAMELCASE = Class(IRI(ONTOLOGY_ID, "Barbara'sPuffinsHoney-RiceCereal-10.5ozBox"));
   public static final OWLClass BARBARA_PUFFINS_SNAKECASE = Class(IRI(ONTOLOGY_ID, "Barbara's_Puffins_Honey-Rice_Cereal_-_10.5oz_box"));
   public static final OWLClass BARBARA_PUFFINS_UUID = Class(IRI(ONTOLOGY_ID, "ba4a8803-d1ed-3115-a491-4e257f2b490a"));
   public static final OWLClass BARBARA_PUFFINS_MD5 = Class(IRI(ONTOLOGY_ID, "290d31fea17d405a10b7a025dde55111"));
   public static final OWLClass CAR_LOWERCASE = Class(IRI(ONTOLOGY_ID, "car"));
   public static final OWLClass CAR_UPPERCASE = Class(IRI(ONTOLOGY_ID, "CAR"));
   public static final OWLClass BMW_CAR = Class(IRI(ONTOLOGY_ID, "BMWCar"));
   public static final OWLClass CAR_BMW = Class(IRI(ONTOLOGY_ID, "CarBMW"));
   public static final OWLClass BAYERISCHE_MOTOREN_WERKE = Class(IRI(ONTOLOGY_ID, "BayerischeMotorenWerke"));
   public static final OWLClass ZYVOX = Class(IRI(ONTOLOGY_ID, "Zyvox"));
   public static final OWLClass DEFAULT_NAME = Class(IRI(ONTOLOGY_ID, "DefaultName"));

   public static final OWLObjectProperty HAS_ENGINE = ObjectProperty(IRI(ONTOLOGY_ID, "hasEngine"));
   public static final OWLObjectProperty HAS_HULL = ObjectProperty(IRI(ONTOLOGY_ID, "hasHull"));
   public static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI(ONTOLOGY_ID, "hasParent"));
   public static final OWLObjectProperty HAS_GENDER = ObjectProperty(IRI(ONTOLOGY_ID, "hasGender"));
   public static final OWLObjectProperty HAS_P1 = ObjectProperty(IRI(ONTOLOGY_ID, "hasP1"));
   public static final OWLObjectProperty HAS_P2 = ObjectProperty(IRI(ONTOLOGY_ID, "hasP2"));

   public static final OWLDataProperty HAS_P3 = DataProperty(IRI(ONTOLOGY_ID, "hasP3"));
   public static final OWLDataProperty HAS_P4 = DataProperty(IRI(ONTOLOGY_ID, "hasP4"));
   public static final OWLDataProperty HAS_SSN = DataProperty(IRI(ONTOLOGY_ID, "hasSSN"));
   public static final OWLDataProperty HAS_ORIGIN = DataProperty(IRI(ONTOLOGY_ID, "hasOrigin"));
   public static final OWLDataProperty HAS_NAME = DataProperty(IRI(ONTOLOGY_ID, "hasName"));
   public static final OWLDataProperty HAS_AGE = DataProperty(IRI(ONTOLOGY_ID, "hasAge"));
   public static final OWLDataProperty HAS_SALARY = DataProperty(IRI(ONTOLOGY_ID, "hasSalary"));
   public static final OWLDataProperty HAS_DOB = DataProperty(IRI(ONTOLOGY_ID, "hasDOB"));
   public static final OWLDataProperty HAS_BEDTIME = DataProperty(IRI(ONTOLOGY_ID, "hasBedTime"));
   public static final OWLDataProperty HAS_VALUE = DataProperty(IRI(ONTOLOGY_ID, "hasValue"));

   public static final OWLAnnotationProperty SKOS_PREFLABEL = AnnotationProperty(IRI(Namespaces.SKOS + "prefLabel"));
   public static final OWLAnnotationProperty FOAF_DEPICTION = AnnotationProperty(IRI(Namespaces.FOAF + "depiction"));
   public static final OWLAnnotationProperty RDFS_LABEL = AnnotationProperty(IRI(Namespaces.RDFS + "label"));
   public static final OWLAnnotationProperty RDFS_COMMENT = AnnotationProperty(IRI(Namespaces.RDFS + "comment"));
   public static final OWLAnnotationProperty RDFS_SEE_ALSO = AnnotationProperty(IRI(Namespaces.RDFS + "seeAlso"));
   public static final OWLAnnotationProperty RDFS_IS_DEFINED_BY = AnnotationProperty(IRI(Namespaces.RDFS + "isDefinedBy"));
   public static final OWLAnnotationProperty OWL_VERSION_INFO = AnnotationProperty(IRI(Namespaces.OWL + "versionInfo"));
   public static final OWLAnnotationProperty COMMENT = AnnotationProperty(IRI(ONTOLOGY_ID, "comment"));

   public static final OWLNamedIndividual P1 = NamedIndividual(IRI(ONTOLOGY_ID, "p1"));
   public static final OWLNamedIndividual DOUBLE_HULL = NamedIndividual(IRI(ONTOLOGY_ID, "double-hull"));
   public static final OWLNamedIndividual MALE = NamedIndividual(IRI(ONTOLOGY_ID, "male"));
   public static final OWLNamedIndividual FEMALE = NamedIndividual(IRI(ONTOLOGY_ID, "female"));
   public static final OWLNamedIndividual OTHER = NamedIndividual(IRI(ONTOLOGY_ID, "other"));
   public static final OWLNamedIndividual FRED = NamedIndividual(IRI(ONTOLOGY_ID, "fred"));
   public static final OWLNamedIndividual FREDDY = NamedIndividual(IRI(ONTOLOGY_ID, "freddy"));
   public static final OWLNamedIndividual ALFRED = NamedIndividual(IRI(ONTOLOGY_ID, "alfred"));
   public static final OWLNamedIndividual BOB = NamedIndividual(IRI(ONTOLOGY_ID, "bob"));
   public static final OWLNamedIndividual BOBBY = NamedIndividual(IRI(ONTOLOGY_ID, "bobby"));

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
