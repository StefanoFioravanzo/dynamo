package dynamo.messages;

import java.io.Serializable;

public class GetMessage implements Serializable {
    public int key;

    public GetMessage(int key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GetMessage{" +
                "key=" + key +
                '}';
    }
}
