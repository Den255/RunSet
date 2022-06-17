package org.openjfx;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ClientApi {
    public static int port;
    public static String token;
    public void init() {
        Path filePath = Path.of("/home/den/snap/leagueoflegends/common/.wine/drive_c/Riot Games/League of Legends/lockfile");
        String content = "";
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            System.out.println("Lockfile not found or client not started!");
        }
        String[] split = content.split(":");
        String password = split[3];
        String token = new String(Base64.getEncoder().encode(("riot:" + password).getBytes()));
        int port = Integer.parseInt(split[2]);
        ClientApi.port = port;
        ClientApi.token = token;
        System.out.println("Token: " + token);
        System.out.println("Port: " + port);
    }
    public static String getRequest(String request_url) {
        HttpGet method = new HttpGet();
        try {
            method.setURI(new URI("https://127.0.0.1:" + port + request_url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        method.addHeader("Authorization", "Basic " + token);
        method.addHeader("Accept", "*/*");

        String t = null;
        try (CloseableHttpResponse response = createHttpClient().execute(method)) {
            boolean b = response.getStatusLine().getStatusCode() == 200;
            if (!b) {
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
            }
            else {
                t = dumpStream(response.getEntity().getContent());
                EntityUtils.consume(response.getEntity());
            }
        }catch (ConnectException e){
            System.out.println("Client not started");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }
    public static void postRequest(String requestUrl, String jsonStr) {
        System.out.println("Executing get request");
        HttpPost method = new HttpPost();
        try {
            method.setURI(new URI("https://127.0.0.1:" + port + requestUrl));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        method.addHeader("Authorization", "Basic " + token);
        method.addHeader("Accept", "*/*");

        method.setEntity(
                EntityBuilder.create()
                        .setText(jsonStr)
                        .setContentType(ContentType.APPLICATION_JSON)
                        .build()
        );

        try (CloseableHttpResponse response = createHttpClient().execute(method)) {
            boolean b = response.getStatusLine().getStatusCode() == 200;
            if (!b) {
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
            }
            else {
                String t = dumpStream(response.getEntity().getContent());
                EntityUtils.consume(response.getEntity());
                System.out.println("Response: " + t);
            }
        }catch (ConnectException e){
            System.out.println("Client not started");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void delRequest(String request_url) {
        HttpDelete method = new HttpDelete();
        try {
            method.setURI(new URI("https://127.0.0.1:" + port + request_url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        method.addHeader("Authorization", "Basic " + token);
        method.addHeader("Accept", "*/*");

        try (CloseableHttpResponse response = createHttpClient().execute(method)) {
            boolean b = response.getStatusLine().getStatusCode() == 200;
            if (!b) {
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
            }
            else {
                String t = dumpStream(response.getEntity().getContent());
                EntityUtils.consume(response.getEntity());
                System.out.println("Response: " + t);
            }
        }catch (ConnectException e){
            System.out.println("Client not started");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String dumpStream(InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    private static CloseableHttpClient createHttpClient() throws Exception {
        return HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLUtil.getSocketFactory(), (HostnameVerifier) null))
                .build();
    }
}
