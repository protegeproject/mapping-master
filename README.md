Mapping Master
==============

MappingMaster is an open source library that can be used to transform the content of spreadsheets to OWL ontologies. 

See the [Mapping Master Wiki](https://github.com/protegeproject/mapping-master/wiki) for documentation.

#### Building

To build this library you must have the following items installed:

+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ Apache's [Maven](http://maven.apache.org/index.html).

Get a copy of the latest code:

    git clone https://github.com/protegeproject/mapping-master.git 

Change into the mapping-master directory:

    cd mapping-master

Then build it with Maven:

    mvn clean install

On build completion your local Maven repository will contain the generated <tt>mapping-master-${version}.jar</tt> file.

A [Build Project](https://github.com/protegeproject/mapping-master-project) is provided to build core Mapping Master-related components.

#### Questions

If you have questions about this library, please go to the main
Protégé website and subscribe to the [Protégé Developer Support
mailing list](http://protege.stanford.edu/support.php#mailingListSupport).
After subscribing, send messages to protege-dev at lists.stanford.edu.


## Protégé 6 / OWL API 5

MappingMaster 3 targets OWL API 5.5.1 and Java 11. Build and test with
`mvn --batch-mode clean verify`. The spreadsheet regression tests load both
Excel formats, generate class/subclass/annotation axioms, check that previewing
does not apply changes, and save and reopen the resulting ontology.

For a paired Cellfie 3 development build, first run `mvn --batch-mode clean install`
here, then build Cellfie. Cellfie embeds this library; users install only the
Cellfie plugin JAR.

The standard Protégé publishing workflow is in `.github/workflows/publish.yml`.
A numeric release tag matching the POM version publishes signed source, Javadoc,
and binary artifacts to Maven Central using organization publishing secrets.
