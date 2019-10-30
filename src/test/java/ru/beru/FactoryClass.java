package ru.beru;

import org.testng.annotations.Factory;

public class FactoryClass {
    @Factory
    public Object[] invokeObjects()
    {
        Object[] obj = new Object[3];
        obj[0] = new SimpleTests();
        obj[1] = new Login();
        obj[2] = new Delivery();
        return obj;
    }
}
