# Compiling the Grammar

We are going to compile the MappingMaster transformation-rule grammar using JavaCC version 6.1.2

## Setup

Installing JavaCC 6.1.2 is a bit tricky due to the project is quite inactive (last update was 2 years ago). However by following each step below, you will be able to run JavaCC 6.1.2 in your computer:

* Download [JavaCC 5.0](https://java.net/projects/javacc/downloads/download/javacc-5.0.zip) and unzip it into your selected directory, .e.g, `[JAVACC_HOME]`

> **NOTE**: There is a slight mistake with the JavaCC 6.0 distribution, such that, the author forgot to include the script files to run javacc [[1](http://stackoverflow.com/questions/18674474/setting-javacc-to-work-with-command-prompt)]. We are going to reuse the scripts from the previous version.

* Download the latest [JavaCC 6.1.2](https://java.net/projects/javacc/downloads/download/releases/Release%206.1.2/javacc-6.1.2.jar) library.

* Replace the JAR file in `[JAVACC_HOME]/bin/lib` with `javacc-6.1.2.jar`. You might need to rename it into `javacc.jar` without the versioning number.

* (Optional) If you are working in UNIX system, create a symbolic link for your convenience [[2](http://stackoverflow.com/questions/1951742/how-to-symlink-a-file-in-linux)].

## Parser Building Pipeline

JavaCC is a parser generator that takes a context free grammar and generates a parser for that language. This parser is all what we need for our MappingMaster project. Follow the steps below every time you make changes to the grammar file.

### 1. Generate the tree nodes

The grammar file `MappingMasterParser.jjt` contains the structure definition of the rule language that is used by MappingMaster. The structure is based on the abstract syntax tree (AST) where each language construct is presented as a tree node.

The `jjtree` command will generate those tree nodes as JAVA codes which will be the main sources for our parser tree module.

Type the command below to run the process:

```
$ [JAVACC_HOME]/bin/jjtree MappingMasterParser.jjt
```

The command will create a new directory called `jjtree` in `target/generated-sources` folder. The folder will contain all the tree nodes (i.e., files with prefix 'AST') and an annotated grammar called `MappingMasterParser.jj`.


### 2. Generate the parser

Once we have the annotated grammar, we can start generating the parser code. The `javacc` command will do the task for us automatically.

Type the command below to run the process:

```
$ [JAVACC_HOME]/bin/javacc MappingMasterParser.jj
```

The command will create a new directory called `javacc` in `target/generated-sources` folder. The folder will contain all the parser source codes.

## Working with Grammar

This section will show you the regular procedure every time you change the grammar.

1. Run the pipeline as described above.
2. Replace all the files located in `src/main/java/org/mm/parser` with the new parser codes in `target/generated-sources/javacc`.
3. Replace the file `src/main/java/org/mm/parser/node/MappingMasterParserTreeConstants.java` with the new one `target/generated-sources/jjtree/MappingMasterParserTreeConstants.java`
4. **This fourth step is required iff you introduce a new tree node or rename the existing node in the grammar**: Copy the generated 'AST' files located in the `jjtree` folder to `src/main/java/org/mm/parser/node`. Remove any unused 'AST' files if neccessary.

**IMPORTANT NOTE**

Please take care when you replace the `AST` files because some of them have codes manually inserted. These codes have an annotated comment `/* XXX: ... */` next to them. You need to maintain these extra codes carefully.

The example below shows the instance variable `name`, and the methods `getValue()` and `accept(NodeVisitor)` are manually added. You might lose them if you are not careful when updating the files.
```java
public
class ASTName extends SimpleNode {

  public String name; /* XXX: Manually added */

  public ASTName(int id) {
    super(id);
  }

  public ASTName(MappingMasterParser p, int id) {
    super(p, id);
  }

  public String getValue() { /* XXX: Manually added */
    return name;
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
```
