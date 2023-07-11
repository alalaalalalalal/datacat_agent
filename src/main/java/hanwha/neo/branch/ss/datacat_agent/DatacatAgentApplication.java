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

import hanwha.neo.branch.ss.datacat_agent.entity.ExecutionLogEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageMailEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.ScriptEntity;
import hanwha.neo.branch.ss.datacat_agent.service.DatacatAgentService;
import hanwha.neo.branch.ss.datacat_agent.service.DatacatAgentServiceImpl;
import hanwha.neo.branch.ss.mail.service.MailSender;
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
		if (args == null || args.length == 0) {
			log.info("인자 전달이 필요 합니다. ex: dev-us (미국 개발계)");
		} else {
			log.info("인자 확인" + args[0]);
			while (true) {
				List<ScriptEntity> scriptList = getDatacatAgentService().readScript(args[0]);
				for (ScriptEntity scriptEntity : scriptList) {
					if (scriptEntity != null) {
						log.info("스크립트 리드 성공");
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						String hour = sdf.format(timestamp).replace(":", "");
						String starTtime = scriptEntity.getStartTime().toString().replace(":", "");
						String endTime = scriptEntity.getEndTime().toString().replace(":", "");
						// if("09".equals(hour) || "9".equals(hour)){ // 9시 발송
							sendItrm();
						// }

						if (Integer.valueOf(hour) >= Integer.valueOf(starTtime)
								&& Integer.valueOf(hour) <= Integer.valueOf(endTime)) { // 언제부터 언제까지 실행해야 하는지 체크
							log.info("스크립트 실행 성공");
							log.info(hour + " / " + starTtime + " / " + endTime);
							log.info("id : " + scriptEntity.getPid());
							executeK8s(scriptEntity, args[0]);
						} else {
							log.info("스크립트 시간 체크로 제외됨");
						}
					}
				}

				
				Thread.sleep(1000 * 60 * 5); // 5분에 한번씩 체크
			}
		}
	}

	public void sendItrm() throws RemoteException  {
		List<MessageMailEntity> messageMailList =  getDatacatAgentService().selectItrmMail("0");
		MessageMailEntity messageMailEntity = messageMailList.get(0);
		WsRecipient[] receivers = new WsRecipient[1];
		receivers[0] = new WsRecipient();
		receivers[0].setSeqID(1);
		receivers[0].setRecvType("TO");
		receivers[0].setRecvEmail("justwon323@hanwha.com");
		String content = messageMailEntity.getMailContents();
		MailSender mailSender = new MailSender();
		//@20230707 임시로 메일 발송만 막음ㄴ
		mailSender.sendTextMail(MailEndpoint, messageMailEntity.getMailSubject(), sender, receivers,
				content);
		getDatacatAgentService().updateMailstatus(messageMailEntity.getSeq()); // 메일 발송 후 상태 업데이트
	}
	public void executeK8s(ScriptEntity scriptEntity, String env) throws RemoteException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		// 현재시간 구함
		String[] scriptCommand = { "/bin/sh", "-c", scriptEntity.getCommand() };

		log.info("스크립트 log={}", scriptCommand[2]);
		int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();

		Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);
		StringBuilder scriptResult = new StringBuilder();
		String result = "";
		if (lastExcutionAt == null) { // 최초실행
			scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
			result = scriptResult.toString();
			// true 가 0 false 가 0 아닌것
			if (!result.equals("0") || result.length() > 1) {// 비정상
				getDatacatAgentService().insertScriptResult(
						new ExecutionLogEntity(1, result.length() > 1 ? "toolong" : result, timestamp, scriptId));
			} else {// 정상
				getDatacatAgentService().insertScriptResult(new ExecutionLogEntity(0, result, timestamp, scriptId));
			}
		} else {
			String lastExecStamp = String.valueOf(lastExcutionAt.getTime()); // 마지막 실행 시간
			Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
			// 스크립트 실행
			timestamp = new Timestamp(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastExecDate);
			cal.add(Calendar.MINUTE, scriptEntity.getRepeatInterval()); // 마지막 실행결과 시간 + 인터벌
			if (cal.getTime().compareTo(timestamp) <= 0) { // 만약 최종시작일 + 인터벌이 현재 시각보다 클경우 (마지막 실행 2시 인터벌 120분 현재시각 4시
															// 30분이면 2시+120분 = 4시 이므로 실행 해야함)
				scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
				result = scriptResult.toString();

				log.info("실행결과 = {}", result);

				if (!result.equals("0") || result.length() > 1) {// 비정상, 결과가 0이 아닌것 전부비정상
					// 비정상시 메일 전송

					WsRecipient[] receivers = new WsRecipient[1];
					receivers[0] = new WsRecipient();
					receivers[0].setSeqID(1);
					receivers[0].setRecvType("TO");
					receivers[0].setRecvEmail("justwon323@hanwha.com");
					String content = env + " 환경에서 <br>" + timestamp.toString() + " 에 <br>" + scriptEntity.getCommand()+ "<br> 실행시 비정상 값 검출됨";
					MailSender mailSender = new MailSender();
					//@20230707 임시로 메일 발송만 막음ㄴ
					// mailSender.sendTextMail(MailEndpoint, "확인요망 " + timestamp.toString(), sender, receivers,
					// 		content);

					getDatacatAgentService().insertScriptResult(
							new ExecutionLogEntity(1, result.length() > 1 ? "toolong" : result, timestamp, scriptId));
				} else {// 정상
					getDatacatAgentService().insertScriptResult(new ExecutionLogEntity(0, result, timestamp, scriptId));
				}
			}
		}
	}
}
