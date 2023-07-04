package hanwha.neo.branch.ss.mail;

import java.rmi.RemoteException;

import hanwha.neo.branch.ss.common.vo.WsException;
import hanwha.neo.branch.ss.mail.service.MailServiceProxy;
import hanwha.neo.branch.ss.mail.vo.WsMailInfo;
import hanwha.neo.branch.ss.mail.vo.WsRecipient;

public class testmailsend {
	/**
	 * @author 유용원 (201204907)
	 * @paramtype String 배열
	 * @param [제목, 발신메일, 수신메일, 내용]
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		String endpoint = "http://hcom.circle.hanwha.com/api/axis/services/MailService?wsdl";
		MailServiceProxy proxy = new MailServiceProxy();
		proxy.setEndpoint(endpoint);

		WsMailInfo mailInfo = new WsMailInfo();
		mailInfo.setSubject("test");
		mailInfo.setHtmlContent(true);
		mailInfo.setAttachCount(0);
		mailInfo.setSenderEmail("justwon323@hanwha.com");
		mailInfo.setImportant(false);

		WsRecipient[] receivers = new WsRecipient[1];
		receivers[0] = new WsRecipient();
		receivers[0].setSeqID(1);
		receivers[0].setRecvType("TO");
		receivers[0].setRecvEmail("justwon323@hanwha.com");

		try {
			String mailbody = "test content";
			String resultMsg = proxy.sendMISMail(mailbody, mailInfo, receivers, null);
			System.out.println("result :" + resultMsg);
		} catch (WsException e) {
			System.out.println("error");
		}

	}

}
