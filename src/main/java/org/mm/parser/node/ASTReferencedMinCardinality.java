/* Generated By:JJTree: Do not edit this line. ASTReferencedMinCardinality.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTReferencedMinCardinality extends SimpleNode {

   public boolean hasFiller; /* XXX: Manually added */

   public ASTReferencedMinCardinality(int id) {
     super(id);
   }

   public ASTReferencedMinCardinality(MappingMasterParser p, int id) {
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
/* JavaCC - OriginalChecksum=fe2fa9d70ae89376a4c2fc52cdcb48e9 (do not edit this line) */