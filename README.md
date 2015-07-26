SWRLAPI
=======

MappingMaster is an open source library that can be used to transform the content of spreadsheets to OWL ontologies. 

See the [Mapping Master Wiki](https://github.com/protegeproject/mapping-master/wiki) for documentation.

#### Building Prerequisites

To build this library you must have the following items installed:

+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ Apache's [Maven](http://maven.apache.org/index.html).

#### Building

Get a copy of the latest code:

    git clone https://github.com/protegeproject/mapping-master.git 

Change into the mapping-master directory:

    cd mapping-master

Then build it with Maven:

    mvn clean install

On build completion your local Maven repository will contain the generated mapping-master-${version}.jar file.

A [Build Project](https://github.com/protegeproject/mapping-master-project) is provided to build core Mapping Master-related components.
