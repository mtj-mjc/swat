package ch.mrnjec.swat.bus;

import java.io.IOException;

public interface Communication {
    String syncCall(String route, String message) throws IOException, InterruptedException;
}
