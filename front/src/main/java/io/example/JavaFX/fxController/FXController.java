package io.example.JavaFX.fxController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.Application;
import io.example.JavaFX.SharedData;
import io.example.WebSocketClient;
import java.util.HashMap;
import java.util.Map;

public abstract class FXController {
    protected static final SharedData SHARED_DATA = SharedData.getInstance();
    protected static final WebSocketClient WEBSOCKET_CLIENT = Application.getWebsocketClient();
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public abstract void updateInterface(String message);

//    public abstract void changeScene(String type) throws IOException;
    public void sendMsgToRemoteServer(String key, Object value) {
        Map<String, Object> message = new HashMap<>();

        message.put(key, value);

        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        WEBSOCKET_CLIENT.sendMessage(json);
    }

    public void sendMsgToRemoteServer(Map<String, Object> message) {

        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        WEBSOCKET_CLIENT.sendMessage(json);
    }
}
