package org.drg.MonthlyReport.airtel;

import java.util.LinkedHashMap;

public class keyPriceDetails
{
  public static LinkedHashMap<String, Double> actProdKeyPP = new LinkedHashMap<String, Double>();
  public static LinkedHashMap<String, Double> renProdKeyPP = new LinkedHashMap<String, Double>();
  double sum = 0.0D;
  String name;

  public void keyPriceAct(String product, Double d)
  {
    if (product != null) {
      this.name = product;
      this.sum = (actProdKeyPP.containsKey(this.name) ? ((Double)actProdKeyPP.get(this.name)).doubleValue() : 0.0D);
      actProdKeyPP.put(this.name, Double.valueOf(this.sum + d.doubleValue()));
    }
  }

  public void keyPriceRen(String product, Double d)
  {
    if (product != null) {
      this.name = product;
      this.sum = (renProdKeyPP.containsKey(this.name) ? ((Double)renProdKeyPP.get(this.name)).doubleValue() : 0.0D);
      renProdKeyPP.put(this.name, Double.valueOf(this.sum + d.doubleValue()));
    }
  }
}