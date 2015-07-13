package org.mm.renderer.owlapi;

import org.mm.core.ReferenceType;
import org.mm.core.ReferenceDirectives;
import org.mm.renderer.RendererException;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.HashMap;
import java.util.Map;

class OWLAPIObjectHandler
{
  private final Map<String, Map<String, OWLEntity>> createdOWLEntitiesUsingLabel; // Map of namespace to map of rdfs:label to rdf:ID

  private final Map<String, Map<SpreadsheetLocation, OWLEntity>> createdOWLEntitiesUsingLocation; // Map of namespace to map of location to rdf:ID

	private final Map<String, OWLClassExpression> classExpressionMap;

  private final OWLOntology ontology;

	private int classExpressionIndex;

  public OWLAPIObjectHandler(OWLOntology ontology)
  {
    this.ontology = ontology;

    this.createdOWLEntitiesUsingLabel = new HashMap<>();
		this.createdOWLEntitiesUsingLocation = new HashMap<>();
		this.classExpressionMap = new HashMap<>();
		this.classExpressionIndex = 0;
  }

  public void reset()
  {
    this.createdOWLEntitiesUsingLabel.clear();
    this.createdOWLEntitiesUsingLocation.clear();
		this.classExpressionMap.clear();
		this.classExpressionIndex = 0;
  }

	public String registerOWLClassExpression(OWLClassExpression classExpression)
	{
		String classExpressionID = "CE" + classExpressionIndex++;
		this.classExpressionMap.put(classExpressionID, classExpression);
		return classExpressionID;
	}

  public OWLClassExpression getOWLClassExpression(String classExpressionID)
  {
    return null; // TODO
  }

  public OWLClass getOWLClass(String shortName)
  {
    return null; // TODO
  }

  public OWLClass getOWLClass(IRI iri)
  {
    return null; // TODO
  }

  public OWLNamedIndividual getOWLNamedIndividual(String shortName)
  {
    return null; // TODO
  }

  public OWLNamedIndividual getOWLNamedIndividual(IRI iri)
  {
    return null; // TODO
  }

  public OWLObjectProperty getOWLObjectProperty(String shortName)
  {
    return null; // TODO
  }

  public OWLObjectProperty getOWLObjectProperty(IRI iri)
  {
    return null; // TODO
  }

  public OWLDataProperty getOWLDataProperty(String shortName)
  {
    return null; // TODO
  }

  public OWLDataProperty getOWLDataProperty(IRI iri)
  {
    return null; // TODO
  }

  public OWLAnnotationProperty getOWLAnnotationProperty(String shortName)
  {
    return null; // TODO
  }

