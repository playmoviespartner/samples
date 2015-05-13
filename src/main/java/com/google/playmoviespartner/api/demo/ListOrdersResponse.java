package com.google.playmoviespartner.api.demo;

import java.util.Collections;
import java.util.List;

import com.google.api.client.util.Objects;

public final class ListOrdersResponse {

  private List<Order> orders = Collections.emptyList();
  private String nextPageToken;

  public List<Order> getOrders() {
    return orders;
  }
  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
  public String getNextPageToken() {
    return nextPageToken;
  }
  public void setNextPageToken(String nextPageToken) {
    this.nextPageToken = nextPageToken;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("size", orders.size())
        .add("nextToken", nextPageToken)
        .add("orders", orders)
        .toString();
  }
}
