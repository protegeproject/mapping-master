/* Generated By:JJTree: Do not edit this line. ASTDatatype.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTDatatype extends SimpleNode {
  public ASTDatatype(int id) {
    super(id);
  }

  public ASTDatatype(MappingMasterParser p, int id) {
    super(p, id);
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=493a58ebe676e321f56725bf018cc161 (do not edit this line) */