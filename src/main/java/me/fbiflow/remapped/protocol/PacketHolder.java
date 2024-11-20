package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public interface PacketHolder {

    Map<Socket, List<Packet>> getReceivedPackets();

}
