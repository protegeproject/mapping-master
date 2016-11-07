package org.mm.transformationrule;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.gson.Gson;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class TransformationRuleSetManager {

   @Nonnull
   public static TransformationRuleSet createEmptyTransformationRuleSet() {
      return new TransformationRuleSet();
   }

   @Nonnull
   public static TransformationRuleSet loadTransformationRulesFromDocument(@Nonnull InputStream inputStream)
         throws FileNotFoundException {
      checkNotNull(inputStream);
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      return new Gson().fromJson(br, TransformationRuleSet.class);
   }

   @Nonnull
   public static TransformationRuleSet loadTransformationRulesFromDocument(@Nonnull File file)
         throws FileNotFoundException {
      checkNotNull(file);
      BufferedReader br = new BufferedReader(new FileReader(file));
      return new Gson().fromJson(br, TransformationRuleSet.class);
   }

   public static void saveTransformationRulesToDocument(@Nonnull File file,
         @Nonnull Collection<TransformationRule> rules) throws IOException {
      TransformationRuleSet ruleSet = TransformationRuleSet.create(rules);
      saveTransformationRulesToDocument(file, ruleSet);
   }

   public static void saveTransformationRulesToDocument(@Nonnull File file,
         @Nonnull TransformationRuleSet ruleSet) throws IOException {
      checkNotNull(file);
      checkNotNull(ruleSet);
      String json = new Gson().toJson(TransformationRuleSet.create(ruleSet));
      FileWriter writer = new FileWriter(file);
      writer.write(json);
      writer.close();
   }
}
