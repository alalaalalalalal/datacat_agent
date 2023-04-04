package com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.springframework.stereotype.Component;

import com.jcraft.jsch.*;
@Component
public class SSH2AWScopy {
    private final String username = "ec2-user";
    private final String host = "DEV-Bastion-TEMP-5c1b20d8c89c141e.elb.ap-northeast-2.amazonaws.com";
    private final int port = 10022;
    private final String privateKey = "/Users/jamesyu/Downloads/SIT_DEV_DP_KEY_Bastion.pem";

    private Session session;
    private ChannelExec channelExec;
    private ChannelExec channelExec1;

    private void disConnectSSH() {
        if (session != null) session.disconnect();
        if (channelExec != null) channelExec.disconnect();
    }
    private void connectSSH() throws JSchException {
        JSch jsch=new JSch();
        jsch.addIdentity(privateKey);
        session = jsch.getSession(username, host, port);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");       // 호스트 정보를 검사하지 않도록 설정
        session.connect();
    }
    public void command(String command) {
        try {
            connectSSH();
            channelExec = (ChannelExec) session.openChannel("exec");	// 실행할 channel 생성

            channelExec.setCommand(command);	// 실행할 command 설정
            channelExec.connect();		// command 실행

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            this.disConnectSSH();
        }
    }

    public String getSSHResponse(String region, String command) throws IOException, JSchException, InterruptedException {
        connectSSH();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        channel.setOutputStream(outputStream);
        PrintStream stream = new PrintStream(channel.getOutputStream());
        channel.connect();
   
        stream.println(region);
        stream.flush();
   
        String tempString = waitForPrompt(outputStream);
        stream.println(command);
        stream.flush();
        String result = waitForPrompt(outputStream);
   
        channel.disconnect();
        session.disconnect();
        // channelExec = (ChannelExec) session.openChannel("exec");
        // channelExec.setCommand(region + "&&" + command);
        // channelExec.connect();
        // InputStream inputStream = channelExec.getInputStream();

        // byte[] buffer = new byte[8192];
        // int decodedLength;
        
        // while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
        //     response.append(new String(buffer, 0, decodedLength));


        this.disConnectSSH();
        return result;
        // return response.toString();
    }
    public String waitForPrompt(ByteArrayOutputStream outputStream) throws InterruptedException {
        int retries = 5;
        String result = "";
        for (int x = 1; x < retries; x++) {
           Thread.sleep(1000);
           if (outputStream.toString().indexOf("$") > 0) {
               result = outputStream.toString();
               outputStream.reset();
               return result;
           }
        }
        return result;
     }

}

