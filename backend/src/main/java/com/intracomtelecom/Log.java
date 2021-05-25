package com.intracomtelecom;

import lombok.Data;

@Data
public class Log {
    private String recordDate;
    private String OSS;
    private String eNodeB;
    private String cell;
    private String carrier;
    private String cellAvailability;
    private String CQI;
    private String downlinkCellThroughput;
    private String downlinkPacketLossRate;
    private String downlinkUserThroughput;
    private String drops;
    private String estabFailures;
    private String accessibility;
    private String retainability;
    private String uplinkCellThroughput;
    private String uplinkPacketLossRate;
    private String uplinkUserThroughput;
    private String VoLTECallAttempts;
    private String latitude;
    private String longitude;
    private String azimuth;
    private String parentNetwork;
    private Long ID;
}
