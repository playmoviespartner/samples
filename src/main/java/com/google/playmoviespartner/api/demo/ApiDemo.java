package com.google.playmoviespartner.api.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

public final class ApiDemo {

  private static final String API_SCOPE = "https://www.googleapis.com/auth/playmovies_partner.readonly";

  private static final String BASE_API = "https://playmoviespartner.googleapis.com";

  private static String listUnfulfilledOrdersUrl(String accountId, int size,
      String nextPageToken) {
    String url = String.format(
        "%s/v1/accounts/%s/orders?status=STATUS_UNFULFILLED&pageSize=%d",
        BASE_API, accountId, size);
    if (nextPageToken != null) {
      try {
        url += "&pageToken="
            + URLEncoder.encode(nextPageToken,
                StandardCharsets.UTF_8.toString());
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Error encoding " + nextPageToken, e);
      }
    }
    return url;
  }

  private static ListOrdersResponse listUnfulFilledOrders(
      GoogleCredential credential, String accountId, int batchSize,
      String nextPageToken) throws IOException {
    credential.refreshToken();
    final String accessToken = credential.getAccessToken();

    HttpTransport transport = new NetHttpTransport();
    HttpRequestFactory httpBasicFactory = transport
        .createRequestFactory(new HttpRequestInitializer() {
          @Override
          public void initialize(HttpRequest request) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-GFE-SSL", true);
            headers.setAuthorization("Bearer " + accessToken);
            request.setHeaders(headers);
          }
        });
    String url = listUnfulfilledOrdersUrl(accountId, batchSize, nextPageToken);
//  System.out.println("URL: " + url);
    HttpRequest request = httpBasicFactory.buildGetRequest(new GenericUrl(url));
    HttpResponse response = request.execute();
    String responseJson = response.parseAsString();
    Gson gson = new Gson();
    return gson.fromJson(responseJson, ListOrdersResponse.class);
  }

  public static void main(final String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Usage: java ApiDemo <account_id> <batch_size> <max_calls>");
      System.exit(0);
    }
    String accountId = args[0];
    int batchSize = Integer.parseInt(args[1]);
    int maxCalls = Integer.parseInt(args[2]);

    final GoogleCredential credential = GoogleCredential.getApplicationDefault()
        .createScoped(Collections.singletonList(API_SCOPE));

    String nextPageToken = null;
    int callNumber = 0;
    int total = 0;
    do {
      callNumber++;
      try {
        ListOrdersResponse ordersResponse =
            listUnfulFilledOrders(credential, accountId, batchSize, nextPageToken);
        // System.out.println("Response: " + ordersResponse);
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
        System.out.printf("Call %d failed (%s), will retry,\n", callNumber,
            e.getMessage());
      }
    } while (nextPageToken != null && callNumber < maxCalls);
  }

  private ApiDemo() {
    throw new UnsupportedOperationException("contains static methdos only");
  }
}
