package com.mastere_project.vacances_tranquilles.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class ConversationWithMessagesDTOTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        ConversationDTO conv = new ConversationDTO();
        MessageDTO msg = new MessageDTO();
        ConversationWithMessagesDTO c1 = new ConversationWithMessagesDTO(conv, Arrays.asList(msg));
        
        assertEquals(conv, c1.getConversation());
        assertEquals(1, c1.getMessages().size());

        ConversationWithMessagesDTO c2 = new ConversationWithMessagesDTO(conv, Arrays.asList(msg));
        
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertTrue(c1.toString().contains("ConversationDTO"));
    }
} 