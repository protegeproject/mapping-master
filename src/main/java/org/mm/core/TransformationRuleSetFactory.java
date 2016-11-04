package org.mm.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.Gson;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class TransformationRuleSetFactory {

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

   public static void saveTransformationRulesToDocument(@Nonnull String path,
         @Nonnull List<TransformationRule> rules) throws IOException {
      checkNotNull(path);
      checkNotNull(rules);
      String json = new Gson().toJson(TransformationRuleSet.create(rules));
      FileWriter writer = new FileWriter(path);
      writer.write(json);
      writer.close();
   }

   public static void saveTransformationRulesToDocument(@Nonnull File file,
         @Nonnull List<TransformationRule> rules) throws IOException {
      checkNotNull(file);
      checkNotNull(rules);
      String json = new Gson().toJson(TransformationRuleSet.create(rules));
      FileWriter writer = new FileWriter(file);
      writer.write(json);
      writer.close();
   }
}
