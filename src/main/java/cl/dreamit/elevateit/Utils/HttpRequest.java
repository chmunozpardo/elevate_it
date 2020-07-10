package cl.dreamit.elevateit.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequest {
    private final String address;
    private final Map<String, Object> parametros;
    private int retrys = 3;
    private int timeout = 5000;

    private final static Logger LOGGER =
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public HttpRequest(String url) {
        this.address = url;
        this.parametros = new HashMap<>();
    }

    public HttpRequest(String url, Map<String, Object> parametros) {
        this.address = url;
        this.parametros = parametros;
    }

    public String serializeParameters() {
        String paramURL = "";
        if (!parametros.isEmpty()) {
            for (String paramName : parametros.keySet()) {
                String value = parametros.get(paramName).toString();
                if (!paramURL.isEmpty()) {
                    paramURL += "&";
                }
                try {
                paramURL += URLEncoder.encode(
                                paramName,
                                StandardCharsets.UTF_8.toString()
                            ) + "=" +
                            URLEncoder.encode(
                                value,
                                StandardCharsets.UTF_8.toString()
                            );
                } catch(UnsupportedEncodingException exp) {}
            }
        }
        LOGGER.log(Level.CONFIG, paramURL);
        return paramURL;
    }

    public String getResponse() {
        String retorno = "";
        String paramURL = this.serializeParameters();
        Log.info(address + "0" + paramURL, true);
        while (retrys > 0) {
            HttpURLConnection conexion = null;
            try {
                URL url = new URL(address);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setConnectTimeout(timeout);
                conexion.setReadTimeout(timeout);
                conexion.setDoOutput(true);
                conexion.setDoInput(true);
                conexion.setInstanceFollowRedirects(true);
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("charset", "utf-8");
                conexion.setRequestProperty("Accept", "*/*");
                conexion.setUseCaches(false);
                DataOutputStream wr =
                    new DataOutputStream(conexion.getOutputStream());
                wr.writeBytes(paramURL);
                wr.flush();
                BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(conexion.getInputStream())
                    );
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    retorno = retorno + inputLine;
                }
                wr.close();
                in.close();
                conexion.disconnect();
                return retorno;
            } catch (MalformedURLException ex) {
                LOGGER.log(Level.SEVERE, "Error" + ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error");
                LOGGER.log(Level.SEVERE, "URL: " + address + "?" + paramURL);
                if (conexion != null) {
                    try {
                        String errorMessage = "";
                        String errorLine;
                        BufferedReader in =
                            new BufferedReader(
                                new InputStreamReader(conexion.getErrorStream())
                            );
                        while ((errorLine = in.readLine()) != null) {
                            errorMessage = errorMessage + errorLine;
                        }
                        in.close();
                        conexion.disconnect();
                        LOGGER.log(Level.INFO, "Respuesta: " + errorMessage);
                    } catch (Exception ex2) {
                    }
                }
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.log(Level.SEVERE, "Error" + ex);
            }
            retrys--;
            try{
                Thread.sleep(5000);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public void setTimeout(int millis) {
        this.timeout = millis;
    }

    public String toString() {
        String paramURL = this.serializeParameters();
        return address + "?" + paramURL;
    }
}
