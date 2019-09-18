package com.flavio.generalcomparator;

import com.flavio.generalcomparator.exception.ComparisonException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

public class GeneralComparator {

    public static Boolean compare(Object obj1, Object obj2){
        try{
            if(Collection.class.isAssignableFrom(obj1.getClass())
                    && Collection.class.isAssignableFrom(obj2.getClass())){
                compareCollection(obj1, obj2);
            } else if(Map.class.isAssignableFrom(obj1.getClass())
                    && Map.class.isAssignableFrom(obj2.getClass())){
                compareMap(obj1, obj2);
            } else if(obj1 instanceof Object[]
                    && obj2 instanceof Object[]){
                compareObjectArray(obj1, obj2);
            } else if(obj1 instanceof Number
                    && obj2 instanceof  Number){
                compareNumber(obj1, obj2);
            } else if(obj1 instanceof String
                    && obj2 instanceof String){
                compareString(obj1, obj2);
            } else if(obj1 instanceof Method
                    && obj2 instanceof Method){
                compareMethod(obj1, obj2);
            } else if(obj1 instanceof Field
                    && obj2 instanceof Field){
                compareField(obj1, obj2);
            } else {
                compareGenericObject(obj1, obj2);
            }
            return true;

        } catch (ComparisonException e){
            return false;
        }
    }

    private static void compareCollection(Object obj1, Object obj2){
        var a = (Collection)obj1;
        var b = (Collection)obj2;
        if(a.size() != b.size()){
            throw new ComparisonException();
        }
        IntStream
                .range(0, a.size())
                .parallel()
                .forEach( i -> {
                    if(! compare(a.toArray()[i], b.toArray()[i])){
                        throw new ComparisonException();
                    }
                });
    }

    private static void compareMap(Object obj1, Object obj2){
        var a = (Map) obj1;
        var b = (Map) obj2;
        if(a.size() != b.size()){
            throw new ComparisonException();
        }
        a.forEach((key, value) -> {
            if(!b.containsKey(key) || !compare(value, b.get(key))){
                throw new ComparisonException();
            }
        });
    }

    private static void compareObjectArray(Object obj1, Object obj2){
        var a = (Object[])obj1;
        var b = (Object[])obj2;
        if(a.length != b.length){
            throw new ComparisonException();
        }
        IntStream
                .range(0, a.length)
                .parallel()
                .forEach( i -> {
                    if(!compare(a[i], b[i])){
                        throw new ComparisonException();
                    }
                });
    }

    private static  void compareNumber(Object obj1, Object obj2){
        var a = (Number) obj1;
        var b = (Number) obj2;
        if(!a.equals(b)){
            throw new ComparisonException();
        }
    }

    private static void compareString(Object obj1, Object obj2){
        if(!obj1.equals(obj2)){
            throw new ComparisonException();
        }
    }

    private static void compareMethod(Object obj1, Object obj2){
        var a = (Method) obj1;
        var b = (Method) obj2;
        if(!a.getName().equals(b.getName())){
            throw new ComparisonException();
        }
    }

    private static void compareField(Object obj1, Object obj2){
        var a = (Field) obj1;
        var b = (Field) obj2;
        if(!a.getName().equals(b.getName())){
            throw new ComparisonException();
        }
    }

    private static void compareGenericObject(Object obj1, Object obj2){
        if(!compare(obj1.getClass().getDeclaredMethods(), obj2.getClass().getDeclaredMethods())){
            throw new ComparisonException();
        }
        var fieldsA = obj1.getClass().getDeclaredFields();
        var fieldsB = obj2.getClass().getDeclaredFields();
        if(!compare(fieldsA, fieldsB)){
            throw new ComparisonException();
        }
        IntStream
                .range(0, fieldsA.length)
                .parallel()
                .forEach( i -> {
                    var fieldA = fieldsA[i];
                    var fieldB = fieldsB[i];
                    fieldA.setAccessible(true);
                    fieldB.setAccessible(true);
                    try {
                        if(!compare(fieldA.get(obj1), fieldB.get(obj2))){
                            throw new ComparisonException();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }
}
