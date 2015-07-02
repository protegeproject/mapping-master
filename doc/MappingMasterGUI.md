The MappingMaster plugin provides a graphical user interface for defining,
managing, and executing mappings defined using the [http://protege.cim3.net
/cgi-bin/wiki.pl?MappingMasterDSL MappingMaster DSL]. When the MappingMaster
tab is [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMaster#nidAYU
activated] for a particular ontology this GUI is accessible as a tab within
Protege-OWL.

Mappings defined using [http://protege.cim3.net/cgi-
bin/wiki.pl?MappingMasterDSL MappingMaster's DSL] are saved in an OWL mapping
ontology. This OWL ontology is independent of the source ontology that the
mappings apply to and is saved separately. The process of defining and
executing mappings on a particular source ontology in MapingMaster is as
follows:

  * Load the source ontology in Protege-OWL and activate the MappingMaster tab. 
  * Navigate to the tab and open the spreadsheet to be used in the mapping. 
  * Define the desired mappings using [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterDSL MappingMasters DSL] or load existing mappings from a previously created mapping ontology. 
  * Save these mappings in an OWL mappings ontology. 
  * Execute the mappings. 
  * Review the mappings and optionally save them into a target ontology. 

The MappingMaster tab is designed to support this workflow.

## MappingMaster Tab

The tab is divided horizontally into two areas. The top area provides a
spreadsheet preview and allows users to visualize the contents of a
spreadsheet. This preview presents a standard tabular preview of a spreadsheet
and has a series of tabs to show the contents of individual sheets within the
spreadsheet. It does not support editing of the content of the sheets. Buttons
allow spreadsheets to be opened and closed.

Here, for example, is a view of the MappingMaster tab with a spreadsheet
called <tt>MMWikiExamples.xsl</tt> loaded.

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png](ht
tp://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png)

The bottom area provides mechanisms to create, manage, and execute expressions
in [http://protege.cim3.net/cgi-bin/wiki.pl?MappingMasterDSL MappingMaster's
DSL]. It is divided into two subtabs, which are called the Control and
Mappings.

The Control subtab allows mapping ontologies to be loaded, saved, and
executed. It also presents a diagnostic screen to show the mapping process.
Various mapping configuration options can also be selected in this subtab.

## MappingMaster Expressions View

The Mapping subtab provides a tabular view of [http://protege.cim3.net/cgi-
bin/wiki.pl?MappingMasterDSL MappingMaster DSL] expressions and provides a
popup editor for editing these expressions.

Here is a view of this subtab with no loaded expressions.

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionsViewEmpty
.png](http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionsView
Empty.png)

## Editing a MappingMaster Expressions

Within this subtab, the Add button can be clicked to pop up an expression
editor dialog. Within this editor dynamic expansion of terms in the loaded
ontology is supported. In this dialog the user can enter their mapping
expression.

For example, if the user has the following spreadsheet

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/ProductSales.png](http
://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/ProductSales.png)

and wishes to enter the following MappingMaster expression:

<tt> Individual: @**(Sale) Facts: hasAmount @** hasProduct @B*(Product)
hasState @*2(State) </tt>

and would like that expression to iterate over the cells D4:G6 from 'Sheet1',
they can fill in this dialog as follows:

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionDialog.png
](http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionDialog.pn
g)

When this expression is saved, a condensed version of it will appear in a
tabular view.

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionsViewFille
d.png](http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMExpressionsVie
wFilled.png)

## Executing a MappingMaster Expressions

When all expressions have been added, the user can navigate to the Mappings
Control subtab to execute the mappings.

Pressing the "Map" button in this subtab will execute all mappings. If an any
errors occur during mapping a dialog box will pop up.

[http://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png](ht
tp://swrl.stanford.edu/MappingMaster/1.0/ScreenShots/MMWorkbookView.png)

## Saving and Loading Mapping Expressions

The mappings can also be be saved in a mapping ontology using the Mapping
Control tab.

To save a new set of mappings press the "Save As..." button in the "Mapping
Ontology" subpanel, whereupon a dialog box will pop up that can be used to
name the mapping ontology.

Existing mappings can also be opened in this subtab by pressing the "Open"
button. The "Save" button can be used to save any changes to an open mapping
ontology. An open mapping ontology can be closed using the "Close" button.

