/* Generated By:JJTree: Do not edit this line. ASTOWLDifferentFrom.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeVisitor;

public
class ASTDifferentFrom extends SimpleNode {
  public ASTDifferentFrom(int id) {
    super(id);
  }

  public ASTDifferentFrom(MappingMasterParser p, int id) {
    super(p, id);
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=0238a1584e2af5497a3a2e74263f28b1 (do not edit this line) */