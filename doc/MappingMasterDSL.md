''This wiki describes language features available in the 3.4.7 or later
release of Protege-OWL. Earlier versions are missing many of the features
described here.''

MappingMaster uses a domain specific language (DSL) to define mappings from
spreadsheet content to OWL ontologies. This language is based on the
[http://www.co-ode.org/resources/reference/manchester_syntax/ Manchester OWL
Syntax], which is itself a DSL for describing OWL ontologies. The Manchester
Syntax supports the declarative specification of OWL axioms. Some example
Manchester Syntax expressions can be found [http://www.w3.org/TR/owl2
-manchester-syntax/#Quick_Reference here].

For example, a Manchester Syntax declaration of an OWL named class
<tt>Gum</tt> that is a subclass of a named class called <tt>Product</tt> can
be written using using a class declaration clause as:

  * '''Class:''' Gum '''SubClassOf:''' Product 

The MappingMaster DSL extends the Manchester Syntax to support references to
spreadsheet content in these declarations. MappingMaster introduces a new
''reference'' clause for referring to spreadsheet content. In this DSL, any
clause in a Manchester Syntax expression that indicates an OWL named class,
OWL property, OWL individual, data type, or a data value can be substituted
with this reference clause. Any declarations containing such references are
preprocessed and the relevant spreadsheet content specified by these
references is imported. As each declaration is processed, the appropriate
spreadsheet content is retrieved for each reference. This content can then be
used in four main ways:

  * It can be used to directly name OWL entities that are created on demand. 
  * It can be used to annotate OWL entities that are created on demand. 
  * The content may reference existing OWL entities, either directly as a URI or through an annotation property. 
  * Finally, the content may be used as a data value. 

Using one of these approaches, each reference within an expression is thus
resolved during preprocessing to a named OWL entity, a data type, or a data
value. The resulting expression can then be executed by a standard Manchester
Syntax processor.

## Basic References Use In Expressions

Reference in the MappingMaster DSL are prefixed by the character @. These are
generally followed by an Excel-style cell reference. In the standard Excel
cell notation, cells extend from A1 in the top left corner of a sheet within a
spreadsheet to successively higher columns and rows, with alpha characters
referring to columns and numerical values referring to rows . For example, a
reference to cell A5 in a spreadsheet is written as follows:

  * @A5 

The above cell specification indicates that the reference is relative, meaning
that if a formula containing the reference is copied to another cell then the
row and column components of the reference are updated appropriately. An
equivalent absolute reference, again adopting Excel notation, can be written
as follows:

  * @$A$5 

Sheets can also be specified by enclosing them in single quotes and using the
"!" character separator:

  * @'A sheet'!A3 

For example, in the following spreadsheet rows 4 to 6 of column B contain
product categories; columns D to G of row 2 contain state identifiers, and the
grid range D4 to G6 contains sales amounts.

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/ProductSales.png](http
://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/ProductSales.png)

These references can then be used in MappingMaster's DSL to define OWL
constructs using spreadsheet content.

For example, a MappingMaster expression to declare that a class
<tt>FlavouredGum</tt> is a subclass of the class named by the contents of cell
B4 can be written:

  * '''Class:''' FlavouredGum '''SubClassOf:''' @B4 

When processed, this expression will create an OWL named class using the
contents of cell B4 ("Gum") as the class name and declare
<tt>FlavouredGum</tt> to be its subclass. If the class <tt>Gum</tt> already
exists, the subclass relationship will simply be established.

That is, references can be used both to define new OWL entities or to refer to
existing entities.

A similar expression to declare that the class <tt>SalesItem</tt> is
equivalent to the class named by the contents of cell B4 can be written:

  * '''Class:''' SalesItem '''EquivalentTo:''' @B4 

The Manchester Syntax also supports an individual declaration clause for
declaring individuals; property values can be associated with the declared
individuals using a facts subclause, which contains a list of property value
declarations.

For example, an expression to specify that an individual created from the
contents of cell D2 ("CA") has a value of "California" for a data property
value <tt>hasStateName</tt> can be written:

  * '''Individual:''' @D2 '''Facts:''' hasStateName "California" 

Here, an individual will <tt>CA</tt> be created if necessary and associated
with the data property <tt>hasStateName</tt>, which will be given the string
value "California".

Using the standard Manchester Syntax, annotation properties can also be
associated with declared entities.

For example, an existing string data type annotation property called
<tt>hasSource</tt> can be used to associated the above declared California
individual with the source document as follows:

  * '''Individual:''' @D2 '''Facts:''' hasStateName "California" '''Annotations:''' hasSource "DMV Spreadsheet 12/12/2010" 

Classes or properties can be annotated in the same way. For example, a class
can be annotated with the <tt>hasSource</tt> annotation property as follows:

  * '''Class:''' @D2 '''Annotations:''' hasSource "DMV Spreadsheet 12/12/2010" 

The Manchester Syntax also supports the use of OWL class expressions. In
general, a class expression may occur anywhere a named class can occur.

For example, an expression to define a necessary and sufficient condition of a
class <tt>Sale</tt> used the contents of cell D4 as the filler of an
<tt>owl:HasValue</tt> axiom with the property <tt>hasAmount</tt> can be
written:

  * '''Class:''' Sale '''SubClassOf:''' (hasAmount value @D4) 

In general, OWL entities named explicitly in a MappingMaster expression must
already exist in the target ontology. In these examples, the classes
<tt>Sale</tt>, <tt>SalesItem</tt> and <tt>FlavouredGum</tt>, and properties
<tt>hasAmount</tt>, <tt>hasStateName</tt> and <tt>hasSource</tt> must already
exist.

As mentioned, OWL entities specified through cell references are created on
demand by default, though they may also refer to previously declared entities.

## Specifying the Type of a Reference

In the above state declaration example, reference @D2 clearly refers to an OWL
individual. However, the type cannot always be inferred and ambiguities may
arise regarding the type of the referenced entity. To deal with this case,
explicit entity type specifications are supported.

Specifically, a reference may be optionally followed by a parenthesis-enclosed
entity type specification to explicitly declare the type of referenced entity.
This specification can indicate that the entity is a named OWL class, an OWL
object or data property, or an OWL individual or a data type. The
MappingMaster keywords to specify the types are the standard Manchester Syntax
keywords <tt>Class</tt>, <tt>ObjectProperty</tt>, <tt>DataProperty</tt>, and
<tt>Individual</tt>, plus any XSD type name (e.g., <tt>xsd:int</tt>).

Using this specification, the above drug declaration, for example, can be
written:

  * '''Class:''' @A5('''Class''') '''SubClassOf:''' Drug 

A declaration of an individual from cell B5 with an associated property value
from cell C5 that is of type float can be specified as follows:

  * '''Individual:''' @B5 '''Facts:''' hasSalary @C5('''xsd:float''') 

If the <tt>hasSalary</tt> data property is already declared to be of type
<tt>xsd:float</tt> then the explicit type qualification is not needed. A
global default type can also be specified for data values in the case where
the type of the associated data property is either unknown or unspecified or
if no explicit type is provided in the reference.

In many cases, specifying the super class, super property, or individual class
membership of an entity is also desired. While these types of relationships
can be defined using standard Manchester Syntax expressions, this approach
will often entail the use of multiple mapping expressions. To concisely
support defining these types of relationships, a reference may optionally be
followed by a parenthesis-enclosed list of type names. Using this approach,
the above drug declaration, for example, can be written:

  * '''Class:''' @A5(Drug) 

References to OWL properties and individuals can be qualified in the same way.

## Name Resolution for OWL Entities

A variety of name resolution strategies are supported when creating or
referencing OWL entities from cells. The three primary strategies are to:

  * Using <tt>rdf:ID</tt>s to create or resolve entities. 
  * Use <tt>rdfs:label</tt>s to create or resolve entities 
  * Create entities based on the location of a cell ignoring its value. 

With <tt>rdf:ID</tt> encoding, and OWL entity generated from a cell reference
is assigned its <tt>rdf:ID</tt> directly from the cell contents. Obviously,
this content must represent a valid identifier (spaces are not, allowed in
<tt>rdf:ID</tt>s for example).

Using <tt>rdfs:label</tt> encoding, an OWL entity generated from a cell
reference is given an automatically generated (and non meaningful) URI and its
<tt>rdfs:label</tt> annotation value is set to the content of the cell.

With location encoding, an OWL entity generated from a cell reference also
given an automatically generated (and non meaningful) URI but in this case the
cell contents are unused.

The default naming encoding uses the <tt>rdfs:label</tt> annotation property.
The default may also be changed globally.

A name encoding clause is provided to explicitly specify a desired encoding
for a particular reference. As with entity type specifications, this clause is
enclosed by parentheses after the cell reference. The keywords to specify the
three types of encoding are <tt>mm:Location</tt>, <tt>rdf:ID</tt>, and
<tt>rdfs:label</tt>.

Using this clause, a specification of <tt>rdf:ID</tt> encoding for the
previous drug example can be written:

  * '''Class:''' @B4('''rdf:ID''') '''SubClassOf:''' Drug 

As mentioned, MappingMaster also supports entity creation where cell values
are ignored. In this case, the keyword <tt>mm:Location</tt> can be used in
parenthesis following a reference.

For example, an expression to create an individual for cell D4 while ignoring
the contents of the cell can be written:

  * '''Individual:''' @D4('''mm:Location''') 

By default, OWL entities names are resolved or generated using the namespace
of the currently active ontology. The language includes <tt>mm:prefix</tt> and
<tt>mm:namespace</tt> clauses to override this default behavior.

For example, an expression to indicate that an individual created or resolved
from the contents of cell A2 (assuming <tt>rdfs:label</tt> resolution) should
use the namespace identified by the prefix "clinical", can be written:

  * '''Individual:''' @A2('''mm:prefix'''="clinical") 

Similarly, an expression to indicate that it must use the namespace
"http://clinical.stanford.edu/Clinical.owl#" can be written:

  * '''Individual:''' @A2('''mm:namespace'''="http://clinical.stanford.edu/Clinical.owl#") 

Explicit namespace or prefix qualification in reference allows disambiguation
of duplicate labels in an ontology.

## Referring to OWL Entities in Expressions Using Annotation Values

To support direct references to annotation values in expressions,
MappingMaster's DSL adopts the Manchester Syntax mechanism of enclosing these
references in single quotes.

For example, if the OWL class <tt>Product</tt> has an <tt>rdfs:label</tt>
annotation value 'A sellable product' it can be referred as follows:

  * '''Class:''' @B4 '''SubClassOf:''' 'A sellable product' 

A sellable product will be resolved through an annotation value to the class
<tt>Product</tt> when this expression is processed.

## Processing Cell Content

The default behavior is to directly use the contents of the referenced cell
when encoding a name. However, this default can be overridden using an
optional ''value specification clause''. This clause is indicated by the '='
character immediately after the encoding specification keyword and is followed
by a parenthesis-enclosed, comma-separated list of ''value specifications'',
which are appended to each other. These value specifications can be cell
references, quoted values, regular expressions containing capturing groups
(see [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterDSL#nidBPS below]),
or inbuilt text processing functions.

For example, an expression that extends a reference to specify that the entity
created from cell A5 is to use <tt>rdfs:label</tt> name encoding and that the
name is to be the value of the cell preceded by the string "Sale:" can be
written as follows:

  * '''Class:''' @A5('''rdfs:label'''=("Sale:", @A5)) 

Value specification references are not restricted to the referenced cell
itself and may indicate arbitrary cells. More than one encoding can also be
specified for a particular reference so, for example, separate identifier and
label annotation values can be generated for a particular entity using the
contents of different cells.

For example, we can extend the example above to assign the <tt>rdf:ID</tt> of
generated classes to cell B5 as follows:

  * '''Class:''' @A5('''rdf:ID'''=(@B5) '''rdfs:label'''=("Sale:", @A5)) 

If the assignment list includes only a single value then the opening and
closing parenthesis can be omitted:

  * '''Class:''' @A5('''rdf:ID'''=@B5 '''rdfs:label'''=("Sale:", @A5)) 

The language includes several inbuilt text processing methods that be used in
value specifications. At present, several methods are supported. These include
<tt>mm:prepend</tt>, <tt>mm:append</tt>, <tt>mm:toLowerCase</tt>,
<tt>mm:toUpperCase</tt>, <tt>mm:trim</tt>, <tt>mm:reverse</tt>,
<tt>mm:replace</tt>, <tt>mm:replaceAll</tt>, and <tt>mm:replaceFirst</tt>.
These methods take zero or more arguments and return a value. Supplied
arguments may be quoted string or a references.

The <tt>mm:replace</tt> and <tt>mm:replaceAll</tt> functions follow from the
associated methods in the standard Java
[http://download.oracle.com/javase/1.5.0/docs/api/java/lang/String.html
String] class.

For example, the <tt>mm:prepend</tt> method can be used as follows to simplify
the above example:

  * '''Class:''' @A5('''rdfs:label'''='''mm:prepend'''("Sale:")) 

The expression can be further simplified by omitting the explicit
<tt>rdfs:label</tt> qualification if it is the default:

  * '''Class:''' @A5('''mm:prepend'''("Sale:")) 

An expression to convert the contents of cell A5 to upper case before label
assignment in the previous example can be written:

  * '''Class:''' @A5('''mm:toUpperCase'''(@A5)) 

A method can also have an explicit first argument omitted if the argument
refers to the current location value. The previous expression can thus also be
written:

  * '''Class:''' @A5('''mm:toUpperCase''') 

The language supports a data value encoding clause to allow a value
specification clause to be used to assign values to data value. This clause
has a similar form to the <tt>rdf:ID</tt> and <tt>rdfs:label</tt> name
resolution clauses and is introduced by the keyword <tt>mm:DataValue</tt>. For
example, an expression using this clause to create a string data value that is
composed of the contents of cell A5 preceded by the string "Sale:" can be
written:

  * '''Individual:''' @A5 '''Facts:''' hasItems @B5('''mm:DataValue'''='''mm:prepend'''("Sale:")) 

If the parser can determine that the reference is a data value, explicit
qualification can be omitted:

  * '''Individual:''' @A5 '''Facts:''' hasItems @B5('''mm:prepend'''("Sale:")) 

For example, to remove all non alphanumeric characters from a cell before
assignment, the <tt>mm:replaceAll</tt> function can be used as follows:

  * '''Individual:''' @A5 '''Facts:''' hasItems @B5('''mm:replaceAll'''("[^a-zA-Z0-9]","")) 

A similar approach can be used to selectively extract values from referenced
cells. A regular expression
[http://download.oracle.com/javase/tutorial/essential/regex/groups.html
capturing groups] clause is provided and can be used in any position in a
value specification clause. This clause is contained in a quoted string
enclosed by square parenthesis. For example, if cell A5 in a spreadsheet
contains the string "Pfizer:Zyvox" but only the text following the ':'
character is to be used in the label encoding, an appropriate capture
expression could be written as:

  * '''Class:''' @A5('''rdfs:label'''=[":(\S+)"] Drug) 

Note that parentheses around the sub-expressions in a regular expression
clause specify capture groups and indicate that the matched strings are to be
extracted. In some cases, more than one group may be matched for a cell value,
in which case the matched strings are extracted in the order that they are
matched and are appended to each other.

Capturing groups can also be used to generate data values. For example, if
cell A2 in a spreadsheet has a person's forename, middle initial, and surname
separated by a single space, three capturing expressions can be used to
selectively extract each name portion and separately assign them to different
properties as follows:

  * '''Individual:''' @A2 '''Types:''' Person '''Facts:''' hasForename @A2(["(\S+)"]), hasInitial @A2(["\S+\s(\S+)"]), hasSurname @A2(["\S+\s\S+\s(\S+)"]) 

A similar example to separately extract two space-separated integers from a
cell can be written as:

  * '''Individual:''' @A2 '''Types:''' Person '''Facts:''' hasMin @A2(["([-+]\d+)\s+"]), hasMax @A2(["\s+([-+]\d+)"]) 

A more complex variant to convert commma-specified floating point numbers to
dot-specified can be written:

  * '''Individual:''' @A2 '''Types:''' Person '''Facts:''' hasSalary @A2('''xsd:float''' '''mm:DataValue'''=(["([-+]*\d+),"], ".", [",(\d+)"]))) 

If the <tt>hasSalary</tt> property is already of type <tt>xsd:float</tt> then
the explicit qualification is not required here.

Of course, the <tt>mm:replace</tt> method would also work here:

  * '''Individual:''' @A2 '''Types:''' Person '''Facts:''' hasSalary @A2('''mm:replace'''(",", ".")) 

Capturing expressions can also be invoked via the <tt>mm:capturing</tt>
function:

  * '''Individual:''' @A2 '''Types:''' Person '''Facts:''' hasForename @A2('''mm:capturing'''("(\S+)") { 

The syntax of capturing expressions follows that supported by the Java
[http://download.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
Pattern] class.

Value processing functions can also used outside of value specification
clauses - but only if these clause are not used in a reference, and only a
single function can be used.

For example, assuming default <tt>rdfs:label</tt> encoding, the string "_MM"
can be appended to a generated label as follows using the <tt>mm:append</tt>
function:

  * '''Individual:''' @A2('''mm:append'''("_MM")) 

Similarly, the <tt>mm:replace</tt> method can be used to replace commas with
periods when processing data values:

  * '''Individual:''' @A2 '''Facts:''' hasSalary @A3('''xsd:float''' '''mm:replace'''(",", ".")) 

## Iterating Over a Range of Cells in a Reference

Obviously, most mappings will not just reference individual cells but will
instead iterate of a range of columns or rows in a spreadsheet. The wildcard
character '*' can then be used in references to refer to the current column
and/or row in an iteration. MappingMaster provides a graphical interface to
specify these ranges. (They will soon be supported in the DSL.)

Example references using this wildcard notation include:

  * @A3 
  * @A* 
  * @** **

For example, an expression that iterates over the grid D4 to G6 to create an
individual of class <tt>Sale</tt> for each cell can be written:

  * '''Individual:''' @** '''Types:''' Sale **

This expression can be extended to assign property values to these
individuals:

  * '''Individual:''' @** '''Types:''' Sale '''Facts:''' hasAmount @**, hasProduct @B*, hasState @*2 

## Missing Value Handling

To deal with missing cell values, default values can also be specified in
references. A default value clause is provided to assign these values. This
clause is indicated by the keyword mm:default and is followed by a
parenthesis-enclosed, comma-separated list of value specifications. For
example, the following expression uses this clause to indicate that the value
Unknown should be used as the created class label if cell A5 is empty:

  * '''Class:''' @A5('''rdfs:label''' '''mm:default'''=(Unknown) Drug) 

Additional behaviors are also supported to deal with missing cell values. The
default behavior is to skip an entire expression if it contains any references
with empty cells. Four keywords are supplied to modify this behavior. These
keywords indicate that:

  * An error should be thrown if a cell value is missing and the mapping process should be stopped (<tt>mm:ErrorIfEmptyLocation</tt>) 
  * Expressions containing references with empty cells should be skipped (<tt>mm:SkipIfEmptyLocation</tt>) 
  * Expressions containing references with empty cells should generate a warning in addition to being skipped (<tt>mm:WarningIfEmptyLocation</tt>) 
  * Expressions containing such empty cells should be processed (<tt>mm:ProcessIfEmptyLocation</tt>). 

The last option allows processing of spreadsheets that may contain a large
amount of missing values. The option indicates that the language processor
should, if possible, conservatively drop the sub-expression containing the
empty reference rather than dropping the entire expression.

Consider, for example, the following expression declaring an individual from
cell A5 of a spreadsheet and associating a property <tt>hasAge</tt> with it
using the value in cell A6:

  * '''Individual:''' @A5 '''Facts:''' hasAge @A6('''mm:ProcessIfEmptyLocation''') 

Here, using the default skip behavior action, a missing value in cell A5 will
cause the expression to be skipped. However, the process directive for the
hasAge property value in cell A6 will instead drop only the sub-expression
containing it if that cell is empty. So, if cell A5 contains a value and cell
A6 is empty, the resulting expression will still declare an individual.

Using a similar approach, more fine grained empty value handling is also
supported to specify different empty value handling behaviors for
<tt>mm:DataValue</tt>, <tt>rdf:ID</tt> and <tt>rdfs:label</tt> values. Here,
the label directives are <tt>mm:ErrorIfEmptyLabel</tt>,
<tt>mm:SkipIfEmptyLabel</tt>, <tt>mm:WarningIfEmptyLabel</tt>, and
<tt>mm:ProcessIfEmptyLabel</tt>, with equivalent keywords for RDF identifier
and data value handling. These are <tt>mm:ErrorIfEmptyID</tt>,
<tt>mm:SkipIfEmptyID</tt>, <tt>mm:WarningIfEmptyID</tt>,
<tt>mm:ProcessIfEmptyID</tt> and <tt>mm:ErrorIfEmptyDataValue</tt>,
<tt>mm:SkipIfEmptyDataValue</tt>, <tt>mm:WarningIfEmptyDataValue</tt>,
<tt>mm:ProcessIfEmptyDataValue</tt>.

One additional option is provided to deal with empty cell values. This option
is targeted to the common case in many spreadsheets where a particular cell is
supplied with a value and all empty cells below it are implied to have the
same value. In this case, when these empty cells are being processed, their
location must be shifted to the location above it containing a value. For
example, the following expression uses this keyword to indicate that call A5
does not contain a value for the name of the declared class then the row
number must be shifted upwards until a value is found:

  * '''Class:''' @A5('''mm:ShiftUp''' Drug) 

If no value is found, normal empty value handling processing is applied.
Similar directives provide for shifting down (<tt>mm:ShiftDown</tt>), and to
allow shifting to the left (<tt>mm:ShiftLeft</tt>), or to the right
(<tt>mm:ShiftRight</tt>).

## Manchester Syntax Coverage

The DSL supports arbitrary OWL class expressions. The DSL will ultimately
support the entire Manchester syntax. For the moment, because of limitations
in the Manchester OWL Syntax parser in Protege-OWL 3.4, it supports only the
following two additional constructs:

  * '''OWLClassDeclaration''' 
  * '''OWLIndividalDeclaration''' 

## Configuration Options

A set of global defaults can be specified for entity types and name encoding.
The language has a number of clauses to specify these defaults.

The following examples illustrate the use of these clauses together with the
current defaults.

  * '''mm:DefaultNameEncoding''' = '''rdf:ID''' -- Encode entity names as rdf:ID by default 
  * '''mm:DefaultEntityType''' = '''owl:Class''' -- If an entity type cannot be inferred, use owl:Class 
  * '''mm:DefaultPropertyType''' = '''owl:ObjectProperty''' -- If we are expecting a property, use an OWL object property 
  * '''mm:DefaultPropertyValueType''' = '''xsd:String''' -- If we are expecting a data property (object or datatype) value, use xsd:String 
  * '''mm:DefaultDataTypePropertyValueType''' = '''xsd:String''' -- If we are expecting a data property value, use xsd:String 

## Summary

The MappingMaster DSL effectively allows OWL entities to be created from or
reference by arbitrary spreadsheet content. More importantly, the use of the
Manchester syntax allows these entities to be related to each other in complex
ways. Since the Manchester syntax supports the full OWL specification, very
complex interrelationships can be specified.

Declaratively specifying mappings in this way has several advantages. The
writing of these mappings does not require any programming or scripting
expertise. These mappings can be shared easily using the MappingMaster plugin,
which saves the mappings in an OWL ontology. The mappings can easily be
executed repeatedly on different spreadsheets with the same structure. Since
MappingMaster is available as a plugin in Protg-OWL, the results of these
mappings can be examined immediately and the mappings modified as needed and
immediately re-executed, speeding the development process. MappingMaster
provides [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterGUI an
interactive editor] for the mapping DSL that supports on-the-fly entity name
checking and dynamic expansion of entity references.

