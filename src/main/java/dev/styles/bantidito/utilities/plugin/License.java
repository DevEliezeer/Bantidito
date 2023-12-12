package dev.styles.bantidito.utilities.plugin;

import dev.styles.bantidito.utilities.ServerUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class License {

    private final URI host;
    private final String licenseKey;
    private final String product;
    private final String version;
    private final String apiKey;
    private final String hwid;

    private boolean valid;

    private int statusCode;
    private String discordName;
    private String discordID;
    private String statusMsg;

    public boolean check() {
        HttpPost post = new HttpPost(host);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("licensekey", licenseKey));
        urlParameters.add(new BasicNameValuePair("product", product));
        urlParameters.add(new BasicNameValuePair("version", version));
        urlParameters.add(new BasicNameValuePair("hwid", hwid));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setHeader("Authorization", apiKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post)) {
            String data = EntityUtils.toString(response.getEntity());
            JSONObject obj = new JSONObject(data);
            if(!obj.has("status_msg") || !obj.has("status_id")) {
                valid = false;
                return true;
            }

            statusCode = obj.getInt("status_code");
            statusMsg = obj.getString("status_msg");

            if(obj.getString("status_overview") == null){
                valid = false;
                return true;
            }

            discordName = obj.getString("clientname"); // You can set discord_username too!
            discordID = obj.getString("discord_id");

            valid = true;
            return true;

        } catch (Exception e) {
            ServerUtil.logger(new String[]{
                    "",
                    "&cAn error occurred while connecting to the license server",
                    "&cDisabling Bantidito...",
                    ""
            });

            valid = false;
            return false;
        }
    }
}