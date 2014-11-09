package com.radioline.master.soapconnector;

import android.os.AsyncTask;

import com.radioline.master.basic.Group;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by master on 28.10.2014.
 */
public class Link {
    private String nameSpace;
    private String url;
    private boolean debug = false;

    public Boolean getISWorkUrl() {
        return workUrl;
    }

    public void setIsWorkUrl(Boolean workUrl) {
        this.workUrl = workUrl;
    }

    private Boolean workUrl;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isConnectedToServer(String urlsite, int timeout) {
        try {
            URL myUrl = new URL(urlsite);
            SSLConection.allowAllSSL();
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    public Link(){
        nameSpace = "http://www.rl.ua";
        //url = "https://of.rl.com.ua:6443/GlobalBase/ws/wsPrice.1cws";
        workUrl = false;
        if (isConnectedToServer("https://of.rl.com.ua:6443/",3000)){
            url = "https://of.rl.com.ua:6443/GlobalBase1/ws/wsPrice.1cws";
            workUrl = true;}
        else if (isConnectedToServer("https://of.rl.com.ua:6443/",3000)){
            url = "http://mws-01.rl.int/GlobalBase/ws/wsPrice.1cws";
            workUrl = true;}
        //url = "http://mws-01.rl.int/GlobalBase/ws/wsPrice.1cws";
    }

    public SoapPrimitive getFromServerSoapPrimitive(String methodName)
    {
        String soapAction = nameSpace + "/" + methodName;
        //SoapObject resultRequestSoap;
        SoapObject request = new SoapObject(nameSpace,
                methodName);
        //SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = this.debug;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            httpTransport.call(soapAction, envelope);
            return (SoapPrimitive)envelope.getResponse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SoapPrimitive getFromServerSoapPrimitive(String methodName, PropertyInfo[] properties)
    {
        String soapAction = nameSpace + "/" + methodName;
        //SoapObject resultRequestSoap;
        SoapObject request = new SoapObject(nameSpace,
                methodName);
        for (PropertyInfo propertyInfo:properties) {
            request.addProperty(propertyInfo);
        }

        //SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = this.debug;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            httpTransport.call(soapAction, envelope);
            return (SoapPrimitive)envelope.getResponse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SoapObject getFromServerSoapObject(String methodName, PropertyInfo[] properties)
    {
        String soapAction = nameSpace + "/" + methodName;
        //SoapObject resultRequestSoap;
        SoapObject request = new SoapObject(nameSpace,
                methodName);
        for (PropertyInfo propertyInfo:properties) {
            request.addProperty(propertyInfo);
        }
        //SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = this.debug;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            httpTransport.call(soapAction, envelope);
            return (SoapObject)envelope.getResponse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SoapObject getFromServerSoapObject(String methodName)
    {
        String soapAction = nameSpace + "/" + methodName;
        //SoapObject resultRequestSoap;
        SoapObject request = new SoapObject(nameSpace,
                methodName);
        //SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = this.debug;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            httpTransport.call(soapAction, envelope);
            return (SoapObject)envelope.getResponse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}


