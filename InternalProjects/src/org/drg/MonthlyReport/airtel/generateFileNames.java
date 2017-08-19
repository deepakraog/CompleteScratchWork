package org.drg.MonthlyReport.airtel;

import java.io.File;

public class generateFileNames
{
  public String names(File f)
  {
    String[] nameVoid = f.getName().split("_");
    return nameVoid[4] + "_" + nameVoid[5] + "_" + nameVoid[6].replace(".csv.gz", "");
  }
}