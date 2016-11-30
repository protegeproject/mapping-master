# Compiling the Grammar

We are going to compile the M2 transformation rule grammar using JavaCC version 6.1.2

## Setup

Installing JavaCC 6.1.2 is a bit tricky due to the project is quite inactive (last update was 2 years ago). However by following each step below, you will be able to run JavaCC 6.1.2 in your computer:

* Download [JavaCC 5.0](https://java.net/projects/javacc/downloads/download/javacc-5.0.zip) and unzip it into your selected directory, .e.g, `[JAVACC_HOME]`

> **NOTE**: There is a mistake when distributing JavaCC 6.0, such that, they forgot to include the script files to run javacc [[1](http://stackoverflow.com/questions/18674474/setting-javacc-to-work-with-command-prompt)]. We are going to reuse the scripts from the previous version.

* Download the latest [JavaCC 6.1.2](https://java.net/projects/javacc/downloads/download/releases/Release%206.1.2/javacc-6.1.2.jar) library.

* Replace the JAR file in `[JAVACC_HOME]/bin/lib` with `javacc-6.1.2.jar`. You might need to rename it into `javacc.jar` without the versioning number.

* (Optional) If you are working in UNIX system, create a symbolic link for your convenience [[2](http://stackoverflow.com/questions/1951742/how-to-symlink-a-file-in-linux)].


## Generate the tree nodes

Before we are able to generate the parser file, we need to generate the tree nodes first.

* Type the command below in your command line prompt.

```
$ [JAVACC_HOME]/bin/jjtree MappingMasterParser.jjt
```

* Go to the `target/generated-sources` folder from the project root and you should see a new directory called `jjtree` with all tree node files inside.

```
$ ls -al [PROJECT_ROOT]/target/generated-sources/jjtree
```

## Generate the parser

You are going to need a .jj file to generate the parser files. This file is generated when you compile the grammar file using `jjtree` command.

* Type the command below in your command line promopt.

```
$ [JAVACC_HOME]/bin/javacc ../../../target/generated-sources/jjtree/MappingMasterParser.jj
```

* Go to the `target/generated-sources` folder from the project root and you should see a new directory called `javacc` with all parser files inside.

```
$ ls -al [PROJECT_ROOT]/target/generated-sources/javacc
```

## Renew the tree node files

You ONLY need to replace the tree node files IF you make changes in the grammar file.

Currently, all the tree node files are located in `[PROJECT_ROOT]/src/main/java/org/mm/parser/node` directory. Please take care when you replace the files because some of them have a code manually inserted. Look at the annotated comment `/* XXX: ... */` that indicates the inserted code.

For example:
```java
public
class ASTName extends SimpleNode {

  public String name; 

  public ASTName(int id) { /* XXX: Manually added */
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

## Renew the parser files

You ONLY need to replace the parser files IF you make changes in the grammar file.

Currently, all the parser files are located in `[PROJECT_ROOT]/src/main/java/org/mm/parser` directory. In contrast with the tree node files, it is safe to copy and replace all the parser files.