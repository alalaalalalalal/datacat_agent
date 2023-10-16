package hanwha.neo.branch.ss.datacat_agent;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import hanwha.neo.branch.ss.common.vo.WsException;
import hanwha.neo.branch.ss.datacat_agent.entity.ExecutionLogEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageMailEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.ScriptEntity;
import hanwha.neo.branch.ss.datacat_agent.service.DatacatAgentService;
import hanwha.neo.branch.ss.datacat_agent.service.DatacatAgentServiceImpl;
import hanwha.neo.branch.ss.mail.service.MailSender;
import hanwha.neo.branch.ss.mail.service.MailServiceProxy;
import hanwha.neo.branch.ss.mail.vo.WsMailInfo;
import hanwha.neo.branch.ss.mail.vo.WsRecipient;
import lombok.extern.slf4j.Slf4j;


/**
 * @author 유용원
 * @param (1번 : "실행환경") ex : us-dev (위치-개발/운영)
 */
@Slf4j
@SpringBootApplication
public class DatacatAgentApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(DatacatAgentApplication.class, args);

	}

	// 메일 관련
	private final String MailEndpoint = "http://hcom.circle.hanwha.com/api/axis/services/MailService?wsdl";
	private final String sender = "justwon323@hanwha.com";

	@Bean
	public DatacatAgentService getDatacatAgentService() {
		return new DatacatAgentServiceImpl();
	}

	@Override
	public void run(String... args) throws Exception {
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



		if (args == null || args.length == 0) {
			log.info("인자 전달이 필요 합니다. ex: dev-us (미국 개발계)");
		} else if(args[0].equals("test")){
			String[] scriptCommand = { "/bin/sh", "-c", args[1] };

			String result = "";
			result = getDatacatAgentService().execShellScript(scriptCommand);
			log.info("스크립트 결과 확인" + result);
		}else{
			log.info("인자 확인" + args[0]);
			while (true) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String hour = sdf.format(timestamp).replace(":", "");
				log.info ("시간 : "+ hour);
				log.info ("아규먼트 : "+args[1]);
			 	// if(hour.startsWith("00")){ // 9시 발송 개발계 IP 메일api에 미등록으로 UTC기준으로 변경
					if("y".equals(args[1])){
						log.info("##@@메일전송##@@");
						sendItrm();
					}
			 	// }
				List<ScriptEntity> scriptList = getDatacatAgentService().readScript(args[0]);
				for (ScriptEntity scriptEntity : scriptList) {
					if (scriptEntity != null) {
						log.info("스크립트 리드 성공");


						String starTtime = scriptEntity.getStartTime().toString().replace(":", "");
						String endTime = scriptEntity.getEndTime().toString().replace(":", "");

						if (Integer.valueOf(hour) >= Integer.valueOf(starTtime)
								&& Integer.valueOf(hour) <= Integer.valueOf(endTime)) { // 언제부터 언제까지 실행해야 하는지 체크
							log.info("스크립트 실행 성공");
							log.info(hour + " / " + starTtime + " / " + endTime);
							log.info("id : " + scriptEntity.getPid());
							try{
								executeK8s(scriptEntity, args[0]);
							}catch(Exception e){
								log.error("@@@@@@@@@@@@@@@@ 에러 발생 @@@@@@@@@@@@@@@@@" + e.toString());
							}
						} else {
							log.info("스크립트 시간 체크로 제외됨");
						}
					}else{
						log.info("스크립트 리드 실패");
					}
				}

				
				Thread.sleep(1000 * 60 * 5); // 5분에 한번씩 체크
			}
		}
	}

	public void sendItrm() throws RemoteException  {
		List<MessageMailEntity> messageMailList =  getDatacatAgentService().selectItrmMail("0");
		if(!messageMailList.isEmpty()){
			log.info("메일 전송중@@@@@");
			MessageMailEntity messageMailEntity = messageMailList.get(0);
			String[] recvList = messageMailEntity.getMailRecvGroup().split("\\|\\|");
			WsRecipient[] receivers = new WsRecipient[1];
			for (String receiver : recvList){
				log.info("리시버 {}" ,receiver);
				receivers[0] = new WsRecipient();
				receivers[0].setSeqID(1);
				receivers[0].setRecvType("TO");
				receivers[0].setRecvEmail(receiver);
				String content = messageMailEntity.getMailContents();

				MailSender mailSender = new MailSender();
				mailSender.sendTextMail(MailEndpoint, messageMailEntity.getMailSubject(), sender, receivers,
					content);
			}


			getDatacatAgentService().updateMailstatus(messageMailEntity.getSeq()); // 메일 발송 후 상태 업데이트
		}
		log.info("메일 전송 대상이 없습니다???");
	}
	public void executeK8s(ScriptEntity scriptEntity, String env) throws RemoteException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		String utctimestamp = "";

		Calendar utc = Calendar.getInstance();
		utc.setTime(timestamp);
		utc.add(Calendar.HOUR, 9); // 마지막 실행결과 시간 + 인터벌
		utctimestamp = new Timestamp(utc.getTimeInMillis()).toString();

		// 현재시간 구함
		String[] scriptCommand = { "/bin/sh", "-c", scriptEntity.getCommand() };

		log.info("스크립트 log={}", scriptCommand[2]);
		int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();

		Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);
		String result = "";
		if (lastExcutionAt == null) { // 최초실행
			result = getDatacatAgentService().execShellScript(scriptCommand);

			log.info("실행결과 = {}", result);
			// true 가 0 false 가 0 아닌것
			if (!result.equals("0") || result.length() > 1) {// 비정상
				WsRecipient[] receivers = new WsRecipient[2];
				receivers[0] = new WsRecipient();
				receivers[0].setSeqID(1);
				receivers[0].setRecvType("TO");
				receivers[0].setRecvEmail("justwon323@hanwha.com");

				receivers[1] = new WsRecipient();
				receivers[1].setSeqID(1);
				receivers[1].setRecvType("TO");
				receivers[1].setRecvEmail("true84you@hanwha.com");
				String content = "<!DOCTYPE html> \n"
				+ " <html> \n"
				+ " <head> \n"
				+ "   <title>한화컨버젼스 점검항목 확인 </title> \n"
				+ " </head> \n"
				+ " <body style='font-family: Arial, Helvetica, sans-serif, text-align: center;'> \n"
				+ "   <table cellpadding='0' cellspacing='0' border='0' style='width: 970px; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; margin: auto'> \n"
				+ "   <tr> \n"
				+ "     <td> \n"
				+ " 	  <table cellpadding='0' cellspacing='0' border='0' style='width: 100%; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;'> \n"
				+ " 		<tr bgcolor=#272f39 > \n"
				+ " 		  <td style='padding: 1px; width:20%; text-align: left; font-weight: bold; border-bottom: 1px solid #ddd;'> \n"
				+ " 			<img width=150 src='https://heis2.hanwha.com/_nuxt/img/sitlogo.png'> \n"
				+ " 		  </td> \n"
				+ " 		  <td style='padding: 1px; width:55%; text-align: center; font-size: 15px; font-weight: bold; border-bottom: 1px solid #ddd; color: #FFFFFF'>ITRM BATCH REPORT </td> \n"
				+ " 		  <td style='padding: 1px; width:25%;  text-align: left; font-size: 11px; font-weight: bold; border-bottom: 1px solid #ddd;color: #FFFFFF'>점검자: SYSTEM<br>점검일시: "+ timestamp.toString() +" <br> 점검일시: "+ utctimestamp +" (UTC+9) </td> \n"
				+ " 		</tr> \n"
				+ " 		<tr> \n"
				+ " 		  <td></td> \n"
				+ " 		</tr> \n"
				+ " 	  </table>  \n"
				+ " 	</td> \n"
				+ "   </tr> \n"
				+ "   <tr> \n"
				+ "     <td> \n"
				+ "		&nbsp;\n"
				+ " 	</td> \n"
				+ "   </tr> \n"
				+ "   <tr> \n"
				+ "     <td> \n"
				+ " 	  <h2 style='font-size: 18px; margin-top: 20px;text-decoration: underline;'>확인 항목</h2> \n"
				+ " 	  <table cellpadding='0' cellspacing='0' border='0' style='width: 100%; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;'> \n"
				+ " 		<tr> \n"
				+ " 		  <th   style='padding: 8px; font-size: 0.69em; text-align: center; background-color: #dbdbdb; border-bottom: 1px solid #ddd;'>내용</th>  \n"
				+ " 		</tr> \n"
				+ " 		<tr> \n"
				+ "		  <td> ERROR-CONTENT\n"
				+ " 		</tr> \n"
				+ " 	  </table> \n"
				+ " 	</td> \n"
				+ "  </tr> \n"
				+ " </table> \n"
				+ "   </body> \n"
				+ " </html> ";

				content = content.replace("ERROR-CONTENT", env + " 환경에서 ( " + timestamp.toString() + "  ) 에 </td> </tr><tr> <td style='color:red'>" + scriptEntity.getComment()+ "</td> </tr> <tr> <td>점검항목 이상유무 발생 확인 </td>");

				MailSender mailSender = new MailSender();
				// @20230707 임시로 메일 발송만 막음ㄴ
				mailSender.sendTextMail(MailEndpoint, "[점검]한화컨버젼스 BATCH 점검(자동점검) "+ timestamp.toString(), sender, receivers,
						content);
				log.info("알람 메일 전송");



				getDatacatAgentService().insertScriptResult(
						new ExecutionLogEntity(1, result.length() > 1 ? "toolong" : result, timestamp, scriptId));
			} else {// 정상
				getDatacatAgentService().insertScriptResult(new ExecutionLogEntity(0, result, timestamp, scriptId));
				log.info("정상으로 알람 메일 미 전송");
			}

		} else {
			String lastExecStamp = String.valueOf(lastExcutionAt.getTime()); // 마지막 실행 시간
			Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
			// 스크립트 실행
			// timestamp = new Timestamp(System.currentTimeMillis());

			Calendar cal = Calendar.getInstance();
			cal.setTime(lastExecDate);
			cal.add(Calendar.MINUTE, scriptEntity.getRepeatInterval()); // 마지막 실행결과 시간 + 인터벌
			// if (cal.getTime().compareTo(timestamp) <= 0) { // 만약 최종시작일 + 인터벌이 현재 시각보다 클경우 (마지막 실행 2시 인터벌 120분 현재시각 4시
															// 30분이면 2시+120분 = 4시 이므로 실행 해야함)
				result = getDatacatAgentService().execShellScript(scriptCommand);

				log.info("실행결과 = {}", result);

				if (!result.equals("0") || result.length() > 1) {// 비정상, 결과가 0이 아닌것 전부비정상
					// 비정상시 메일 전송

					WsRecipient[] receivers = new WsRecipient[2];
					receivers[0] = new WsRecipient();
					receivers[0].setSeqID(1);
					receivers[0].setRecvType("TO");
					receivers[0].setRecvEmail("justwon323@hanwha.com");

					receivers[1] = new WsRecipient();
					receivers[1].setSeqID(1);
					receivers[1].setRecvType("TO");
					receivers[1].setRecvEmail("true84you@hanwha.com");
					String content = "<!DOCTYPE html> \n"
					+ " <html> \n"
					+ " <head> \n"
					+ "   <title>한화컨버젼스 점검항목 확인 </title> \n"
					+ " </head> \n"
					+ " <body style='font-family: Arial, Helvetica, sans-serif, text-align: center;'> \n"
					+ "   <table cellpadding='0' cellspacing='0' border='0' style='width: 970px; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; margin: auto'> \n"
					+ "   <tr> \n"
					+ "     <td> \n"
					+ " 	  <table cellpadding='0' cellspacing='0' border='0' style='width: 100%; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;'> \n"
					+ " 		<tr bgcolor=#272f39 > \n"
					+ " 		  <td style='padding: 1px; width:20%; text-align: left; font-weight: bold; border-bottom: 1px solid #ddd;'> \n"
					+ " 			<img width=150 src='https://heis2.hanwha.com/_nuxt/img/sitlogo.png'> \n"
					+ " 		  </td> \n"
					+ " 		  <td style='padding: 1px; width:55%; text-align: center; font-size: 15px; font-weight: bold; border-bottom: 1px solid #ddd; color: #FFFFFF'>ITRM BATCH REPORT </td> \n"
					+ " 		  <td style='padding: 1px; width:25%;  text-align: left; font-size: 11px; font-weight: bold; border-bottom: 1px solid #ddd;color: #FFFFFF'>점검자: SYSTEM<br>점검일시: "+ timestamp.toString() +" <br> 점검일시: "+ utctimestamp +" (UTC+9) </td> \n"
					+ " 		</tr> \n"
					+ " 		<tr> \n"
					+ " 		  <td></td> \n"
					+ " 		</tr> \n"
					+ " 	  </table>  \n"
					+ " 	</td> \n"
					+ "   </tr> \n"
					+ "   <tr> \n"
					+ "     <td> \n"
					+ "		&nbsp;\n"
					+ " 	</td> \n"
					+ "   </tr> \n"
					+ "   <tr> \n"
					+ "     <td> \n"
					+ " 	  <h2 style='font-size: 18px; margin-top: 20px;text-decoration: underline;'>확인 항목</h2> \n"
					+ " 	  <table cellpadding='0' cellspacing='0' border='0' style='width: 100%; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;'> \n"
					+ " 		<tr> \n"
					+ " 		  <th   style='padding: 8px; font-size: 0.69em; text-align: center; background-color: #dbdbdb; border-bottom: 1px solid #ddd;'>내용</th>  \n"
					+ " 		</tr> \n"
					+ " 		<tr> \n"
					+ "		  <td> ERROR-CONTENT\n"
					+ " 		</tr> \n"
					+ " 	  </table> \n"
					+ " 	</td> \n"
					+ "  </tr> \n"
					+ " </table> \n"
					+ "   </body> \n"
					+ " </html> ";

					content = content.replace("ERROR-CONTENT", env + " 환경에서 ( " + timestamp.toString() + "  ) 에 </td> </tr><tr> <td style='color:red'>" + scriptEntity.getComment()+ "</td> </tr> <tr> <td>점검항목 이상유무 발생 확인 </td>");
					MailSender mailSender = new MailSender();
					// @20230707 임시로 메일 발송만 막음ㄴ
					mailSender.sendTextMail(MailEndpoint, "[점검]한화컨버젼스 BATCH 점검(자동점검) "+ timestamp.toString(), sender, receivers,
							content);
					log.info("알람 메일 전송");
					getDatacatAgentService().insertScriptResult(
							new ExecutionLogEntity(1, result.length() > 1 ? "toolong" : result, timestamp, scriptId));
				} else {// 정상
					getDatacatAgentService().insertScriptResult(new ExecutionLogEntity(0, result, timestamp, scriptId));
					log.info("정상으로 알람 메일 미 전송");
				}
			// }
		}
	}
}
