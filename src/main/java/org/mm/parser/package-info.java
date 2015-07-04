/**
 * This package contains user-modified JJTree-generated node files (named AST*.java).
 * (These files were manually modified to add extra state. JJTree has no way of specifying the 
 * addition of this state to generated AST node classes.) 
 *
 * By default, AST files generated by JJTree are placed in the ./target/generate-sources/ directory.
 * If an AST file for a node is present in this source package directory then the JJTree Maven 
 * plugin simply copies the file to the ./target/generate-sources directory. AST node files that
 * do not require the addition of extra state are copied into the ./target/generate-sources directory
 * during JJTree process. Unlike the modified AST files, they are not under source control.
 */

package org.mm.parser;