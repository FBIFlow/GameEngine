package me.fbiflow.gameengine.protocol;

import me.fbiflow.gameengine.protocol.packet.Packet;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public interface PacketHolder {

    Map<Socket, List<Packet>> getReceivedPackets();

}
