package Aop;

import java.lang.reflect.Proxy;

public class JdkProxyDemo {
    interface Foo{
        void foo();
    }

    static class Target implements Foo{
        public void foo(){
            System.out.println("target foo");
        }
    }

    //jdk只能针对接口代理 cglib目标没有实现接口也可以代理
    public static void main(String[] args) {
        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader(); //需要一个classLoader来作为形参,用来加载在运行期间动态生成的字节码
        Foo fooProxy =(Foo) Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, (proxy, method, args1) -> {
            System.out.println("before");
            return null;
        });

        fooProxy.foo();
    }
}
