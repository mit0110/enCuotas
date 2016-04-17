package com.example.milagro.encuotas;

import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;

import static org.junit.Assert.*;

/**
 * Created by milagro on 16/04/16.
 */
public class PurchaseTest {
  @Test
  public void testNoInflation() {
    // Without inflation, cash and share prices are equal.
    Double realPrice = 100.0;
    Purchase purchase = new Purchase(0.0, realPrice, 1.0, 12, "");
    assertEquals(purchase.getRealValue(), realPrice, 0.01);
  }

  @Test
  public void testNoShares() {
    // Without inflation, cash and share prices are equal.
    Double realPrice = 100.0;
    Purchase purchase = new Purchase(0.3, realPrice, 1.0, 0, "");
    assertEquals(purchase.getRealValue(), realPrice, 0.01);
  }

  @Test
  public void testOneShare() {
    Double realPrice = 100.0;
    Purchase purchase = new Purchase(0.5, realPrice, 1.0, 1, "");
    Double monthInflation = Math.pow(1.5, 0.08333) - 1;
    // Because the first share is payed in a month, the values are different.
    Double expectedSharePrice = realPrice * (1 - monthInflation);
    assertEquals(expectedSharePrice, purchase.getRealValue(), 0.01);
  }

  @Test
  public void testSharePrice() {
    Purchase purchase = new Purchase(0.3, 90.0, 1.0, 3, "");
    Double expectedSharePrice = 86.0805;
    assertEquals(expectedSharePrice, purchase.getRealValue(), 0.01);
  }

  @Test
  public void testSharePriceWithUsd() {
    Purchase purchase = new Purchase(0.3, 90.0, 2.0, 3, "");
    // The value of the USD does not affect the real value
    Double expectedSharePrice = 86.0805;
    assertEquals(expectedSharePrice, purchase.getRealValue(), 0.01);
  }

  @Test
  public void testCashUsdPrice() {
    Purchase purchase = new Purchase(0.3, 90.0, 2.0, 0, "");
    // The value of the USD does not affect the real value
    Double expectedSharePrice = 45.0;
    assertEquals(expectedSharePrice, purchase.getRealUsdValue(), 0.01);
  }

  @Test
  public void testShareUsdPrice() {
    Purchase purchase = new Purchase(0.3, 90.0, 2.0, 3, "");
    // The value of the USD does not affect the real value
    Double expectedSharePrice = 86.0805 / 2;
    assertEquals(expectedSharePrice, purchase.getRealUsdValue(), 0.01);
  }
}