  public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri)
  {
    return null; // TODO
  }

  public OWLDatatype getOWLDatatype(String shortName)
  {
    return null; // TODO
  }

  public boolean isOWLClass(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLClass(IRI iri)
  {
    return false; // TODO
  }

  public boolean isOWLObjectProperty(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLObjectProperty(OWLProperty property)
  {
    return false; // TODO
  }

  public boolean isOWLObjectProperty(IRI iri)
  {
    return false; // TODO
  }

  public boolean isOWLNamedIndividual(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLNamedIndividual(IRI iri)
  {
    return false; // TODO
  }

  public boolean isOWLDataProperty(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLDataProperty(OWLProperty property)
  {
    return false; // TODO
  }

  public boolean isOWLDataProperty(IRI iri)
  {
    return false; // TODO
  }

  public boolean isOWLAnnotationProperty(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLAnnotationProperty(IRI iri)
  {
    return false; // TODO
  }

  public boolean isOWLDatatype(String shortName)
  {
    return false; // TODO
  }

  public boolean isOWLDatatype(IRI iri)
  {
    return false; // TODO
  }

  public OWLEntity createOrResolveOWLEntity(SpreadsheetLocation location, String locationValue,
    ReferenceType referenceType, String rdfID, String labelText, String defaultNamespace, String language,
    ReferenceDirectives referenceDirectives) throws RendererException
  {
    OWLEntity createdOrResolvedOWLEntity;

    if (referenceDirectives.usesLocationWithDuplicatesEncoding()) {
      createdOrResolvedOWLEntity = resolveOWLEntityWithDuplicatesEncoding(referenceType, defaultNamespace);
    } else if (referenceDirectives.usesLocationEncoding()) {
      createdOrResolvedOWLEntity = resolveOWLEntityWithLocationEncoding(location, referenceType, defaultNamespace);
    } else { // Uses rdf:ID or rdfs:label encoding
      boolean isEmptyRDFIDValue = rdfID == null || rdfID.equals("");
      boolean isEmptyRDFSLabelText = labelText == null | labelText.equals("");

      if (isEmptyRDFIDValue && referenceDirectives.actualEmptyRDFIDDirectiveIsSkipIfEmpty()) {
        System.err.println("--processReference: skipping because of empty rdf:ID");
        createdOrResolvedOWLEntity = null;
      } else if (isEmptyRDFSLabelText && referenceDirectives.actualEmptyRDFSLabelDirectiveIsSkipIfEmpty()) {
        createdOrResolvedOWLEntity = null;
        System.err.println("--processReference: skipping because of empty rdfs:label");
      } else {
        if (isEmptyRDFIDValue && isEmptyRDFSLabelText) { // Empty rdf:ID and rdfs:label
          createdOrResolvedOWLEntity = createOrResolveOWLEntityWithEmptyIDAndEmptyLabel(location, referenceType,
            defaultNamespace);
        } else { // One or both of rdf:ID and label have values
          if (isEmptyRDFIDValue) { // rdf:ID is empty, label must then have a value. Use label to resolve possible existing entity.
            createdOrResolvedOWLEntity = createOrResolveOWLEntityWithEmptyIDAndNonEmptyLabel(location, referenceType,
              defaultNamespace, labelText, language, referenceDirectives);
          } else { // Has an rdf:ID value, may or may not have a label value
            if (isEmptyRDFSLabelText) { // Has a value for rdf:ID and an empty rdfs:label value
              createdOrResolvedOWLEntity = createOrResolveOWLEntityWithNonEmptyIDAndEmptyLabel(location, rdfID,
                referenceType, defaultNamespace, referenceDirectives);
            } else { // Has rdf:ID and rdfs:label values. Use rdf:ID to resolve resolve possible existing entity.
              createdOrResolvedOWLEntity = createOrResolveOWLEntityWithNonEmptyIDAndNonEmptyLabel(location, rdfID,
                labelText, referenceType, defaultNamespace, language, referenceDirectives);
            }
          }
        }
      }
    }

    if (createdOrResolvedOWLEntity == null)
      System.err.println("--processReference: no " + referenceType + " created");
    else
      System.err.println("--processReference: resolved " + referenceType + " " + createdOrResolvedOWLEntity);

    return createdOrResolvedOWLEntity;
  }

  private OWLEntity createOrResolveOWLEntityWithNonEmptyIDAndNonEmptyLabel(SpreadsheetLocation location, String rdfID,
    String labelText, ReferenceType referenceType, String namespace, String language,
    ReferenceDirectives referenceDirectives) throws RendererException
  {
    OWLEntity resolvedOWLEntity;

    if (shouldCreateOrResolveOWLEntityWithRDFID(namespace, rdfID, referenceDirectives)
      && shouldCreateOrResolveOWLEntityWithRDFSLabel(labelText, language, referenceDirectives)) {
      if (hasOWLEntityBeenCreatedAtLocation(location,
        namespace)) { // Has an entity been created at this location already?
        resolvedOWLEntity = getCreatedEntityRDFIDAtLocation(referenceType, location, namespace);
        System.err.println(
          "--processReference: using existing " + referenceType + " " + resolvedOWLEntity + " created at this location");
      } else { // No existing entity created at this location -- create one. If an existing entity has this rdf:ID it will be reused.
        resolvedOWLEntity = createOWLEntity(location, rdfID, referenceType, namespace,
          referenceDirectives); // If entity exists, it will be retrieved.
        System.err.println("--processReference: creating/resolving " + referenceType + " using rdf:ID " + rdfID);
        recordCreatedOWLEntityRDFIDAtLocation(referenceType, location, namespace, resolvedOWLEntity);
      }
      // TODO: look at this because it allows a back door way to add a possibly duplicate label
      addRDFSLabelToOWLEntity(referenceType, resolvedOWLEntity, namespace, labelText, language);
    } else
      resolvedOWLEntity = null;

    return resolvedOWLEntity;
  }

  private OWLEntity createOrResolveOWLEntityWithNonEmptyIDAndEmptyLabel(SpreadsheetLocation location, String rdfID,
    ReferenceType referenceType, String namespace, ReferenceDirectives referenceDirectives) throws RendererException
  {
    OWLEntity resolvedOWLEntity;

    if (shouldCreateOrResolveOWLEntityWithRDFID(namespace, rdfID, referenceDirectives)) {
      if (hasOWLEntityBeenCreatedAtLocation(location,
        namespace)) { // Has an entity for this location been created already?
        resolvedOWLEntity = getCreatedEntityRDFIDAtLocation(referenceType, location, namespace);
        System.err.println(
          "--processReference: using existing " + referenceType + " " + resolvedOWLEntity + " created at this location");
      } else { // No existing entity created at this location -- create one. If an existing entity has this rdf:ID it will be reused.
        resolvedOWLEntity = createOWLEntity(location, rdfID, referenceType, namespace,
          referenceDirectives); // If entity exists, it will be
        // retrieved
        System.err.println("--processReference: creating/resolving " + referenceType + " using rdf:ID " + rdfID);
        recordCreatedOWLEntityRDFIDAtLocation(referenceType, location, namespace, resolvedOWLEntity);
      }
    } else
      resolvedOWLEntity = null;

    return resolvedOWLEntity;
  }

  private OWLEntity createOrResolveOWLEntityWithEmptyIDAndNonEmptyLabel(SpreadsheetLocation locationNode,
    ReferenceType referenceType, String defaultNamespace, String labelText, String language,
    ReferenceDirectives referenceDirectives) throws RendererException
  {
    OWLEntity resolvedOWLEntity;

    if (shouldCreateOrResolveOWLEntityWithRDFSLabel(labelText, language, referenceDirectives)) {

      if (hasOWLEntityBeenCreatedWithLabel(defaultNamespace, labelText,
        language)) { // Have already created an entity using this label
        resolvedOWLEntity = getCreatedOWLEntityWithRDFSLabel(referenceType, defaultNamespace, labelText,
          language); // Find the existing
        // one
        System.err.print(
          "--processReference: using existing " + referenceType + " " + resolvedOWLEntity + " with rdfs:label "
            + labelText);
        if (language == null || language.equals(""))
          System.err.println();
        else
          System.err.println(", xml:lang \"" + language + "\"");
      } else if (isExistingOWLEntityWithRDFSLabelAndLanguage(labelText, language)) {
        resolvedOWLEntity = getOWLEntityWithRDFSLabel(labelText, language);
        System.err.print(
          "--processReference: using existing ontology " + referenceType + " " + resolvedOWLEntity + " with rdfs:label "
            + labelText);
        displayLanguage(language);
        System.err.println();
      } else { // No existing entity with this label - create one
        // Has an entity for this location in the specified namespace been created already?
        if (hasOWLEntityBeenCreatedAtLocation(locationNode, defaultNamespace)) {
          resolvedOWLEntity = getCreatedEntityRDFIDAtLocation(referenceType, locationNode, defaultNamespace);
          System.err.println("--processReference: using existing " + referenceType + " " + resolvedOWLEntity
            + " created at this location with namespace " + defaultNamespace);
        } else { // No existing entity created at this location - create one with an auto-generated rdf:ID
          resolvedOWLEntity = createOWLEntity(referenceType, defaultNamespace);
          // TODO: call createOWLEntityUsingRDFSLabel when it is fixed to create in proper namespace
          System.err.println("--processReference: creating " + referenceType);
          recordCreatedOWLEntityRDFIDAtLocation(referenceType, locationNode, defaultNamespace, resolvedOWLEntity);
        }
        addRDFSLabelToOWLEntity(referenceType, resolvedOWLEntity, defaultNamespace, labelText, language);
      }
    } else
      resolvedOWLEntity = null;

    return resolvedOWLEntity;
  }

  private void displayLanguage(String language)
  {
    String display = "";

    if (language != null) {
      display += ", xml:lang";
      if (language.equals(""))
        display += "=mm:null";
      else if (!language.equals("*"))
        display += "=*";
      else if (language.equals("+"))
        display += "!=mm:null";
    }
    System.err.print(display);
  }

  private OWLEntity getOWLEntityWithRDFSLabel(String labelText, String language) throws RendererException
  {
    if (language != null && language.equals("*"))
      return getOWLEntityWithRDFSLabel(labelText); // Match on any language or none
    else if (language != null && language.equals("+"))
      return getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(labelText); // Match on at least one language
    else
      return getOWLEntityWithRDFSLabelAndLanguage(labelText, language); // Match on specific language
  }

  private boolean shouldCreateOrResolveOWLEntityWithRDFSLabel(String labelText, String language,
    ReferenceDirectives referenceDirectives) throws RendererException
  {
    if (isExistingOWLEntityWithRDFSLabelAndLanguage(labelText, language)) {
      if (referenceDirectives.actualIfExistsDirectiveIsError())
        throwExistingOWLEntityWithLabelException(labelText, language);
      else if (referenceDirectives.actualIfExistsDirectiveIsWarning())
        warnExistingOWLEntityWithRDFSLabel(labelText, language);
      else if (referenceDirectives.actualIfExistsDirectiveIsSkip()) {
        System.err.println("--processReference: skipping because OWL entity with this label already exists");
        return false;
      }
      // If setting is MM_RESOLVE_IF_EXISTS we resolve it.
    } else { // No existing entity
      if (referenceDirectives.actualIfNotExistsDirectiveIsError())
        throwNoExistingOWLEntityWithRDFSLabelException(labelText, language);
      else if (referenceDirectives.actualIfNotExistsDirectiveIsWarning())
        warnNoExistingOWLEntityWithRDFSLabel(labelText, language);
      else if (referenceDirectives.actualIfNotExistsDirectiveIsSkip()) {
        System.err.println("--processReference: skipping because OWL entity with this label does not exist");
        return false;
      }
      // If setting is MM_CREATE_IF_NOT_EXISTS we create it.
    }
    return true;
  }

  // TODO: code is currently very sloppy about fully qualified rdf:IDs vs. fragments.
  private boolean shouldCreateOrResolveOWLEntityWithRDFID(String namespace, String rdfID,
    ReferenceDirectives referenceDirectives) throws RendererException
  {
    if (isExistingOWLEntityWithRDFID(rdfID)) {
      if (referenceDirectives.actualIfExistsDirectiveIsError())
        throwExistingOWLEntityWithRDFIDException(namespace, rdfID);
      else if (referenceDirectives.actualIfExistsDirectiveIsWarning())
        warnExistingOWLEntityWithRDFID(namespace, rdfID);
      else if (referenceDirectives.actualIfExistsDirectiveIsSkip()) {
        return false;
      }
      // If setting is MM_RESOLVE_IF_EXISTS we resolve it.
    } else { // No existing entity
      if (referenceDirectives.actualIfNotExistsDirectiveIsError())
        throwNoExistingOWLEntityWithRDFIDException(namespace, rdfID);
      else if (referenceDirectives.actualIfNotExistsDirectiveIsWarning())
        warnNoExistingOWLEntityWithRDFID(namespace, rdfID);
      else if (referenceDirectives.actualIfNotExistsDirectiveIsSkip())
        return false;
      // If setting is MM_CREATE_IF_NOT_EXISTS we create it.
    }
    return true;
  }

  private OWLEntity resolveOWLEntityWithDuplicatesEncoding(ReferenceType referenceType, String namespace)
    throws RendererException
  {
    OWLEntity resolvedOWLEntity = createOWLEntity(referenceType, namespace); // Create entity with an auto-generated rdf:ID
    System.err.println(
      "--processReference: creating " + referenceType + " at this location using location with duplicates encoding");
    return resolvedOWLEntity;
  }

  private OWLEntity resolveOWLEntityWithLocationEncoding(SpreadsheetLocation location, ReferenceType referenceType,
    String namespace) throws RendererException
  {
    OWLEntity resolvedOWLEntity;

    if (hasOWLEntityBeenCreatedAtLocation(location,
      namespace)) { // Has an entity of this type for this location been created already?
      resolvedOWLEntity = getCreatedEntityRDFIDAtLocation(referenceType, location, namespace);
      System.err.println("--processReference: using existing " + referenceType + " " + resolvedOWLEntity
        + " created at this location using location encoding");
    } else { // Create entity with an auto-generated rdf:ID
      resolvedOWLEntity = createOWLEntity(referenceType, namespace);
      recordCreatedOWLEntityRDFIDAtLocation(referenceType, location, namespace, resolvedOWLEntity);
      System.err.println("--processReference: creating " + referenceType + " at this location using location encoding");
    }
    return resolvedOWLEntity;
  }

  private OWLEntity createOrResolveOWLEntityWithEmptyIDAndEmptyLabel(SpreadsheetLocation location,
    ReferenceType referenceType, String defaultNamespace) throws RendererException
  {
    OWLEntity resolvedOWLEntity;

    if (hasOWLEntityBeenCreatedAtLocation(location,
      defaultNamespace)) { // Has an entity for this location been created already?
      resolvedOWLEntity = getCreatedEntityRDFIDAtLocation(referenceType, location, defaultNamespace);
      System.err.println(
        "--processReference: using existing " + referenceType + " " + resolvedOWLEntity + " created at this location");
    } else { // No existing entity created at this location -- create one with an auto-generated rdf:ID
      resolvedOWLEntity = createOWLEntity(referenceType, defaultNamespace);
      System.err.println("--processReference: creating " + referenceType);
      recordCreatedOWLEntityRDFIDAtLocation(referenceType, location, defaultNamespace, resolvedOWLEntity);
    }
    return resolvedOWLEntity;
  }

  // Here, owlEntityName may represent an IRI, a prefixed name, or a short name. If a namespace or prefix is specified for a reference then we assume that it is
  // a fragment and prepend a namespace to it; otherwise we assume it can be either of the three and let createOWLNamedClass take care of it.
  // An empty entityRDFID indicates that one should be generated.
  // TODO: fix so that we are strict about RDFID
  private OWLEntity createOWLEntity(SpreadsheetLocation location, String owlEntityName, ReferenceType referenceType,
    String namespace, ReferenceDirectives referenceDirectives) throws RendererException
  {
    boolean isEmptyName = owlEntityName.equals("");

    if (referenceType.isOWLClass()) {
      return isEmptyName ? createOWLClassWithNamespace(namespace) : createOWLClass(owlEntityName, namespace);
    } else if (referenceType.isOWLIndividual()) {
      return isEmptyName ?
        createOWLNamedIndividualWithNamespace(namespace) :
        createOWLNamedIndividual(owlEntityName, namespace);
    } else if (referenceType.isOWLObjectProperty()) {
      return isEmptyName ?
        createOWLObjectPropertyWithNamespace(namespace) :
        createOWLObjectProperty(owlEntityName, namespace);
    } else if (referenceType.isOWLDataProperty()) {
      return isEmptyName ?
        createOWLDataPropertyWithNamespace(namespace) :
        createOWLDataProperty(owlEntityName, namespace);
    } else
      throw new RendererException(
        "unknown entity type " + referenceType + " for entity " + owlEntityName + " in namespace " + namespace
          + "	in reference " + referenceDirectives + " at location " + location);
  }

  private void addRDFSLabelToOWLEntity(ReferenceType referenceType, OWLEntity resolvedOWLEntity, String namespace,
    String labelText, String language) throws RendererException
  {
    String creationLanguage = ((language != null && language.equals("*"))) ? getDefaultLanguage() : language;

    addRDFSLabelToOWLEntity(resolvedOWLEntity, labelText, language);

    recordCreatedOWLEntityNameWithRDFSLabel(referenceType, namespace, labelText, creationLanguage, resolvedOWLEntity);
    System.err.print("--processReference: adding rdfs:label " + labelText);
    if (creationLanguage == null || creationLanguage.equals(""))
      System.err.println();
    else
      System.err.println(", xml:lang \"" + creationLanguage + "\"");
  }

  private OWLEntity createOWLEntity(ReferenceType referenceType, String namespace) throws RendererException
  {
    if (referenceType.isOWLClass()) {
      return createOWLClassWithNamespace(namespace);
    } else if (referenceType.isOWLIndividual()) {
      return createOWLNamedIndividualWithNamespace(namespace);
    } else if (referenceType.isOWLObjectProperty()) {
      return createOWLObjectPropertyWithNamespace(namespace);
    } else if (referenceType.isOWLDataProperty()) {
      return createOWLDataPropertyWithNamespace(namespace);
    } else
      throw new RendererException("invalid entity type " + referenceType);
  }

  private boolean hasOWLEntityBeenCreatedAtLocation(SpreadsheetLocation location, String namespace)
    throws RendererException
  {
    if (createdOWLEntitiesUsingLocation.containsKey(namespace)) {
      return createdOWLEntitiesUsingLocation.get(namespace).containsKey(location);
    } else
      return false;
  }

  private OWLEntity getCreatedEntityRDFIDAtLocation(ReferenceType referenceType, SpreadsheetLocation location,
    String namespace) throws RendererException
  {
    if (createdOWLEntitiesUsingLocation.containsKey(namespace) && createdOWLEntitiesUsingLocation.get(namespace)
      .containsKey(location))
      return createdOWLEntitiesUsingLocation.get(namespace).get(location);
    else
      throw new RendererException(
        "internal error: " + referenceType + " with namespace " + namespace + " was not created at location " + location);
  }

  private void recordCreatedOWLEntityRDFIDAtLocation(ReferenceType referenceType, SpreadsheetLocation location,
    String namespace, OWLEntity owlEntity) throws RendererException
  {
    if (location != null) {
      if (createdOWLEntitiesUsingLocation.containsKey(namespace)) {
        if (!createdOWLEntitiesUsingLocation.get(namespace).containsKey(location))
          createdOWLEntitiesUsingLocation.get(namespace).put(location, owlEntity);
        else
          checkOWLReferenceType(referenceType, owlEntity);
      } else {
        createdOWLEntitiesUsingLocation.put(namespace, new HashMap<SpreadsheetLocation, OWLEntity>());
        createdOWLEntitiesUsingLocation.get(namespace).put(location, owlEntity);
      }
    }
  }

  private void recordCreatedOWLEntityNameWithRDFSLabel(ReferenceType referenceType, String namespace, String labelText,
    String language, OWLEntity owlEntity) throws RendererException
  {
    String key = language == null ? labelText : labelText + "@" + language;

    if (createdOWLEntitiesUsingLabel.containsKey(namespace)) {
      if (!createdOWLEntitiesUsingLabel.get(namespace).containsKey(key))
        createdOWLEntitiesUsingLabel.get(namespace).put(key, owlEntity);
      else
        checkOWLReferenceType(referenceType, owlEntity);
    } else {
      createdOWLEntitiesUsingLabel.put(namespace, new HashMap<>());
      createdOWLEntitiesUsingLabel.get(namespace).put(key, owlEntity);
    }
  }

  // Duplicate labels are allowed if they are in different namespaces.
  private boolean hasOWLEntityBeenCreatedWithLabel(String namespace, String labelText, String language)
  {
    String key = (language == null || language.equals("")) ? labelText : labelText + "@" + language;

    if (createdOWLEntitiesUsingLabel.containsKey(namespace)) {
      return createdOWLEntitiesUsingLabel.get(namespace).containsKey(key);
    } else
      return false;
  }

  private void checkOWLReferenceType(ReferenceType expectedReferenceType, OWLEntity owlEntity) throws RendererException
  {
    if ((expectedReferenceType.isOWLClass() && !ontology.containsClassInSignature(owlEntity.getIRI())) || (
      expectedReferenceType.isOWLIndividual() && !ontology.containsIndividualInSignature(owlEntity.getIRI())) || (
      expectedReferenceType.isOWLObjectProperty() && !ontology.containsObjectPropertyInSignature(owlEntity.getIRI())) || (
      expectedReferenceType.isOWLDataProperty() && !ontology.containsDataPropertyInSignature(owlEntity.getIRI())))
      throw new RendererException(
        "existing OWL entity with URI " + owlEntity + " has type that differs from expected " + expectedReferenceType
          .getTypeName());

  }

  private OWLEntity getCreatedOWLEntityWithRDFSLabel(ReferenceType referenceType, String namespace, String labelText,
    String language) throws RendererException
  {
    String key = language == null ? labelText : labelText + "@" + language;

    if (createdOWLEntitiesUsingLabel.containsKey(namespace) && createdOWLEntitiesUsingLabel.get(namespace)
      .containsKey(key))
      return createdOWLEntitiesUsingLabel.get(namespace).get(key);
    else
      throw new RendererException(
        "internal error: " + referenceType + " with namespace " + namespace + " was not created with rdfs:label " + key);
  }

  private void addRDFSLabelToOWLEntity(OWLEntity entity, String labelText, String language)
  {
    // TODO
  }

  private String getDefaultLanguage()
  {
    return ""; // TODO
  }

  private OWLEntity getOWLEntityWithRDFSLabel(String labelText)
  {
    return null; // TODO
  }

  private OWLEntity getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(String labelText)
  {
    return null; // TODO
  }

  private OWLEntity getOWLEntityWithRDFSLabelAndLanguage(String labelText, String language)
  {
    return null; // TODO
  }

  private boolean isExistingOWLEntityWithRDFSLabel(String labelText)
  {
    return false; // TODO
  }

  private boolean isExistingOWLEntityWithRDFSLabelAndLanguage(String labelText, String language)
  {
    return false; // TODO
  }

  private boolean isExistingOWLEntityWithRDFID(String rdfID)
  {
    return false; // TODO
  }

  private OWLClass createOWLClass(String className, String namespace)
  {
    return null; // TODO
  }

  private OWLClass createOWLClassWithNamespace(String namespace)
  {
    return null; // TODO
  }

  private OWLNamedIndividual createOWLNamedIndividual(String individualName, String namespace)
  {
    return null; // TODO
  }

  private OWLNamedIndividual createOWLNamedIndividualWithNamespace(String namespace)
  {
    return null; // TODO
  }

  private OWLObjectProperty createOWLObjectProperty(String propertyName, String namespace)
  {
    return null; // TODO
  }

  private OWLObjectProperty createOWLObjectPropertyWithNamespace(String namespace)
  {
    return null; // TODO
  }

  private OWLDataProperty createOWLDataProperty(String propertyName, String namespace)
  {
    return null; // TODO
  }

  private OWLDataProperty createOWLDataPropertyWithNamespace(String namespace)
  {
    return null; // TODO
  }

  private void throwExistingOWLEntityWithLabelException(String labelText, String language) throws RendererException
  {
    String errorMessage = "an entity already exists with the rdfs:label " + labelText;
    if (language != null && !language.equals(""))
      errorMessage += " and language " + language;
    throw new RendererException(errorMessage);
  }

  private void warnExistingOWLEntityWithRDFSLabel(String labelText, String language)
  {
    String errorMessage = "WARNING: an entity already exists with the rdfs:label " + labelText;
    if (language != null && !language.equals(""))
      errorMessage += " and language " + language;
    System.err.println(errorMessage);
  }

  private void throwNoExistingOWLEntityWithRDFSLabelException(String labelText, String language)
    throws RendererException
  {
    String errorMessage = "an entity does not exists with the rdfs:label " + labelText;
    if (language != null && !language.equals(""))
      errorMessage += " and language " + language;
    throw new RendererException(errorMessage);
  }

  private void warnNoExistingOWLEntityWithRDFSLabel(String labelText, String language)
  {
    String errorMessage = "WARNING: an entity does not exists with the rdfs:label " + labelText;
    if (language != null && !language.equals(""))
      errorMessage += " and language " + language;
    System.err.println(errorMessage);
  }

  private void throwExistingOWLEntityWithRDFIDException(String namespace, String rdfID) throws RendererException
  {
    throw new RendererException("an entity already exists in namespace " + namespace + " with the rdf:ID " + rdfID);
  }

  private void warnExistingOWLEntityWithRDFID(String namespace, String rdfID) throws RendererException
  {
    System.err.println("WARNING: an entity already exists in namespace " + namespace + " with the rdf:ID " + rdfID);
  }

  private void throwNoExistingOWLEntityWithRDFIDException(String namespace, String rdfID) throws RendererException
  {
    throw new RendererException("an entity does not exists in namespace " + namespace + " with the rdf:ID " + rdfID);
  }

  private void warnNoExistingOWLEntityWithRDFID(String namespace, String rdfID) throws RendererException
  {
    System.err.println("WARNING: an entity does not exists in namespace " + namespace + " with the rdf:ID " + rdfID);
  }
}
