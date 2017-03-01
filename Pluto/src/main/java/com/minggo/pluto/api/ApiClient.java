package com.minggo.pluto.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minggo.pluto.common.PlutoException;
import com.minggo.pluto.model.Result;
import com.minggo.pluto.util.LogUtils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 接口调用客户端
 * @author minggo
 * @time 2014-12-2下午1:32:56
 */
public class ApiClient {
	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 15000;
	private final static int TIMEOUT_SOCKET = 15000;
	private final static int RETRY_TIME = 0;

	private static String appUserAgent;

	private static String getUserAgent() {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder();
			ua.append("/Android");// 手机系统平台
			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			ua.append("/" + android.os.Build.MODEL); // 手机型号
			ua.append("/" + Locale.getDefault().toString()); // 手机语言
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		
		httpClient.getHttpConnectionManager().getParams().setParameter("http.socket.timeout", TIMEOUT_SOCKET);
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
										String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
										  String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}

	public static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}

		return url.toString().replace("?&", "?");
	}
	

	public static <T> Result<T> httpGetModel(String url,Map<String, Object> params) throws PlutoException {
		return httpGetModel(url,params,true);
	}

	public static <T> Result<List<T>> httpGetList(String url,Map<String, Object> params) throws PlutoException {
		return  httpGetList(url,params,true);
	}

	public static <T> Result<T> httpGetModel(String url,Map<String, Object> params,boolean keepChangeLine) throws PlutoException {

		int requestId = new Random().nextInt(10000);
		String urlTemp = url;
		String userAgent = getUserAgent();
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				if (!urlTemp.contains("http")) {
					url = ApiUrl.URL_DOMAIN + urlTemp;//加上域名URL
				}
				url=_MakeURL(url,params);

				LogUtils.info("Get." + requestId + ".URL:" + url);
				httpClient = getHttpClient();

				httpGet = getHttpGet(url, null, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);


				LogUtils.info("Get." + requestId + ".StatusCode:" + statusCode);
				if (statusCode==200||statusCode==404||statusCode==302||statusCode==301||statusCode==504||statusCode==502) {
					responseBody = toString(httpGet.getResponseBodyAsStream(), "utf-8",keepChangeLine);
					LogUtils.info("Get." + requestId + ".Result:" + responseBody);

				}
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.network(e);
			} finally {
				if(httpGet!=null){
					httpGet.releaseConnection();
				}
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		if (responseBody == null || responseBody == "") {
			return null;
		}
		Result<T> result = null;

		if (responseBody!=null) {
			responseBody = responseBody.toString();
		}else{
			LogUtils.info("reader", "Exceptiom-->"+"JSONValue.parse(responseBody)=null,没有json返回");
		}

		Gson gson = new Gson();
		try {
			result = (Result<T>)gson.fromJson(responseBody, new TypeToken<Result<T>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> Result<List<T>> httpGetList(String url,Map<String, Object> params,boolean keepChangeLine) throws PlutoException {

		int requestId = new Random().nextInt(10000);
		String urlTemp = url;

		String userAgent = getUserAgent();
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				if (!urlTemp.contains("http")) {
					url = ApiUrl.URL_DOMAIN + urlTemp;//加上域名URL
				}
				url=_MakeURL(url,params);

				LogUtils.info("Get." + requestId + ".URL:" + url);
				httpClient = getHttpClient();

				httpGet = getHttpGet(url, null, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);


				LogUtils.info("Get." + requestId + ".StatusCode:" + statusCode);
				if (statusCode==200||statusCode==404||statusCode==302||statusCode==301||statusCode==504||statusCode==502) {
					responseBody = toString(httpGet.getResponseBodyAsStream(), "utf-8",keepChangeLine);
					LogUtils.info("Get." + requestId + ".Result:" + responseBody);

				}
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.network(e);
			} finally {
				if(httpGet!=null){
					httpGet.releaseConnection();
				}
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		if (responseBody == null || responseBody == "") {
			return null;
		}
		Result<List<T>> result = null;

		if (responseBody!=null) {
			responseBody = responseBody.toString();

		}else{
			LogUtils.info("reader", "Exceptiom-->"+"JSONValue.parse(responseBody)=null,没有json返回");
		}

		Gson gson = new Gson();
		try {
			result = (Result<List<T>>)gson.fromJson(responseBody, new TypeToken<Result<List<T>>>(){}.getType());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}



	/**
	 * get请求URL
	 *
	 * @param url    不完整url地址
	 * @param params 参数
	 * @return 请求失败返回""
	 * @throws PlutoException
	 */
	public static String http_getString(String url, Map<String, Object> params, boolean keepChangeLine) throws PlutoException {
		int requestId = new Random().nextInt(10000);
		String urlTemp = url;

		String userAgent = getUserAgent();
		HttpClient httpClient;
		GetMethod httpGet = null;
		String responseBody;
		try {
			if (!urlTemp.contains("http")) {
				url = ApiUrl.URL_DOMAIN + urlTemp;//加上域名URL
			}
			url = _MakeURL(url, params);
			LogUtils.infoF("Get.%d.URL:%s", requestId, url);
			httpClient = getHttpClient();
			httpGet = getHttpGet(url, null, userAgent);
			int statusCode = httpClient.executeMethod(httpGet);
			LogUtils.infoF("Get.%d.StatusCode:%d", requestId, statusCode);
			if (statusCode == 200) {
				responseBody = toString(httpGet.getResponseBodyAsStream(), "utf-8", keepChangeLine);
				LogUtils.infoF("Get.%d.Result:%s", requestId, responseBody);
				return responseBody;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return "";
	}



	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws PlutoException
	 */
	public static <T> Result<T> httpPostModel(String url, Map<String, Object> params,Map<String, File> files,
			boolean keepChangeLine) throws PlutoException {
		return httpPostModel(url,params,files,false,keepChangeLine);
	}
	
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws PlutoException
	 */
	public static <T> Result<T> httpPostModel(String url, Map<String, Object> params,
			Map<String, File> files, boolean isphp,boolean keepChangeLine) throws PlutoException {
		int requestId = new Random().nextInt(10000);

		String urlTemp = url;
		String userAgent = getUserAgent();

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				
				if (!urlTemp.contains("http")) {
					url = ApiUrl.URL_DOMAIN + urlTemp;//加上域名URL
				}
				
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, null, userAgent);
				if (files==null){
					//tomcat1.8.5直接用MultipartRequestEntity没法获取参数
					httpPost.setRequestHeader("Content-type","application/x-www-form-urlencoded");
					setPostParams(httpPost,params);
				}else{
					httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));
				}

				LogUtils.info("Post." + requestId + ".URL:" + url);
				showPostParams(requestId, params);
				int statusCode = httpClient.executeMethod(httpPost);
				LogUtils.info("Post." + requestId + ".StatusCode:" + statusCode);
				if (statusCode==200||statusCode==404||statusCode==302||statusCode==301||statusCode==504||statusCode==502) {
					responseBody = toString(httpPost.getResponseBodyAsStream(), "utf-8",keepChangeLine);

					LogUtils.info("Post." + requestId + ".Result:" + responseBody);
				}
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
                        e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
                        e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.network(e);
			} catch (Exception e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
                        e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.network(e);
			} finally {
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
 		Result result = gson.fromJson(responseBody, Result.class);
		return result;
	}

	private static void setPostParams(PostMethod postMethod,Map<String,Object> params){
		for (String name : params.keySet()) {
			postMethod.setParameter(name,String.valueOf(params.get(name)));
			//parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
			// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
		}
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws PlutoException {
		// System.out.println("image_url==> "+url);
		
		String urlTemp = url;
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {

				url = ApiUrl.URL_DOMAIN + urlTemp;//加上域名URL

				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				
				if (statusCode==200||statusCode==404||statusCode==302||statusCode==301||statusCode==504||statusCode==502) {
					InputStream inStream = httpGet.getResponseBodyAsStream();
					bitmap = BitmapFactory.decodeStream(inStream);
					inStream.close();
                    LogUtils.info("domain", "不需要切换域名，正常");
				}
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
                        e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
                        e1.printStackTrace();
					}
					continue;
				}
				e.printStackTrace();
				throw PlutoException.network(e);
			} finally {
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}
	
	
	/**
	 * 将输入流转为字符串,流会被关闭
	 * @param in
	 * @param decoding 解码方式，默认为utf-8
	 * @param keepChangeLine 保持换行符keepChangeLine
	 * @return 异常时返回空串
	 */
	private static final String toString(InputStream in, String decoding ,boolean keepChangeLine) {
		if (in == null) {
			return "";
		} else {
			if (TextUtils.isEmpty(decoding)) {
				decoding = "utf-8";
			}
			
			InputStreamReader isr = null;
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				isr = new InputStreamReader(in, decoding);
				br = new BufferedReader(isr);
				String temp;
				while ((temp = br.readLine()) != null) {
					if (keepChangeLine) {
						sb.append(temp+"\n");
					}else{
						sb.append(temp);
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (isr != null) {
							isr.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (br != null) {
								br.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return sb.toString();
			
		}
	}

	private static void showPostParams(int requestId, Map<String, Object> params) {
		String message = "Post." + requestId + ".Params:";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(message);
		String separator = "&";
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();

			stringBuilder.append(separator);
			stringBuilder.append(String.format(Locale.getDefault(), "%s=%s", key, value));
		}
		//将第一个分隔符删除
		int separatorIndex;
		if ((separatorIndex = stringBuilder.indexOf(separator)) != -1) {
			stringBuilder.deleteCharAt(separatorIndex);
		}
		LogUtils.info(stringBuilder.toString());
	}
}
