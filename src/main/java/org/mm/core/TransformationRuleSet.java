package org.mm.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class TransformationRuleSet implements Iterable<TransformationRule> {

   private Set<TransformationRule> ruleSet;

   public TransformationRuleSet() {
      ruleSet = new HashSet<TransformationRule>();
   }

   public static TransformationRuleSet create(List<TransformationRule> rules) {
      TransformationRuleSet ruleSet = new TransformationRuleSet();
      for (TransformationRule rule : rules) {
         ruleSet.add(rule);
      }
      return ruleSet;
   }

   public Set<TransformationRule> getTransformationRules() {
      return Collections.unmodifiableSet(ruleSet);
   }

   public void setTransformationRules(Set<TransformationRule> ruleSet) {
      this.ruleSet = ruleSet;
   }

   public void add(TransformationRule rule) {
      ruleSet.add(rule);
   }

   public boolean remove(TransformationRule rule) {
      return ruleSet.remove(rule);
   }

   public boolean isEmpty() {
      return ruleSet.isEmpty();
   }

   public boolean contains(TransformationRule rule) {
      return ruleSet.contains(rule);
   }

   public int size() {
      return ruleSet.size();
   }

   @Override
   public Iterator<TransformationRule> iterator() {
      return ruleSet.iterator();
   }
}
