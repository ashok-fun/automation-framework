package utilis;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.print.DocFlavor.STRING;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jcraft.jsch.Logger;
import com.sun.tools.javac.util.List;

import org.apache.http.HttpConnection;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONString;
import java.io.*;

public class ZephyrExecutionIDRetriver {

	private static final String JIRA_Server = "https://jira.ntrs.com:8443";
	private static final String get_exec_id = JIRA_Server + "/rest/zapi/latest/execution?issueId=";
	private static final String get_issue_id = JIRA_Server + "rest/api/2/issue/";
	private static final String all_projects_uri = JIRA_Server + "rest/api/2/project";
	private static final String all_project_cycles_uri = JIRA_Server + "rest/zapi/latest/cycle?projectId=";
	private static final String all_issues_cycles_uri = JIRA_Server + "rest/zapi/latest/executiom?projectId=";
	private static final String all_issues_cycles_zql = JIRA_Server
			+ "rest/api/latest/zql/exeutionSearch?zqlQuery=project=";

	private static String username = null;
	private static String password = null;

	public static void main(String[] args) throws JSONException, ParseException, IOException {
		LinkedHashMap<String, String> outputMap = new LinkedHashMap<String, String>();
		try {
			FileInputStream zephyrFile = new FileInputStream("./src/test/resources/Jira.properties");
			Library.JIRAConfig.load(zephyrFile);
			username = Library.JIRAConfig.getProperty("usernmae");
			password = Library.JIRAConfig.getProperty("password");
			String updatePropertiesFiles = Library.JIRAConfig.getProperty("updatePropertiesFiles");
			String projectKey = Library.JIRAConfig.getProperty("projectKey");
			String cycleName = Library.JIRAConfig.getProperty("cycleName");
			String issueID = null;
			String executionId = null;
			String projectID = getProjectID(projectKey);
			String cycleID = getCycleID(projectID, cycleName);

			if (cycleID != null) {
				outputMap = getIssueSummaryAndExecutionID(projectID, cycleID, outputMap);
			}
			if (updatePropertiesFiles.equals("Y")) {
				updatePrpertesFile(outputMap);
				formatPropertyFile();
			} else {
				System.out.println("Not updating the data in file");
				for (String key : outputMap.keySet()) {
					System.out.println(key.concat("=").concat(outputMap.get(key)));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updatePrpertesFile(LinkedHashMap<String, String> outputMap) throws IOException {
		FileInputStream in;
		try {
			String filepath = System.getProperty("user.dir").toString().replace("\\", "\\\\")
					+ "\\src\\test\resorces\\Jira.properties";
			in = new FileInputStream(filepath);
			Properties props = new Properties();
			props.load(in);
			in.close();
			Map<String, String> map = new TreeMap<>(outputMap);
			OutputStream output = new FileOutputStream(filepath);
			for (String key : map.keySet()) {
				if (key != null) {
					props.setProperty(key, map.get(key));
					System.out.println("Property entered" + (String) key + (String) map.get(key));

				}
			}
			props.store(output, "");
			output.close();
			System.out.println(" Finished updating the script to mapping file");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void formatPropertyFile() throws IOException  {
		FileInputStream in;
		try {
			String filepath= System.getProperty("user.dir").toString().replace("\\", "\\\\")+ "\\src\\test\resorces\\script_to_test_mapping.properties";
			in = new FileInputStream(filepath);
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			BufferedReader br = new BufferedReader (new FileReader(filepath));
			ArrayList<String> propList = new ArrayList<String>();
			String line;
			while((line = br.readLine()) !=null) {
				propList.add(line);
			}
			Collections.sort(propList);
			FileWriter fw = new FileWriter(filepath);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i=1; i < propList.size(); i++) {
				System.out.println(propList.get(i));
				if (propList.get(i) != null) {
					bw.write(propList.get(i));
					bw.newLine();
				}
			}
			if (bw!= null) {
				bw.close();}
			System.out.println("Finshed updating the data in mapping file");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
		
		}
	}

	public static String getProjectID(String projectKey) throws ParseException, JSONException {
		String projectID = null;
		try {
			if (!projectKey.isEmpty()) {
				String response = null;
				HttpURLConnection con = getRESTConnection(all_projects_uri);
				response = makeRestCall(con, null);
				JSONParser parser = new JSONParser();
				org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) parser.parse(response);

				@SuppressWarnings("uncheckd")
				Iterator<String> projectIterator = jsonArray.iterator();
				while (projectIterator.hasNext()) {
					Object uJson = projectIterator.next();
					JSONObject uj = (JSONObject) uJson;
					if (uj.get("key").toString().equals(projectKey)) {
						projectID = uj.get("id").toString();
						break;
					}
				}
				if (projectID == null) {
					System.out.println("Please enter valid project key in config file");
				}
			} else {
				System.out.println("Please enter  project key in config file");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("failed to get project id");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to get project id");
		}
		return projectID;
	}

	public static String getCycleID(String projectID, String cycleName) throws ParseException, JSONException {
		String response = null;
		String cycleID = null;

		try {
			if (!cycleName.isEmpty() && projectID != null) {

				HttpURLConnection con = getRESTConnection(
						all_project_cycles_uri.concat(projectID).concat("&versionID=&"));

				setHeader(con, "GET");
				response = makeRestCall(con, null);
				JSONParser parser = new JSONParser();
				JSONObject cycleObj = (JSONObject) parser.parse(response);
				for (Iterator<?> cyclesItrator = cycleObj.keySet().iterator(); cyclesItrator.hasNext();) {
					String uJson = (String) cyclesItrator.next();
					JSONArray uj = (JSONArray) cycleObj.get(uJson);
					JSONObject newObj = (JSONObject) uj.get(0);
					for (Iterator<?> iterator = newObj.keySet().iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						JSONObject value = null;
						if (!key.contains("records")) {
							value = (JSONObject) newObj.get(key);
							if (value.get("name").toString().equals("cycleName")) {
								cycleID = key;
								System.out.println("cycleID" + cycleID);
							}

						}
					}

				}
				if (cycleID == null) {
					System.out.println("Please enter valid cycle name in config file");
				}

			} else {
				System.out.println("Please enter  project key and cycle namein config file");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("failed to get project id");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to get project id");
		}
		return projectID;
	}

	public static HttpURLConnection getRESTConnection(String url_str) throws IOException, MalformedURLException {
		URL url = new URL(url_str);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		return connection;

	}

	public static String getBasicAuth(String username, String password) throws IOException, MalformedURLException {
		String auth = username + ":" + decryptWithKey(password, "atralm");
		byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(auth.getBytes());
		return "Basic" + new String(encodedAuth);

	}

	public String encrypyWithKey(String plain, String passwordKey) {
		String b64encoded = java.util.Base64.getEncoder().encodeToString(plain.getBytes());
		String reverse = new StringBuffer(b64encoded).reverse().toString();
		StringBuilder tmp = new StringBuilder();
		final int OFFSET = 4;
		for (int i = 0; i < reverse.length(); i++) {
			tmp.append((char) (reverse.charAt(i) + OFFSET));
		}
		String encPassword = tmp.toString();
		for (int k = 0; k < passwordKey.length(); k++) {
			int mod = (k + 2) % 2;
			if (mod == 1) {
				encPassword = encPassword + (char) (((int) (passwordKey.charAt(k))) + k) + encPassword;

			}
		}
		return encPassword;
	}

	public static String decryptWithKey(String encryptedText, String passwordKey) {
		for (int k = 0; k < passwordKey.length(); k++) {
			int mod = (k + 2) % 2;
			if (mod == 1) {
				encryptedText = encryptedText.substring(0, encryptedText.length() - 1);
			} else {
				encryptedText = encryptedText.substring(1, encryptedText.length());
			}
		}
		StringBuilder tmp = new StringBuilder();
		final int OFFSET = 4;
		for (int i = 0; i < encryptedText.length(); i++) {
			tmp.append((char) (encryptedText.charAt(i) - OFFSET));
		}
		String reveresed = new StringBuffer(tmp.toString()).reverse().toString();
		String decryptedText = new String(java.util.Base64.getDecoder().decode(reveresed));
		return decryptedText;
	}

	private static String makeRestCall(HttpURLConnection con, String testCasrJsonString) throws IOException {

		if (testCasrJsonString != null) {
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = testCasrJsonString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
		}
		String response_str = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			response_str = response.toString();
		}
		con.disconnect();
		return response_str;

	}

	private static void setHeader(HttpURLConnection con, String method) throws MalformedURLException, IOException {
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestMethod(method);
		con.setDoOutput(true);
		con.addRequestProperty("Authorization", getBasicAuth(username, password));
		con.setRequestProperty("Accept", "application/json");
	}

	public static String getIssueID(String issueKey) throws ParseException, JSONException {

		String issueID = null;
		try {
			String response = null;
			if (!issueKey.isEmpty()) {
				HttpURLConnection con = getRESTConnection(get_issue_id.concat(issueKey));
				setHeader(con, "GET");
				response = makeRestCall(con, null);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("failed to get issue id");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to get issue id");
		}
		return issueID;

	}

	public static LinkedHashMap<String, String> getIssueSummaryAndExecutionID(String projectId, String cycleID,
			LinkedHashMap<String, String> outputMap) throws ParseException, JSONException {

		String executionID = null;
		String testCaseName, issueKey = null;

		try {
			String response = null;
			if (projectId != null && cycleID != null) {
				HttpURLConnection con = getRESTConnection(
						all_issues_cycles_uri.concat(projectId).concat("&VersionId=&cycleId=").concat(cycleID));
				setHeader(con, "GET");
				response = makeRestCall(con, null);
				JSONParser parser = new JSONParser();
				JSONObject cyclesObj = (JSONObject) parser.parse(response);

				JSONArray uJson = (JSONArray) cyclesObj.get("executions");
				for (int i = 0; i < uJson.length(); i++) {
					JSONObject uj = (JSONObject) uJson.get(i);
					if (uj.get("cycleId").toString().equals(cycleID)) {
						executionID = uj.get("id").toString();
						testCaseName = uj.get("summary").toString();
						issueKey = uj.get("issueKey").toString();
						outputMap.put(testCaseName, executionID);

					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("failed to get execution id");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to get execution id");
		}

		return outputMap;

	}

}