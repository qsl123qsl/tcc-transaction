package org.mengyun.tcctransaction.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by changming.xie on 2/23/17.
 */
public final class FactoryBuilder {


    private FactoryBuilder() {

    }

    /**
     * bean工厂集合
     */
    private static List<BeanFactory> beanFactories = new ArrayList<BeanFactory>();

    /**
     * 类与bean工厂的映射
     */
    private static ConcurrentHashMap<Class, SingeltonFactory> classFactoryMap = new ConcurrentHashMap<Class, SingeltonFactory>();

    /**
     * 获取指定的类单例工厂
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> SingeltonFactory<T> factoryOf(Class<T> clazz) {

        if (!classFactoryMap.containsKey(clazz)) {

            for (BeanFactory beanFactory : beanFactories) {
                if (beanFactory.isFactoryOf(clazz)) {
                    classFactoryMap.putIfAbsent(clazz, new SingeltonFactory<T>(clazz, beanFactory.getBean(clazz)));
                }
            }

            if (!classFactoryMap.containsKey(clazz)) {
                classFactoryMap.putIfAbsent(clazz, new SingeltonFactory<T>(clazz));
            }
        }

        return classFactoryMap.get(clazz);
    }

    public static void registerBeanFactory(BeanFactory beanFactory) {
        beanFactories.add(beanFactory);
    }

    /**
     * 单例工厂
     * @param <T>
     */
    public static class SingeltonFactory<T> {

        /**
         * 单例
         */
        private volatile T instance = null;

        /**
         * 类名
         */
        private String className;

        public SingeltonFactory(Class<T> clazz, T instance) {
            this.className = clazz.getName();
            this.instance = instance;
        }

        public SingeltonFactory(Class<T> clazz) {
            this.className = clazz.getName();
        }

        /**
         * 获得单例
         *
         * 经典的单例模式
         * @return
         */
        public T getInstance() {

            if (instance == null) {
                synchronized (SingeltonFactory.class) {
                    if (instance == null) {
                        try {
                            ClassLoader loader = Thread.currentThread().getContextClassLoader();

                            Class<?> clazz = loader.loadClass(className);

                            instance = (T) clazz.newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to create an instance of " + className, e);
                        }
                    }
                }
            }

            return instance;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            SingeltonFactory that = (SingeltonFactory) other;

            if (!className.equals(that.className)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return className.hashCode();
        }
    }
}