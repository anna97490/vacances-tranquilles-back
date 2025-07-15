package com.mastere_project.vacances_tranquilles.entity;

import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("test@mail.com");
        u1.setPassword("pass");
        u1.setFirstName("John");
        u1.setLastName("Doe");
        u1.setUserRole(UserRole.CLIENT);
        u1.setProfilePicture("pic");
        u1.setPhoneNumber("0123456789");
        u1.setAddress("1 rue de Paris");
        u1.setCity("Paris");
        u1.setPostalCode("75000");
        u1.setSiretSiren("123456789");
        u1.setCompanyName("MaBoite");
        u1.setConversationsAsUser1(new ArrayList<>());
        u1.setConversationsAsUser2(new ArrayList<>());
        u1.setMessagesSent(new ArrayList<>());

        assertEquals(1L, u1.getId());
        assertEquals("test@mail.com", u1.getEmail());
        assertEquals("pass", u1.getPassword());
        assertEquals("John", u1.getFirstName());
        assertEquals("Doe", u1.getLastName());
        assertEquals(UserRole.CLIENT, u1.getUserRole());
        assertEquals("pic", u1.getProfilePicture());
        assertEquals("0123456789", u1.getPhoneNumber());
        assertEquals("1 rue de Paris", u1.getAddress());
        assertEquals("Paris", u1.getCity());
        assertEquals("75000", u1.getPostalCode());
        assertEquals("123456789", u1.getSiretSiren());
        assertEquals("MaBoite", u1.getCompanyName());
        assertNotNull(u1.getConversationsAsUser1());
        assertNotNull(u1.getConversationsAsUser2());
        assertNotNull(u1.getMessagesSent());
        assertTrue(u1.toString().contains("User"));
        assertTrue(u1.toString().contains("John") || u1.toString().contains("Doe"));
    }

    @Test
    void testEqualsWithAllFields() {
        User u1 = new User();
        User u2 = new User();
        u1.setId(1L); u2.setId(1L);
        u1.setEmail("a@b.com"); u2.setEmail("a@b.com");
        u1.setPassword("pass"); u2.setPassword("pass");
        u1.setFirstName("John"); u2.setFirstName("John");
        u1.setLastName("Doe"); u2.setLastName("Doe");
        u1.setUserRole(UserRole.CLIENT); u2.setUserRole(UserRole.CLIENT);
        u1.setProfilePicture("pic"); u2.setProfilePicture("pic");
        u1.setPhoneNumber("0123456789"); u2.setPhoneNumber("0123456789");
        u1.setAddress("1 rue de Paris"); u2.setAddress("1 rue de Paris");
        u1.setCity("Paris"); u2.setCity("Paris");
        u1.setPostalCode("75000"); u2.setPostalCode("75000");
        u1.setSiretSiren("123456789"); u2.setSiretSiren("123456789");
        u1.setCompanyName("MaBoite"); u2.setCompanyName("MaBoite");
        u1.setConversationsAsUser1(new ArrayList<>()); u2.setConversationsAsUser1(new ArrayList<>());
        u1.setConversationsAsUser2(new ArrayList<>()); u2.setConversationsAsUser2(new ArrayList<>());
        u1.setMessagesSent(new ArrayList<>()); u2.setMessagesSent(new ArrayList<>());

        // Cas d'égalité parfaite
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertEquals(u1, u1); // self
        assertNotEquals(null, u1);
        assertNotEquals("string", u1);

        // Cas où une liste est différente
        u2.setMessagesSent(List.of(new Message()));
        assertNotEquals(u1, u2);
        u2.setMessagesSent(new ArrayList<>()); // reset

        // Cas où une liste est nulle dans l'un des objets
        u1.setMessagesSent(null);
        assertNotEquals(u1, u2);
        u1.setMessagesSent(new ArrayList<>()); // reset

        // Cas où un champ String est null dans l'un des objets
        u2.setProfilePicture(null);
        assertNotEquals(u1, u2);
        u2.setProfilePicture("pic"); // reset

        // Cas où un champ Enum est null dans l'un des objets
        u1.setUserRole(null);
        assertNotEquals(u1, u2);
        u1.setUserRole(UserRole.CLIENT); // reset

        // Cas où un champ String est différent
        u2.setFirstName("Jane");
        assertNotEquals(u1, u2);
    }

    @Test
    void testAllArgsConstructor() {
        List<Conversation> conv1 = new ArrayList<>();
        List<Conversation> conv2 = new ArrayList<>();
        List<Message> msgs = new ArrayList<>();
        User u = new User(1L, "pic", "John", "Doe", "a@b.com", "pass", UserRole.CLIENT, "0123456789", "1 rue de Paris", "Paris", "75000", "123456789", "MaBoite", conv1, conv2, msgs);
        
        assertEquals(1L, u.getId());
        assertEquals("pic", u.getProfilePicture());
        assertEquals("John", u.getFirstName());
        assertEquals("Doe", u.getLastName());
        assertEquals("a@b.com", u.getEmail());
        assertEquals("pass", u.getPassword());
        assertEquals(UserRole.CLIENT, u.getUserRole());
        assertEquals("0123456789", u.getPhoneNumber());
        assertEquals("1 rue de Paris", u.getAddress());
        assertEquals("Paris", u.getCity());
        assertEquals("75000", u.getPostalCode());
        assertEquals("123456789", u.getSiretSiren());
        assertEquals("MaBoite", u.getCompanyName());
        assertEquals(conv1, u.getConversationsAsUser1());
        assertEquals(conv2, u.getConversationsAsUser2());
        assertEquals(msgs, u.getMessagesSent());
    }

    @Test
    void testGetAllConversations() {
        User u = new User();
        Conversation c1 = new Conversation();
        Conversation c2 = new Conversation();
        u.setConversationsAsUser1(List.of(c1));
        u.setConversationsAsUser2(List.of(c2));
        List<Conversation> all = u.getAllConversations();
        
        assertEquals(2, all.size());
        assertTrue(all.contains(c1));
        assertTrue(all.contains(c2));
    }
} 
