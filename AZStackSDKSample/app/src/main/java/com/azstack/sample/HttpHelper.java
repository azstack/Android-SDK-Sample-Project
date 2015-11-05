package com.azstack.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;

import android.util.Log;

public class HttpHelper {
	private static final String TAG = "HttpHelper";

	/**
	 * Get data from httpget
	 * 
	 * @param url
	 * @return string
	 */
	public static String getData(String url) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpClient client = HttpUtils.getNewHttpClient();
			HttpGet request = new HttpGet(url);

			HttpResponse httpResponse = client.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();

			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while ((line = buffer.readLine()) != null) {
				builder.append(line);
			}

		} catch (Exception e) {
			Log.e(TAG, "Failed to download data: " + e.toString());
			return null;
		}
		return builder.toString();
	}

	/**
	 * Get data from httppost
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String getData(String url, List<NameValuePair> nameValuePairs) {
		String result = null;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = client.execute(request);
			result = inputStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static String inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			return "";
		}
		// Return full string
		return total.toString();
	}

	public static String getData(String url, List<NameValuePair> params,
			List<NameValuePair> headerParams) {
		String result = null;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPost request = new HttpPost(url);
		if (headerParams != null && headerParams.size() > 0) {
			for (int i = 0; i < headerParams.size(); i++) {
				NameValuePair nameValuePair = headerParams.get(i);
				request.addHeader(nameValuePair.getName(),
						nameValuePair.getValue());
			}
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(request);
			result = inputStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String putData(String url,
			List<NameValuePair> nameValuePairs, List<NameValuePair> headerParams) {
		String result = null;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPut request = new HttpPut(url);
		if (headerParams != null && headerParams.size() > 0) {
			for (int i = 0; i < headerParams.size(); i++) {
				NameValuePair nameValuePair = headerParams.get(i);
				request.addHeader(nameValuePair.getName(),
						nameValuePair.getValue());
			}
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = client.execute(request);
			result = inputStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getDataWithHeader(String url,
			List<NameValuePair> headerParams) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpClient client = HttpUtils.getNewHttpClient();
			HttpGet request = new HttpGet(url);
			if (headerParams != null && headerParams.size() > 0) {
				for (int i = 0; i < headerParams.size(); i++) {
					NameValuePair nameValuePair = headerParams.get(i);
					request.addHeader(nameValuePair.getName(),
							nameValuePair.getValue());
				}
			}

			HttpResponse httpResponse = client.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();

			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while ((line = buffer.readLine()) != null) {
				builder.append(line);
			}

		} catch (Exception e) {
			Log.e(TAG, "Failed to download data: " + e.toString());
			return null;
		}
		return builder.toString();
	}

	public static String getDataWithHeaderAndParams(String url,
			List<NameValuePair> headerParams, List<NameValuePair> params) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpClient client = HttpUtils.getNewHttpClient();
			String paramsString = URLEncodedUtils.format(params, "UTF-8");
			HttpGet request = new HttpGet(url + "?" + paramsString);
			if (headerParams != null && headerParams.size() > 0) {
				for (int i = 0; i < headerParams.size(); i++) {
					NameValuePair nameValuePair = headerParams.get(i);
					request.addHeader(nameValuePair.getName(),
							nameValuePair.getValue());
				}
			}
			HttpResponse httpResponse = client.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();

			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while ((line = buffer.readLine()) != null) {
				builder.append(line);
			}

		} catch (Exception e) {
			Log.e(TAG, "Failed to download data: " + e.toString());
			return null;
		}
		return builder.toString();
	}

	public static String deleteData(String url, List<NameValuePair> headerParams) {
		String result = null;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpDelete request = new HttpDelete(url);
		if (headerParams != null && headerParams.size() > 0) {
			for (int i = 0; i < headerParams.size(); i++) {
				NameValuePair nameValuePair = headerParams.get(i);
				request.addHeader(nameValuePair.getName(),
						nameValuePair.getValue());
			}
		}
		try {
			HttpResponse response = client.execute(request);
			result = inputStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
