package com.main.datacat_agent.service;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

public class InfluxDbConnector {  //연결용 클래스 입니다.
	
    private final String DB_URL = "http://10.157.16.71:8086"; //서버용
    // private final String DB_URL = "http://DEV-Influx-Connect-IMSI-ba7f50050f9bbe38.elb.us-east-1.amazonaws.com:8086"; //로컬용
    private final String DB_ID = "admin";
    private final String DB_PWD = "admin";

    public String queryData(String q){
        InfluxDB influxDB = InfluxDBFactory.connect(DB_URL, DB_ID, DB_PWD);
        String resultValue = "";
        influxDB.setDatabase("isl");
        QueryResult queryResult = influxDB.query(new Query(q));
        List<Result> resultList = queryResult.getResults();
        for(Result result : resultList ){
            for(Series value : result.getSeries()){
                for(List object : value.getValues()){
                    resultValue = object.get(2).toString();
                }
            }
        }
       
        influxDB.close();
        return resultValue;
    }
}
