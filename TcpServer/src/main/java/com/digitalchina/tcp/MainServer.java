package com.digitalchina.tcp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.xml.soap.SAAJMetaFactory;

public class MainServer {

    public static void main(String[] args) {
        final int port = 1234;
        TcpServer server = new TcpServer(port) {

            @Override
            public void onConnect(SocketTransceiver client) {
                printInfo(client, "Connect");
            }

            @Override
            public void onConnectFailed() {
                System.out.println("Client Connect Failed");
            }

            @Override
            public void onReceive(SocketTransceiver client, String s) {
                printInfo(client, "Send Data: " + s);
                client.send(s);

                String json = JSON.toJSONString(s);
                json = SubString(json);
                System.out.println("json:。。。" +json.length());
                if (json.length()==0){
                    System.out.println("JSON length is 2");
                    return;
                }else {
                        JsonParser jp = new JsonParser();
                        //将json字符串转化成json对象
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        //JSONObject jsonObject = JSONObject.parseObject(json);
                        //JsonObject jo = jp.parse(json).getAsJsonObject();
                        int code = jsonObject.get("code").getAsInt();
                        //String  code =  jsonObject.getString("code");
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                    System.out.println("equipmentNo:" + code);
                    System.out.println("data:" +data.toString());

                }

            }

            @Override
            public void onDisconnect(SocketTransceiver client) {
                printInfo(client, "Disconnect");
            }

            @Override
            public void onServerStop() {
                System.out.println("--------Server Stopped--------");
            }
        };
        System.out.println("--------Server Started--------");
        server.start();
    }

    static void printInfo(SocketTransceiver st, String msg) {
        System.out.println("Client " + st.getInetAddress().getHostAddress());
        System.out.println(" AAA " + msg);
    }

    /**
     * 去除首尾指定字符
     * @param str 字符串
     * @param element 指定字符
     * @return
     */
    public static String trim(String str, String element){
        if (str == null || str.equals("")) return str;
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do{
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }
    //去除字符串前后的双引号
    private static String SubString(String fdId) {
        if(fdId.indexOf("\"")==0){
            fdId = fdId.substring(1,fdId.length());   //去掉第一个 "
        }
        if(fdId.lastIndexOf("\"")==(fdId.length()-1)){

        } fdId = fdId.substring(0,fdId.length()-1);  //去掉最后一个 "

        return fdId;
    }
}