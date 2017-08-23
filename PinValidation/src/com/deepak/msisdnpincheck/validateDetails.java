package com.deepak.msisdnpincheck;

import java.io.PrintStream;

public class validateDetails
{
  String key;
  String msisdn;
  String pin;
  String action;
  int value;

  public String getMsisdn()
  {
    return this.msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public String getPin() {
    return this.pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getAction() {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public validateDetails()
  {
    System.out.println("inside valueDetails constr ");
  }

  public String getKey() {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}