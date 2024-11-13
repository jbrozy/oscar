package org.injectables;

import com.illposed.osc.*;
import com.illposed.osc.transport.udp.OSCPortIn;
import jakarta.annotation.PostConstruct;
import org.exceptions.OSCException;
import org.routing.OSCRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.List;

@Service
public class OSCListener implements Runnable{
    private OSCPortIn oscPortIn;
    private final OSCRouter _oscRouter;
    private Thread listenerThread;

    private static final int PORT = 9001;

    @Autowired
    public OSCListener(OSCRouter _oscRouter) {
        this._oscRouter = _oscRouter;
    }

    @PostConstruct
    public void init(){
        try {
            oscPortIn = new OSCPortIn(new InetSocketAddress(PORT));
            oscPortIn.addPacketListener(new OSCPacketListener() {
                @Override
                public void handlePacket(OSCPacketEvent event) {
                    if (event.getPacket() instanceof OSCMessage message) {
                        String address = message.getAddress();
                        List<Object> arguments = message.getArguments();

                        // Log or process the message
                        System.out.println("Received OSC message on address: " + address);
                        System.out.println("Arguments: " + arguments);

                        try {
                            _oscRouter.navigate(address, arguments.toArray());
                        } catch (OSCException | InvocationTargetException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void handleBadData(OSCBadDataEvent badDataEvent) {
                    System.err.println("Received bad OSC data: " + badDataEvent.getException().getMessage());
                }
            });
            listenerThread = new Thread(this, "OSCListenerThread");
            listenerThread.setDaemon(true); // Run as a daemon thread
            listenerThread.start();
        } catch(Exception ignored) {
        }
    }

    @Override
    public void run() {
        try {
            oscPortIn.startListening();
            System.out.println("OSCListener started and listening on port " + PORT);
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            System.err.println("OSCListener interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new RuntimeException("Error in OSCListener", e);
        }
    }
}
