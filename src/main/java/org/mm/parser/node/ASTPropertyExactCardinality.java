/* Generated By:JJTree: Do not edit this line. ASTPropertyExactCardinality.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTPropertyExactCardinality extends SimpleNode {

  public boolean hasFiller; /* XXX: Manually added */

  public ASTPropertyExactCardinality(int id) {
    super(id);
  }

  public ASTPropertyExactCardinality(MappingMasterParser p, int id) {
    super(p, id);
  }

  public boolean hasFiller() { /* XXX: Manually added */
    return hasFiller;
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=c74dc8928da5ae6d999e8fe685f4fbd2 (do not edit this line) */