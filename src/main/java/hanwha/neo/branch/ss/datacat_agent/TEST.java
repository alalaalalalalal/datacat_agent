package hanwha.neo.branch.ss.datacat_agent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TEST {

	public static void main(String[] args) throws IOException {
		 	File file = new File("./src/main/resources/error.txt");
			
			FileReader file_reader = new FileReader(file);
			int cur = 0;
			while((cur = file_reader.read()) != -1){
				System.out.print((char)cur);
			}
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// // System.out.println(sdf.format(timestamp));
		// Date today = new Date();
		// // System.out.println(today);
		// // 문자열
		// String dateStr = "2023년 05월 17일 18시 07분 00초";

		// // 포맷터
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분
		// ss초");

		// // 문자열 -> Date
		// Date date;
		// try {
		// date = formatter.parse(dateStr);
		// // Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
		// timestamp = new Timestamp(System.currentTimeMillis());
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(date);
		// cal.add(Calendar.MINUTE, 120); //마지막 실행결과 시간 + 인터벌
		// System.out.println(cal.getTime().toString());
		// System.out.println(timestamp);
		// if(cal.getTime().compareTo(timestamp)<=0){ // 20시가
		// System.out.println("지금 되면 안됨");
		// }else{
		// System.out.println("지금 되어야됨");
		// }
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		}
}
