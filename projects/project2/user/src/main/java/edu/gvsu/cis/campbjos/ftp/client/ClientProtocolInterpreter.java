package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.common.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.common.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.common.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.Converter.convertToServerPortNumber;
import static java.lang.String.format;

public final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private Socket piSocket;

    public ClientProtocolInterpreter() {
        piSocket = null;
    }

    public void connect(final String ipAddress, final String serverPort)
            throws IOException {
        final int port = convertToServerPortNumber(serverPort);
        try {
            piSocket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket " +
                    "%s:%s", ipAddress, serverPort));
        }
    }

    private void sendCommandToServerControl(final String command)
            throws IOException {
        try {
            ControlWriter.write(piSocket.getOutputStream(), command);
        } catch (IOException e) {
            throw new IOException(format("Error writing \"%s\" to " +
                    "server", command));
        }
    }

    public boolean isConnected() {
        return !(piSocket == null || piSocket.isClosed());
    }

    @Override
    public String list() throws IOException, RuntimeException {
        sendCommandToServerControl(LIST);
        final DataTransferProcess clientDtp = new ClientDtp(
                piSocket.getInetAddress(),
                readOpenDataPort());
        final String list = clientDtp.listenForCharacterStream();
        clientDtp.closeSocket();

        return list;
    }

    @Override
    public void retrieve(final String filename) throws IOException,
            NullPointerException {
        sendCommandToServerControl(format("%s %s", RETR, filename));
        final DataTransferProcess clientDtp = new ClientDtp(piSocket.getInetAddress(), readOpenDataPort());
        clientDtp.listenForByteStream(filename);
        clientDtp.closeSocket();
    }

    @Override
    public void store(final String filename) throws IOException {
        sendCommandToServerControl(format("%s %s", STOR, filename));
        final DataTransferProcess clientDtp =
                new ClientDtp(piSocket.getInetAddress(), readOpenDataPort());
        clientDtp.sendByteStream(filename);
        clientDtp.closeSocket();
    }


    private int readOpenDataPort() throws IOException, NumberFormatException {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(piSocket.getInputStream()));
        String portLine = bufferedReader.readLine();
        return Integer.valueOf(portLine);
    }

    @Override
    public void quit() throws IOException {
        sendCommandToServerControl(QUIT);
        try {
            piSocket.close();
        } catch (IOException e) {
            // It's closed
        }
    }
}