package org.medusar.aeron.demo;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.aeron.driver.media.ReceiveChannelEndpoint;
import io.aeron.driver.media.ReceiveChannelEndpointThreadLocals;
import io.aeron.driver.media.SendChannelEndpoint;
import io.aeron.driver.media.UdpChannel;
import io.aeron.driver.status.SystemCounters;
import org.agrona.concurrent.CachedNanoClock;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.status.AtomicCounter;
import org.agrona.concurrent.status.CountersManager;

public class Dmeo {
    public static void main(String[] args) {
        //pub
        describe("aeron:udp?endpoint=5.6.7.8:9000|control=5.6.7.8:9001|control-mode=dynamic|reliable=true");

        //sub
        describe("aeron:udp?endpoint=5.6.7.8:9000|reliable=true");


        //pub
        describe("aeron:udp?endpoint=5.6.7.8:9000|reliable=true");

        //sub
        describe("aeron:udp?endpoint=10.10.1.100:8000|control=5.6.7.8:9001|control-mode=dynamic|reliable=true");


        AtomicCounter counter = new AtomicCounter(new UnsafeBuffer(new byte[512]), 1);
        MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.DEDICATED)
                .systemCounters(new SystemCounters(new CountersManager(new UnsafeBuffer(new byte[1024*1024*1024]),new UnsafeBuffer(new byte[1024*1024]))))
                .receiveChannelEndpointThreadLocals(new ReceiveChannelEndpointThreadLocals())
                .receiverCachedNanoClock(new CachedNanoClock());

        //bind 5.6.7.8.9001, endpoint: 5.6.7.8:9000 connect: null
//        UdpChannel udpChannel = UdpChannel.parse("aeron:udp?endpoint=5.6.7.8:9000|control=5.6.7.8:9001|control-mode=dynamic|reliable=true");
        //bind 0.0.0.0/0.0.0.0:0, connect: /5.6.7.8:9000, endpoint: /5.6.7.8:9000
//        UdpChannel udpChannel = UdpChannel.parse("aeron:udp?endpoint=5.6.7.8:9000|reliable=true");
//        SendChannelEndpoint endpoint = new SendChannelEndpoint(udpChannel, counter, ctx);
//        endpoint.openDatagramChannel(counter);

        //bind 10.10.1.100:8000, endpoint: /10.10.1.100:8000 connect: null
//        udpChannel = UdpChannel.parse("aeron:udp?endpoint=10.10.1.100:8000|control=5.6.7.8:9001|control-mode=dynamic|reliable=true");
        //bind /5.6.7.8:9000, connect: null, endpoint: /5.6.7.8:9000
        UdpChannel udpChannel = UdpChannel.parse("aeron:udp?endpoint=5.6.7.8:9000|reliable=true");
        ReceiveChannelEndpoint receiveChannelEndpoint = new ReceiveChannelEndpoint(udpChannel, null, counter, ctx);
        receiveChannelEndpoint.openDatagramChannel(counter);
    }

    private static void describe(String channelUri) {
        UdpChannel udpChannel = UdpChannel.parse(channelUri);
        System.out.println("uri:" + channelUri);
        System.out.println("multicast:" + udpChannel.isMulticast());
        System.out.println("remoteControl:" + udpChannel.remoteControl());
        System.out.println("remoteData:" + udpChannel.remoteData());
        System.out.println("localControl:" + udpChannel.localControl());
        System.out.println("localData:" + udpChannel.localData());

        System.out.println(udpChannel);
    }
}
