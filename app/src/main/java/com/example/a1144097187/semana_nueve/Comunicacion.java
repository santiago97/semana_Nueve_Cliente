package com.example.a1144097187.semana_nueve;

import android.util.Log;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


import datos.Bola;

/**
 * Created by Santiago on 03/10/2016.
 */
public class Comunicacion extends java.util.Observable implements Runnable {

    private static final String TAG = "CommunicationManager";
    private static Comunicacion ref;


    public static final String MULTI_GROUP_ADDRESS = "224.3.3.3";
    // Default destination port
    public static  final int DEFAULT_PORT = 5000;

    private MulticastSocket ms;
    private DatagramSocket ds;
    private boolean running;
    private boolean connecting;
    private boolean reset;
    private boolean errorNotified;


    private Comunicacion(){

        running = true;
        connecting = true;
        reset = false;
        errorNotified = false;
        Log.d(TAG, "[ CommunicationManager Instance Built ]");

    }


    public static Comunicacion getInstance() {
        if (ref == null) {
            ref = new Comunicacion();
            Thread runner = new Thread(ref);
            runner.start();
        }

        return ref;
    }


    @Override
    public void run() {

        Log.d(TAG, "[ Communication Thread Started ]");
        while (running) {
            if (connecting) {
                if (reset) {
                    if (ms != null) {
                        ms.close();
                        Log.d(TAG, "[Communication was reset]");
                    }
                    reset = false;
                }
                connecting = !attemptConnection();
            } else {
                if (ms != null) {
                    DatagramPacket p = receiveMessage();

                    // Validate that there are no errors with the data
                    if (p != null) {
                        // Transform packet bytes to understandable data
                        String message = new String(p.getData(), 0, p.getLength());

                        // Notify the observers that new data has arrived and pass the data to them
                        setChanged();
                        notifyObservers(message);
                        clearChanged();
                    }
                }
            }
        }
        ms.close();

    }


    private boolean attemptConnection() {
        try {
            ms = new MulticastSocket();
            setChanged();
            notifyObservers("Connection started");
            clearChanged();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "[ Error starting Communication]");
            if (!errorNotified) {
                setChanged();
                notifyObservers("Connection failed");
                clearChanged();
                errorNotified = true;
            }
            return false;
        }
    }


    public void sendMessage(final Bola b, final String destAddress, final int destPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ms != null) {

                    try {
                        // Validate destAddress
                        InetAddress ia = InetAddress.getByName(destAddress);
                        byte[] data = serializar(b);

                        DatagramPacket packet = new DatagramPacket(data, data.length, ia, destPort);

                        System.out.println("Sending data to " + ia.getHostAddress() + ":" + destPort);
                        ms.send(packet);
                        System.out.println("Data was sent");

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                    setChanged();
                    notifyObservers("Not connected");
                    clearChanged();
                }
            }
        }).start();

    }

    private byte[] serializar(Object data) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            bytes = baos.toByteArray();

            // Close streams
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public DatagramPacket receiveMessage() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            ms.receive(packet);
            System.out.println("Data received from " + packet.getAddress() + ":" + packet.getPort());
            return packet;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}