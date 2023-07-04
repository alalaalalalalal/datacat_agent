package com.main.datacat_agent.service;

import java.rmi.RemoteException;

import hanwha.neo.branch.ss.common.vo.WsException;
import hanwha.neo.branch.ss.mail.service.MailServiceProxy;
import hanwha.neo.branch.ss.mail.vo.WsMailInfo;
import hanwha.neo.branch.ss.mail.vo.WsRecipient;

public class MailSender {

	public String sendTextMail(String endpoint, String subject, String sender, WsRecipient[] receivers,
			String content) throws RemoteException {

		// String endpoint =
		// "http://hcom.circle.hanwha.com/api/axis/services/MailService?wsdl";
		MailServiceProxy proxy = new MailServiceProxy();
		proxy.setEndpoint(endpoint);

		WsMailInfo mailInfo = new WsMailInfo();
		mailInfo.setSubject(subject);
		mailInfo.setHtmlContent(true);
		mailInfo.setAttachCount(0);
		mailInfo.setSenderEmail(sender);
		mailInfo.setImportant(false);

		// WsRecipient[] receivers = new WsRecipient[1];
		// receivers[0] = new WsRecipient();
		// receivers[0].setSeqID(1);
		// receivers[0].setRecvType("TO");
		// receivers[0].setRecvEmail("justwon323@hanwha.com");
		String resultMsg = "";
		try {
			resultMsg = proxy.sendMISMail(content, mailInfo, receivers, null);
			System.out.println("result :" + resultMsg);
		} catch (WsException e) {
			System.out.println("error");
		}
		return resultMsg;

	}

}