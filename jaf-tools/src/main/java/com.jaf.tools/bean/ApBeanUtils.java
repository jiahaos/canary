package com.jaf.tools.bean;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

public final class ApBeanUtils {

    private static Mapper mapper = new DozerBeanMapper();

    private ApBeanUtils() {
    }

    public static <T> T map(Object object, Class<T> tClass) {
        if (object == null) {
            return null;
        }
        return mapper.map(object, tClass);
    }

    public static <T> List<T> mapList(List list, Class<T> tClass) {
        if (list == null || list.isEmpty()) {
            return Lists.newArrayList();
        }
        return (List<T>) list.stream().map(p -> map(p, tClass)).collect(Collectors.toList());
    }
    /*
     *  new APIs
     *  （1）引入泛型，接口更加通用
     *  （2）修改方法名称
     */
    public static <S, T> S fillObjectAttrs(S object, Map<String, T> dataMap) {
        try {
            fillObjectFields(dataMap, object);
            return object;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ApBeanException("Class is " + object.getClass().getName() + ": ", e);
        }
    }

    private static <T> void fillObjectFields(Map<String, T> dataMap, Object obj) throws InvocationTargetException, IllegalAccessException {
        Iterator<Map.Entry<String, T>> iter = dataMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, T> elements = iter.next();
            String key = elements.getKey();
            if (elements.getValue() == null) {
                continue;
            }
            T value = elements.getValue();
            BeanUtils.setProperty(obj, key, value);
        }
    }

    public static <S, T> T copyNotNull(T target, S source) {
        return (T) copyFieldsFromAToBNotNull(target, source);
    }

    /**
     * 复制source中的非空属性到target中，覆盖target中原有的属性；
     */
    public static Object copyFieldsFromAToBNotNull(Object target, Object source) {
        Map<String, Object> sourceAttrs = getObjectField(source);
        Iterator<Map.Entry<String, Object>> iter = sourceAttrs.entrySet().iterator();
        try {
            while (iter.hasNext()) {
                Map.Entry<String, Object> elements = iter.next();
                String key = elements.getKey();
                Object value = elements.getValue();
                if (value == null) {
                    continue;
                }
                BeanUtils.setProperty(target, key, value);
            }
            return target;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ApBeanException("Object is " + target + " and " + source + " : ", e);
        }
    }

    public static Map<String, Object> getObjectField(Object object) {
        if (object == null) {
            return null;
        }
        try {
            Map<String, Object> propertiesMap = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);
                    propertiesMap.put(key, value);
                }
            }
            return propertiesMap;
        } catch (Exception e) {
            throw new ApBeanException("Object is " + object + ": ", e);
        }
    }


    public static Object copyFieldsFromCamelAToBNotNull(Object target, Object source) {
        Map<String, Object> sourceAttrs = getObjectField(source);
        Iterator<Map.Entry<String, Object>> iter = sourceAttrs.entrySet().iterator();
        try {
            while (iter.hasNext()) {
                Map.Entry<String, Object> elements = iter.next();
                String key = elements.getKey();
                Object value = elements.getValue();
                if (value == null) {
                    continue;
                }
                BeanUtils.setProperty(target, underScore2Camel(key), value);
            }
            return target;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ApBeanException("Object is " + target + " and " + source + " : ", e);
        }
    }

    public static Object copyFieldsFromAToCamelBNotNull(Object target, Object source) {
        Map<String, Object> sourceAttrs = getObjectField(source);
        Iterator<Map.Entry<String, Object>> iter = sourceAttrs.entrySet().iterator();
        try {
            while (iter.hasNext()) {
                Map.Entry<String, Object> elements = iter.next();
                String key = elements.getKey();
                Object value = elements.getValue();
                if (value == null) {
                    continue;
                }
                BeanUtils.setProperty(target, camel2UnderScore(key), value);
            }
            return target;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ApBeanException("Object is " + target + " and " + source + " : ", e);
        }
    }

    private static String underScore2Camel(String key) {
        return LOWER_UNDERSCORE.to(LOWER_CAMEL, key);
    }

    private static String camel2UnderScore(String key) {
        return LOWER_CAMEL.to(LOWER_UNDERSCORE, key);
    }

    public static Map<String, Object> deepToMap(Object bean) throws ApBeanException {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            putValues(bean, map, null);
        } catch (IllegalAccessException e) {
            throw new ApBeanException("deepToMap", e);
        }
        return map;
    }

    private static void putValues(Object bean, Map<String, Object> map, String prefix) throws IllegalAccessException {
        Class<?> cls = bean.getClass();

        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(bean);
            String key;
            if (prefix == null) {
                key = field.getName();
            } else {
                key = prefix + "-" + field.getName();
            }

            if (isValue(value)) {
                map.put(key, value);
            } else {
                putValues(value, map, key);
            }
        }
    }

    private static final Set<Class<?>> valueClasses = (
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                    Object.class, String.class, Boolean.class,
                    Character.class, Byte.class, Short.class,
                    Integer.class, Long.class, Float.class,
                    Double.class
            )))
    );

    private static boolean isValue(Object value) {
        return value == null || valueClasses.contains(value.getClass());
    }

}
