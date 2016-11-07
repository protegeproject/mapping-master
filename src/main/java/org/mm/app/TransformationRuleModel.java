package org.mm.app;

import java.util.List;

import org.mm.transformationrule.TransformationRule;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface TransformationRuleModel {

   List<TransformationRule> getRules();
}
