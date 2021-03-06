package edu.gvsu.cis.campbjos.imgine.client;

import edu.gvsu.cis.campbjos.imgine.common.ControlByteReader;
import edu.gvsu.cis.campbjos.imgine.common.ControlByteWriter;
import edu.gvsu.cis.campbjos.imgine.common.DataTransferProcess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

final class ClientDtp implements DataTransferProcess {

    private final Socket socket;

    ClientDtp(InetAddress ipAddress, int port) throws IOException {
        final String address = ipAddress.toString().replaceAll("/", "");
        this.socket = new Socket(address, port);
    }

    @Override
    public void sendByteStream(final String filename) {
        try {
            ControlByteWriter.sendFile(socket.getOutputStream(),
                    filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCharacterStream(String message) {
        // Unused in client DTP
    }

    @Override
    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }

    @Override
    public void listenForByteStream(final String filename) throws
            NullPointerException {
        try {
            ControlByteReader.readByteStream(socket.getInputStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                throw new NullPointerException(e.getMessage());
            }
        }
    }

}