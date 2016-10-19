package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.mm.ss.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMDataSourceModel implements DataSourceModel {

   private final SpreadSheetDataSource dataSource;

   public MMDataSourceModel(@Nonnull SpreadSheetDataSource dataSource) {
      this.dataSource = checkNotNull(dataSource);
   }

   @Override
   public SpreadSheetDataSource getDataSource() {
      return dataSource;
   }

   public List<String> getSheetNames() {
      List<String> sheetNames = new ArrayList<String>();
      if (dataSource != null) {
         sheetNames.addAll(dataSource.getSheetNames());
      }
      return sheetNames;
   }
}
