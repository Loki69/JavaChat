package com.mycompany.client;

import java.io.IOException;

public interface Chat {
    public String read() throws IOException;
    public void write(String message) throws IOException;
}
