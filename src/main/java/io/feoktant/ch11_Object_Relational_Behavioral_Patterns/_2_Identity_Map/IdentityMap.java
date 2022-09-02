package io.feoktant.ch11_Object_Relational_Behavioral_Patterns._2_Identity_Map;

import io.feoktant.domain.Person;

import java.util.HashMap;
import java.util.Map;

public class IdentityMap {
    private Map<Long, Person> people = new HashMap<>();

//    public static void addPerson(Person arg) {
//        /*soleInstance.*/people.put(arg.getID(), arg);
//    }
//    public static Person getPerson(Long key) {
//        return /*(Person) soleInstance.*/people.get(key);
//    }
//
//    public static Person getPerson(long key) {
//        return getPerson(Long.valueOf(key));
//    }
}
