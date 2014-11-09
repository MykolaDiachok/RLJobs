package com.radioline.master.soapconnector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.radioline.master.basic.Group;
import com.radioline.master.basic.Item;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.sort;

/**
 * Created by master on 01.11.2014.
 */
public class Converts {
    final String NULL1C="00000000-0000-0000-0000-000000000000";

    LinkAsyncTaskGetSoapObject linkAsync;
    LinkAsyncTaskGetSoapPrimitive linkAsyncTaskGetSoapPrimitive;

    public Group[] getGroupsFromServer() throws ExecutionException, InterruptedException {
        final String method_name = "GetAllGroups";


       // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsync = new LinkAsyncTaskGetSoapObject(method_name);
        //linkAsync.execute();

        SoapObject tSoap = linkAsync.execute().get();
        SoapObject itemsGroups = (SoapObject)tSoap.getProperty("ItemsGroups");

        int groupsCount = itemsGroups.getPropertyCount();
        Group[] Groups = new Group[groupsCount];
        for (int curCount=0;curCount<groupsCount;curCount++) {
            SoapObject item = (SoapObject)itemsGroups.getProperty(curCount);
            Groups[curCount] = new Group(item);
        }
        return Groups;
    }

    public ArrayList<Group> getGroupsArrayListFromServer() throws ExecutionException, InterruptedException {
        final String method_name = "GetGroups";


        // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsync = new LinkAsyncTaskGetSoapObject(method_name);
        //linkAsync.execute();
        PropertyInfo pi = new PropertyInfo();
        pi.setName("GroupId");
        pi.setValue(NULL1C);
        //pi.setValue("5374706f-daf0-11e1-937d-00155d040a09");
        //pi.setType("5374706f-daf0-11e1-937d-00155d040a09".getClass());
        pi.setType(String.class);
        SoapObject tSoap = linkAsync.execute(pi).get();
        if (tSoap==null) {
            return null;
        }
        SoapObject itemsGroups = (SoapObject)tSoap.getProperty("ItemsGroups");

        int groupsCount = itemsGroups.getPropertyCount();
        ArrayList<Group> Groups = new ArrayList<Group>();
        for (int curCount=0;curCount<groupsCount;curCount++) {
            SoapObject item = (SoapObject)itemsGroups.getProperty(curCount);
            Groups.add(new Group(item));
        }

        sort(Groups, new Comparator<Group>() {
            public int compare(Group p1, Group p2) {
                return p1.getSortcode().compareToIgnoreCase(
                        p2.getSortcode());
            }
        });
        return Groups;
    }

    public ArrayList<Group> getGroupsArrayListFromServer(String IdGroup) throws ExecutionException, InterruptedException {
        final String method_name = "GetGroups";


        // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsync = new LinkAsyncTaskGetSoapObject(method_name);
        //linkAsync.execute();
        PropertyInfo pi = new PropertyInfo();
        pi.setName("GroupId");
        pi.setValue(IdGroup);
        //pi.setValue("5374706f-daf0-11e1-937d-00155d040a09");
        //pi.setType("5374706f-daf0-11e1-937d-00155d040a09".getClass());
        pi.setType(String.class);
        SoapObject tSoap = linkAsync.execute(pi).get();
        SoapObject itemsGroups = (SoapObject)tSoap.getProperty("ItemsGroups");

        int groupsCount = itemsGroups.getPropertyCount();
        ArrayList<Group> Groups = new ArrayList<Group>();
        for (int curCount=0;curCount<groupsCount;curCount++) {
            SoapObject item = (SoapObject)itemsGroups.getProperty(curCount);
            Groups.add(new Group(item));
        }

        sort(Groups, new Comparator<Group>() {
            public int compare(Group p1, Group p2) {
                return p1.getSortcode().compareToIgnoreCase(
                        p2.getSortcode());
            }
        });
        return Groups;
    }

    public ArrayList<Item> getItemsArrayListFromServer(String IdGroup) throws ExecutionException, InterruptedException{
        return getItemsArrayListFromServer(IdGroup,false);
    }

