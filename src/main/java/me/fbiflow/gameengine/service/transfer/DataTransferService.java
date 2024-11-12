package me.fbiflow.gameengine.service.transfer;

public class DataTransferService {

    private DataSender dataSender;
    private DataReceiver dataReceiver;

    public DataTransferService(DataSender dataSender, DataReceiver dataReceiver) {
        this.dataSender = dataSender;
        this.dataReceiver = dataReceiver;
    }

    public void send(){}

    public void receive(){}

}