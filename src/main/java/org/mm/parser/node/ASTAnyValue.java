/* Generated By:JJTree: Do not edit this line. ASTAnyValue.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTAnyValue extends SimpleNode {
  public ASTAnyValue(int id) {
    super(id);
  }

  public ASTAnyValue(MappingMasterParser p, int id) {
    super(p, id);
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=0fb2fdc80f4ed4da28b0172f8c94a9d7 (do not edit this line) */