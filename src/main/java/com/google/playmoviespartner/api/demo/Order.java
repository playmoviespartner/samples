package com.google.playmoviespartner.api.demo;

import com.google.api.client.util.Objects;

import java.util.Collections;
import java.util.List;

public final class Order {

  String orderId;
  private List<String> countries = Collections.emptyList();
  private String type;
  private String name;
  private String orderedTime;
  private String studioName;
  private String pphName;

  public String getOrderId() {
    return orderId;
  }

  public List<String> getCountries() {
    return countries;
  }

  public String getName() {
    return name;
  }

  public String getOrderedTime() {
    return orderedTime;
  }

  public String getStudioName() {
    return studioName;
  }

  public String getPphName() {
    return pphName;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setCountries(List<String> countries) {
    this.countries = countries;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderedTime(String orderedTime) {
    this.orderedTime = orderedTime;
  }

  public void setStudioName(String studioName) {
    this.studioName = studioName;
  }

  public void setPphName(String pphName) {
    this.pphName = pphName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).omitNullValues()
        .add("orderId", orderId)
        .add("countries", countries)
        .add("type", type)
        .add("name", name)
        .add("orderedTime", orderedTime)
        .add("studioName", studioName)
        .add("pphName", pphName)
        .toString();
  }
}
