/* Generated By:JJTree: Do not edit this line. ASTOWLNamedIndividual.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeVisitor;

public
class ASTNamedIndividual extends SimpleNode {
  public ASTNamedIndividual(int id) {
    super(id);
  }

  public ASTNamedIndividual(MappingMasterParser p, int id) {
    super(p, id);
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=6470c9799980bc4dff401a5858b835a3 (do not edit this line) */