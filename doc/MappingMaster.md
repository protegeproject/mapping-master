MappingMaster is an open source Protege-OWL plugin that can be used to
transform the content of spreadsheets in to OWL ontologies. It has two primary
components:

  * '''Domain Specific Language''' Mappings in MappingMaster are specified using a domain specific language (DSL). This DSL is documented [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterDSL here]. 
  * '''MappingMaster Tab''' A graphical user interface for defining, managing, and executing mappings defined using this DSL is also provided. It is documented [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterGUI here]. 

## Publications

  * M.J. O'Connor, C. Halaschek-Wiener, M. A. Musen [http://bmir.stanford.edu/publications/view.php/mapping_master_a_flexible_approach_for_mapping_spreadsheets_to_owl "Mapping Master: A Flexible Approach for Mapping Spreadsheets to OWL"] 9th International Semantic Web Conference ([http://iswc2010.semanticweb.org/ ISWC]), Shanghai, China, 2010. 
  * P. Rocca-Serra, A. Ruttenberg, M. J. O'Connor, T. Whetzel, D. Schober, J. Greenbaum, M. Courtot, S. A. Sansone, R. Scheurmann, B. Peters [http://bmir.stanford.edu/publications/view.php/overcoming_the_ontology_enrichment_bottleneck_with_quick_term_templates "Overcoming the Ontology Enrichment Bottleneck with Quick Term Templates"] Journal of Applied Ontology, 6(1):13-22, 2011. This paper describes the use of Mapping Master to acquire biomedical knowledge from spreadheets. 
  * M.J. O'Connor, C. Halaschek-Wiener, M. A. Musen [http://bmir.stanford.edu/publications/view.php/m2_a_language_for_mapping_spreadsheets_to_owl "M2: A Language for Mapping Spreadsheets to OWL"] OWL: Experiences and Directions Workshop ([http://www.webont.org/owled/2010/ OWLED ]), San Francisco, CA, 2010. This is an earlier version of the ISWC paper. 
  * C. Nyulas, M.J. OConnor, S.W. Tu. "DataMaster - a Plug-in for Importing Schemas and Data from Relational Databases into Protg". 10th International Protg Conference, Budapest, Hungary, 2007. [http://protegewiki.stanford.edu/wiki/DataMaster DataMaster] supports the importation of relational databases into OWL. It can also be used to import spreadsheets with the use of an Excel JDBC driver. 

## Activation

To activate the MappingMaster tab, go to Project | Configure in the Protege-
OWL GUI and check the MappingMasterTab box. The tab will then appear. If the
loaded ontology is then saved the tab will appear when this ontology is opened
in future. The operation of this tab is outlined [http://protege.cim3.net/cgi-
bin/wiki.pl?MappingMasterGUI here].

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png](ht
tp://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png)

## Installation

MappingMaster is part of Protege-OWL 3.4.3 and does not need to be downloaded
separately. However, since many of the MappingMaster components are under
active development, the most recent Protege-OWL 3.4 build should be used when
possible.

## Source Code

MappingMaster is open source. This source code is available for download from
the [http://smi-protege.stanford.edu/svn/mapping-master/trunk/ MappingMaster
Subversion Repository].

## Building

As mentioned, MappingMaster source code can be downloaded from the [https
://smi-protege.stanford.edu/svn/mapping-master/trunk/ Mapping Master
Subversion Repository].

On the command line, with Subversion installed, something like the following
should work:

<tt>svn checkout [https://smi-protege.stanford.edu/repos/protege/mapping-
master/trunk](https://smi-protege.stanford.edu/repos/protege/mapping-
master/trunk) <your-destination-dir></tt>

The root of the download directory has an [http://ant.apache.org/ Ant] build
file that can be used to compile and install it. An option called
<tt>install</tt> can be used to perform an install. The installation step is
expecting an existing Protege installation and determines its location using
the <tt>PROTEGE_HOME</tt> environment variable. An example Protege
installation directory could be <tt>C:/Program Files/Protege_3.4.8</tt>.

In general, it is advisable to download and build the latest version of
Protege and Protege-OWL before building Mapping Master. These can be obtained
at the [http://smi-protege.stanford.edu/svn/protege-core/ Core Protege] and
[http://smi-protege.stanford.edu/svn/owl/ OWL] Subversion repositories.

## Author

MappingMaster was written by
[http://bmir.stanford.edu/people/view.php/martin_j_oconnor Martin O'Connor] at
[http://bmir.stanford.edu/ Stanford Center for Biomedical Informatics
Research].

## How can I get help?

Questions and comments on MappingMaster should be sent to the Protege-OWL
mailing list (protege-owl at lists dot stanford dot edu). This list is
monitored by the developers of this plugin.

