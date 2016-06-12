/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.koss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author koss
 * 
 * Didn't tested on other source!!!
 */
public class Downloader {

    public static void main(String[] args) {
        try {
            URL url = urlFromUser();
            loadLinks(url.openStream(), url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get file name from the link.
     *
     * @param url url from user
     * @return fileName for set name for downloaded file
     * @throws IOException
     */
    private static String getFileName(URL url) throws IOException {
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setInstanceFollowRedirects(false); // disable 302 redirect
        String realPath = connect.getHeaderField("Location"); // get real path on server
        String fileName = "";
        if (realPath != null) {
            File file = new File(realPath);
            fileName = file.getName();
        }
        connect.disconnect();
        return fileName;
    }

    /**
     * Method for checkout links. If link - playlist link, method will get files from all playlist.
     * And if link - link for file, method will start download file.
     *
     * @param inputStream from the url from user
     * @param url1        link for get filename
     * @throws IOException
     */
    private static void loadLinks(InputStream inputStream, URL url1) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "Cp1251"));
        try {
            URL url = new URL(reader.readLine());
            String line;
            URL urlForOnePiece;
            while ((line = reader.readLine()) != null) {
                urlForOnePiece = new URL(line);
                loadFiles(urlForOnePiece.openStream(), getFileName(url));
            }
        } catch (MalformedURLException e) {
            loadFiles(inputStream, getFileName(url1));
        }
        reader.close();
    }

    /**
     * Downloads files.
     *
     * @param inputStream
     * @param fileName    sets the filename for downloaded file
     * @throws IOException
     */
    private static void loadFiles(InputStream inputStream, String fileName) throws IOException {
        BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(fileName));
        int b;
        while ((b = bufferedInput.read()) != -1) {
            bufferedOutput.write(b);
        }
        bufferedOutput.close();
        bufferedInput.close();
    }

    /**
     * This method asking user to insert url.
     *
     * @return url from user
     */
    private static URL urlFromUser() throws MalformedURLException {
        String urlFromUser = "";
        while (rightUrl(urlFromUser) != true) {
            urlFromUser = JOptionPane.showInputDialog("Insert URL : ");
        }
        URL url = new URL(urlFromUser);
        return url;
    }

    /**
     * Method for check if URL from user is right.
     * @param urlFromUser information from user
     * @return
     */
    private static Boolean rightUrl(String urlFromUser) {
        boolean boolUrl;
        if (urlFromUser == null || urlFromUser.trim().isEmpty()) {
            boolUrl = false;
            return boolUrl;
        }
        try {
            URL url = new URL(urlFromUser);
            boolUrl = true;
        } catch (MalformedURLException e) {
            boolUrl = false;
        }
        return boolUrl;
    }
    
}
