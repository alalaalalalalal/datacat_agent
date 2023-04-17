package com.main.datacat_agent.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxDbConnector {  //연결용 클래스 입니다.
	
    private final String DB_URL = "http://DEV-Influx-Connect-IMSI-ba7f50050f9bbe38.elb.us-east-1.amazonaws.com:8086";
    private final String DB_ID = "admin";
    private final String DB_PWD = "admin";

    public void queryData(String q){
        InfluxDB influxDB = InfluxDBFactory.connect(DB_URL, DB_ID, DB_PWD);
        
        influxDB.setDatabase("isl");
        QueryResult queryResult = influxDB.query(new Query(q));
        System.out.println(queryResult);
    }

}
