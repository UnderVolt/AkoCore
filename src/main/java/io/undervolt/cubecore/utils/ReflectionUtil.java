package io.undervolt.cubecore.utils;

import java.lang.reflect.*;

import io.undervolt.cubecore.CubecoreSettings;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectionUtil {

    public static Object getField(Object instance, Class<?> clazz, String field, String obfuscatedField) {
        Field tmp = ObfuscationReflectionHelper.findField(clazz, CubecoreSettings.RELEASE ? obfuscatedField : field);
        try {
            return tmp.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String method, String obfuscatedMethod, Class<?> returnType, Class<?>... parameterTypes) {
        return ObfuscationReflectionHelper.findMethod(clazz, CubecoreSettings.RELEASE ? obfuscatedMethod : method, returnType, parameterTypes);
    }

}