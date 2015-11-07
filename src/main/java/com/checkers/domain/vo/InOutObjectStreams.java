package com.checkers.domain.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Eugene on 07.11.2015.
 */
public class InOutObjectStreams  {

    private Socket white;
    private ObjectOutputStream outObjWhite;
    private ObjectInputStream inObjectWhite;

    public InOutObjectStreams(Socket white) throws IOException {
        this.white = white;
        this.outObjWhite = new ObjectOutputStream(white.getOutputStream());
        this.inObjectWhite = new ObjectInputStream(white.getInputStream());
    }

    public void setWhite(Socket white) {
        this.white = white;
    }

    public ObjectOutputStream getOutObjWhite() {
        return outObjWhite;
    }

    public ObjectInputStream getInObjectWhite() {
        return inObjectWhite;
    }

    public void writeObject(Field currentField) throws IOException {
        outObjWhite.writeObject(currentField);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return inObjectWhite.readObject();
    }

    public void close() throws IOException {
        inObjectWhite.close();
        outObjWhite.close();
    }

    public String waitForName() throws IOException, ClassNotFoundException {
        return (String) inObjectWhite.readObject();
    }
}
