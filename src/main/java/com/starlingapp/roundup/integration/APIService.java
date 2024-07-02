package com.starlingapp.roundup.integration;

import java.util.Map;

public interface APIService {

    <R, B> R put(String path, B body, Class<R> responseType);

    <R> R get(String path, Class<R> responseType);

    <R> R getWithParams(String path, Map<String, String> queryParams, Class<R> responseType);
}
