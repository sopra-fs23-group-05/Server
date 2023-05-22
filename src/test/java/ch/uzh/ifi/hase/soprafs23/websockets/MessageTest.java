package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {

    @Test
    void gettersAndSetters_validInput() {

        Message message = new Message(123456, 1, "Test message", MessageType.DESCRIPTION);

        assertEquals(123456, message.getAccessCode());
        assertEquals(1, message.getSenderId());
        assertEquals("Test message", message.getContent());
        assertEquals(MessageType.DESCRIPTION, message.getType());

        message.setAccessCode(123455);
        message.setSenderId(2L);
        message.setContent("test");
        message.setType(MessageType.GUESS);

        assertEquals(123455, message.getAccessCode());
        assertEquals(2, message.getSenderId());
        assertEquals("test", message.getContent());
        assertEquals(MessageType.GUESS, message.getType());
    }
}