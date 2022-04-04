package utilis;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.io.*;
import org.apache.poi.util.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import groovy.json.JsonException;

public class RestErrorHandler implements ResponseErrorHandler {

	static String responseError = null;

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (response.getStatusCode().series() == Series.CLIENT_ERROR)
				|| (response.getStatusCode().series() == Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		// OutputStream writer = new StringWriter();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		if (response.getStatusCode().is4xxClientError()) {
			IOUtils.copy(response.getBody(), stream);
			responseError = new String(stream.toByteArray());

		}

	}

	public static void ValidateErrorMessage(HashMap<String, String> testData, String errorMsg) throws ParseException {

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(responseError);
			String responseStatus = (String) jsonObject.get("status");
			String responseError = (String) jsonObject.get("mesage");
			if ((responseStatus.equals("BAD_REQEST")) && (responseError.contains(errorMsg))) {
				System.out.println("Pass");
			} else {
				System.out.println("Fail");

			}
		} catch (JsonException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static void ValidateErrorMessage(HashMap<String, String> testData, String errorMsg, String ValidationType,
			String ValidationField) throws ParseException {

		try {

			if (responseError.contains(errorMsg)) {
				System.out.println("Pass");

			} else {
				System.out.println("Fail");
			}
		} catch (JsonException e) {
			e.printStackTrace();
		}

	}

}