    public ArrayList<Item> getItemsArrayListFromServer(String IdGroup,Boolean full) throws ExecutionException, InterruptedException {
        final String method_name = "GetPriceUseParentGroup";


        // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsync = new LinkAsyncTaskGetSoapObject(method_name);
        //linkAsync.execute();
        PropertyInfo pi0 = new PropertyInfo();
        pi0.setName("PartnerId");
        pi0.setValue("a27889a9-4e9f-11e2-8faf-00155d040a09");
        pi0.setType(String.class);

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("GroupId");
        pi1.setValue(IdGroup);
        pi1.setType(String.class);



        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("Simple");
        pi2.setValue(!full);
        pi2.setType(Boolean.class);

        SoapObject tSoap = linkAsync.execute(pi0,pi1,pi2).get();
        SoapObject items = (SoapObject)tSoap.getProperty("Prices");
        //SoapObject items = (SoapObject)prices.getProperty("Item");

        int itemsCount = items.getPropertyCount();
        ArrayList<Item> Items = new ArrayList<Item>();
        for (int curCount=0;curCount<itemsCount;curCount++) {
            SoapObject item = (SoapObject)items.getProperty(curCount);
            Items.add(new Item(item));
        }

        Collections.sort(Items, new Comparator<Item>() {
            public int compare(Item p1, Item p2) {
                return p1.getName().compareToIgnoreCase(
                        p2.getName());
            }
        });
        return Items;
    }



    public Bitmap getBitMapFromServer(String idItem)throws ExecutionException, InterruptedException{
        final String method_name = "GetPNG";


        // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsyncTaskGetSoapPrimitive = new LinkAsyncTaskGetSoapPrimitive(method_name);
        //linkAsync.execute();
        PropertyInfo pi = new PropertyInfo();
        pi.setName("ItemId");
        pi.setValue(idItem);
        pi.setValue("e3dd8ed4-8fa6-11e2-b51b-00155d060502");
        //pi.setType("5374706f-daf0-11e1-937d-00155d040a09".getClass());
        pi.setType(String.class);
        String base64String = linkAsyncTaskGetSoapPrimitive.execute(pi).get().toString();
        byte[] bytearray = Base64.decode(base64String);

        return BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);

    }


    public Bitmap getBitMapFromServer(String idItem, int Height, int Width, int Quality,Boolean HardCompression)throws ExecutionException, InterruptedException{
        final String method_name = "GetPNGWithSize";


        // SoapObject tSoap = getFromServerSoapObject(method_name,soap_action);
        linkAsyncTaskGetSoapPrimitive = new LinkAsyncTaskGetSoapPrimitive(method_name);
        //linkAsync.execute();
        PropertyInfo piidItem = new PropertyInfo();
        piidItem.setName("ItemId");
        piidItem.setValue(idItem);
        //piidItem.setValue("e3dd8ed4-8fa6-11e2-b51b-00155d060502");
        piidItem.setType(String.class);

        PropertyInfo piHeight = new PropertyInfo();
        piHeight.setName("Height");
        piHeight.setValue(Height);
        piHeight.setType(Integer.class);

        PropertyInfo piWidth = new PropertyInfo();
        piWidth.setName("Width");
        piWidth.setValue(Width);
        piWidth.setType(Integer.class);

        PropertyInfo piQuality = new PropertyInfo();
        piQuality.setName("Quality");
        piQuality.setValue(Quality);
        piQuality.setType(Integer.class);

        PropertyInfo piHardCompression = new PropertyInfo();
        piHardCompression.setName("HardCompression");
        piHardCompression.setValue(HardCompression);
        piHardCompression.setType(Boolean.class);

        SoapPrimitive runSoap = linkAsyncTaskGetSoapPrimitive.execute(piidItem,piHeight,piWidth,piQuality,piHardCompression).get();
        if (runSoap==null){
            return null;
        }
        String base64String = runSoap.toString();
        byte[] bytearray = Base64.decode(base64String);

        return BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);

    }




}
