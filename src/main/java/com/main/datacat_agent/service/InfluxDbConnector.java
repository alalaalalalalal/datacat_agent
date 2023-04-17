package com.main.datacat_agent.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class InfluxDbConnector {  //연결용 클래스 입니다.
	
    private static final String DB_URL = "http://DEV-Influx-Connect-IMSI-ba7f50050f9bbe38.elb.us-east-1.amazonaws.com:8086";
    private static final String DB_ID = "admin";
    private static final String DB_PWD = "admin";

    private static InfluxDbConnector inf;
    private InfluxDB db = null;
	
    private InfluxDbConnector() {	 }
	
    public static InfluxDbConnector getInstance() {
        synchronized(InfluxDbConnector.class) {
            if(inf == null) {
                inf = new InfluxDbConnector();
                inf.tryToConnecting();
            }
        }
        return inf;
    }
	
    private void tryToConnecting() {
        db = InfluxDBFactory.connect(DB_URL, DB_ID, DB_PWD);
    }
	
    public InfluxDB getDb() {
        try {
            if(db == null || db.ping() == null){
                synchronized(inf) {
                    db = InfluxDBFactory.connect(DB_URL, DB_ID, DB_PWD);
                }
            }			
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }
}