package com.google.playmoviespartner.api.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.playmoviespartner.Playmoviespartner;
import com.google.api.services.playmoviespartner.PlaymoviespartnerScopes;
import com.google.api.services.playmoviespartner.model.ListOrdersResponse;
import com.google.api.services.playmoviespartner.model.Order;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class ApiDemo {

  public static void main(final String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Usage: java ApiDemo <account_id> <batch_size> <max_calls>");
      System.exit(0);
    }
    String accountId = args[0];
    int batchSize = Integer.parseInt(args[1]);
    int maxCalls = Integer.parseInt(args[2]);

    GoogleCredential credential = GoogleCredential.getApplicationDefault()
        .createScoped(Collections.singletonList(
            PlaymoviespartnerScopes.PLAYMOVIES_PARTNER_READONLY));
    credential.refreshToken();
    HttpTransport transport = credential.getTransport();
    JsonFactory jsonFactory = credential.getJsonFactory();
    HttpRequestInitializer httpRequestInitializer = credential;

    Playmoviespartner pmp =
        new Playmoviespartner.Builder(transport, jsonFactory, httpRequestInitializer)
            .setApplicationName("Demo application")
            .build();

    String nextPageToken = null;
    int callNumber = 0;
    int total = 0;
    IOException exception;
    boolean lastTry = false;
    do {
      exception = null;
      callNumber++;
      lastTry = callNumber == maxCalls;
      try {

        ListOrdersResponse ordersResponse = pmp.accounts().orders().list(accountId)
            .setPageSize(batchSize)
            .setPageToken(nextPageToken)
            .execute();

        List<Order> orders = ordersResponse.getOrders();
        nextPageToken = ordersResponse.getNextPageToken();
        System.out.printf("\nCall #%d returned %d unfulfilled orders:\n\n", callNumber,
            orders.size());
        for (Order order : orders) {
          total++;
          System.out.printf("%3d: '%-50s' ordered on: '%s' by '%s' to '%s'\n", total,
              order.getName(), order.getOrderedTime(), order.getStudioName(),
              order.getPphName());
        }
      } catch (IOException e) {
        exception = e;
        System.out.printf("Call %d failed, will retry. Error:\n%s", callNumber,
            e.getMessage());
        if (!lastTry) {
          sleep();
        }
      }
    } while ((nextPageToken != null || exception != null) && !lastTry);
  }

  /**
   * Sleeps current thread.
   *
   *  <p>Ideally it should use exponential backoff, but it's not to simplify.
   */
  private static void sleep() {
    int duration = 3000;
    try {
      System.out.printf("Sleeping for %d ms.", duration);
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private ApiDemo() {
    throw new UnsupportedOperationException("contains static methdos only");
  }
}
