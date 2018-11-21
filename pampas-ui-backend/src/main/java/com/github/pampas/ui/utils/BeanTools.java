package com.github.pampas.ui.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工具类
 * 支持浅拷贝 和 深拷贝（使用了缓存优化，目前性能差距10-15倍左右）
 * 如果bean中没有嵌套bean则两者之间性能差距极小，
 * 建议多使用deepcopy，因为属性类型错误的时候会抛出异常，而浅拷贝则不会，容易导致数据意外错误
 * Created by darrenfu on 17-2-18.
 */
@SuppressWarnings("unused")
@Slf4j
public class BeanTools {
    /**
     * bean copier 缓存
     */
    private static ConcurrentHashMap<BeanCopierNode, BeanCopier> beanCopierCacheMap = new ConcurrentHashMap<>(32);


    /**
     * 实例化一个bean，懒得new再换行
     *
     * @param <T>         bean type
     * @param targetClass target bean
     * @return bean instance
     */
    @SuppressWarnings("unused")
    public static <T> T instance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * copy properties to other bean，方便一点是一点
     * 使用cglib beanCopier 性能较spring 的beanUtils 高5倍以上
     *
     * @param <T>         bean type
     * @param source      source bean
     * @param targetClass target class
     * @return target bean instance
     */
    public static <T> T copyBean(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass);
        T target = instance(targetClass);
        // copy bean
        beanCopier.copy(source, target, null);
        return target;
    }

    /**
     * 深复制 性能与浅复制差距10倍左右
     *
     * @param <T>         the type parameter
     * @param source      the source
     * @param targetClass the target class
     * @return the t
     */
    public static <T> T copyBeanDeeply(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass, true);
        T target = instance(targetClass);
        // copy bean
        return target;
    }


    /**
     * copy properties and generate list, 方便两点是两点
     *
     * @param <T>           the type parameter
     * @param <E>           the type parameter
     * @param sourceObjList the source obj list
     * @param targetClass   the target class
     * @return the list
     */
    public static <T, E> List<T> copyBeans(List<E> sourceObjList, Class<T> targetClass) {
        if (sourceObjList == null || sourceObjList.size() < 1) {
            return Collections.EMPTY_LIST;
        }

        List<T> targetObjList = new ArrayList<>(sourceObjList.size());
        // 生成copier 循环中复用
        BeanCopier beanCopier = getBeanCopier(sourceObjList.get(0).getClass(), targetClass);

        for (E source : sourceObjList) {
            T target = instance(targetClass);
            // copy bean
            beanCopier.copy(source, target, null);
            targetObjList.add(target);
        }
        return targetObjList;
    }

    /**
     * 深复制 对象列表
     *
     * @param <T>           the type parameter
     * @param <E>           the type parameter
     * @param sourceObjList the source obj list
     * @param targetClass   the target class
     * @return the list
     */
    public static <T, E> List<T> copyBeansDeeply(List<E> sourceObjList, Class<T> targetClass) {
        if (sourceObjList == null || sourceObjList.size() < 1) {
            return Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;

    }


    /**
     * 默认浅拷贝
     * 从缓存中获取 beanCopier, 性能提升3-4倍
     * 提升幅度优于使用HashBasedTable的性能提升
     *
     * @param sourceClz the source clz
     * @param targetClz the target clz
     * @return the bean copier
     */
    public static BeanCopier getBeanCopier(Class sourceClz, Class targetClz) {

        return getBeanCopier(sourceClz, targetClz, false);
    }

    /**
     * 从缓存中获取 beanCopier
     *
     * @param sourceClz    the source clz
     * @param targetClz    the target clz
     * @param useConverter the use converter 使用转换器则表明是深拷贝
     * @return the bean copier
     */
    public static BeanCopier getBeanCopier(Class sourceClz, Class targetClz, boolean useConverter) {
        BeanCopierNode copierNode = new BeanCopierNode(sourceClz, targetClz, useConverter);
        BeanCopier beanCopier = beanCopierCacheMap.get(copierNode);

        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClz, targetClz, useConverter);
            beanCopierCacheMap.putIfAbsent(copierNode, beanCopier);
        }
        return beanCopier;
    }


    /**
     * The type Bean copier node.
     */
    static class BeanCopierNode {

        @Getter
        private Class sourceClass;

        @Getter
        private Class targetClass;

        @Getter
        private boolean useConverter;

        /**
         * Instantiates a new Bean copier node.
         *
         * @param sourceClass the source class
         * @param targetClass the target class
         */
        BeanCopierNode(Class sourceClass, Class targetClass) {
            this(sourceClass, targetClass, false);
        }

        /**
         * Instantiates a new Bean copier node.
         *
         * @param sourceClass  the source class
         * @param targetClass  the target class
         * @param useConverter the use converter
         */
        BeanCopierNode(Class sourceClass, Class targetClass, boolean useConverter) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.useConverter = useConverter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BeanCopierNode that = (BeanCopierNode) o;
            return Objects.equals(sourceClass, that.sourceClass) &&
                    Objects.equals(targetClass, that.targetClass) &&
                    Objects.equals(useConverter, that.useConverter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceClass, targetClass, useConverter);
        }
    }

}
