package com.example.milagro.encuotas;

import android.provider.Settings;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by milagro on 16/04/16.
 */
public class Purchase implements Serializable{
  private final static String LOG_KEY = Purchase.class.getSimpleName();
  private Double inflationRate;
  private Double price;
  private Double usdValue; // How many ARS I need to get one USD
  private Integer shareNumber;
  private String title;

  public Purchase(Double inflationRate, Double price, Double usdValue,
                  Integer shareNumber, String title) {
    this.inflationRate = inflationRate;
    this.price = price;
    this.usdValue = usdValue;
    this.shareNumber = shareNumber;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public Double getRealUsdValue() {
    return getRealValue() / usdValue;
  }

  public Double getRealValue() {
    if (shareNumber == 0) {
      return price;
    }
    Double result = 0.0;
    // 0.08333 = 1/12
    Double monthInflation = Math.pow(1 + inflationRate, 0.08333) - 1;
    Double shareValue = price / shareNumber;  // In ARS
    Double newCurrencyValue = 1.0;

    for (int month=0; month<shareNumber; month++) {
      newCurrencyValue = newCurrencyValue * (1 - monthInflation);
      result = result + shareValue * newCurrencyValue;
    }
    return result;
  }
}
