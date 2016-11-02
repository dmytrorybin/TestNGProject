package ua.app.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Dmytro_Rybin on 10/31/2016.
 */
public  class ModifyList {

    public static void modify(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
        modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
    }
}
