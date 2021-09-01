package Util;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestFile {
    private String filename;
    private int sizeInMB;
    private File fileObj;
    private String md5sum;

    public TestFile(int sizeMB) throws Exception{
        filename = "dummyfile"+System.currentTimeMillis() + ".txt";
        fileObj = new File(Paths.get(".", filename).toUri());
        sizeInMB = sizeMB;
        if(fileObj.createNewFile())
        {
            System.out.println("New file created: " + filename);
            writeToFile(sizeInMB, filename);
            generateMD5sum(filename);
        } else {
            System.out.println("Error when creating the file");
        }
    }

    public TestFile() {
        System.out.println("File object created for file download test.");
    }

    private void writeToFile(int sizeInMB, String path) {
        try {
            FileWriter writer = new FileWriter(path);
            for (int i = 0; i < sizeInMB * 1024 * 1024; i++) {
                writer.write("a");
            }
            writer.close();
        }catch(Exception ex) {
            System.out.println("File writing error.");
        }
    }

    @Parameters({"filepath"})
    @Test
    private void generateMD5sum(String filepath) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            InputStream is = Files.newInputStream(Paths.get(".", filepath));
            DigestInputStream dis = new DigestInputStream(is, messageDigest);
            md5sum = byteToHex(messageDigest.digest());
            //System.out.println(byteToHex(messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hashing algorithm went wrong.");
        } catch (IOException e) {
            System.out.println("I/O problem.");
        }
    }

    private String byteToHex(byte[] message) {
        StringBuilder sb = new StringBuilder();
        for(byte b: message){
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public Boolean testMD5sum (String providedMD5) {
        generateMD5sum(filename);
        System.out.println("Comparing provided: " + providedMD5 + "\nto actual: " + md5sum);
        if(providedMD5.equals(md5sum)) return true;
        return false;
    }

    public void downloadFile(String downloadURL) {
        String filePath = "testFile"+ System.currentTimeMillis() + ".txt";
        try {
            URL url = new URL(downloadURL);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            this.filename = filePath;
            this.fileObj = new File(filePath);
        } catch (MalformedURLException e) {
            System.out.println("wrong URL!");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ReadableChannel error.");
            //e.printStackTrace();
        }
    }

    public int getSizeInMB() { return sizeInMB; }

    public String getMd5sum() { return md5sum; }

    public File getFile() {  return fileObj; }
}
