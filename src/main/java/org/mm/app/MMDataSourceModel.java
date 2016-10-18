package org.mm.app;

import java.util.ArrayList;
import java.util.List;

import org.mm.ss.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMDataSourceModel implements DataSourceModel {

   private SpreadSheetDataSource dataSource;

   public MMDataSourceModel() {
      this(new SpreadSheetDataSource());
   }

   public MMDataSourceModel(SpreadSheetDataSource dataSource) {
      this.dataSource = dataSource;
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
