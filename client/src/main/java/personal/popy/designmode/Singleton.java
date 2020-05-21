package personal.popy.designmode;

public class Singleton {
    private volatile static Singleton lazySingleton;//双重加锁单利模式
    private Singleton(){}

    /**
     * 　　双重检查加锁
     * @return
     */
    public static Singleton getLazySingleton(){
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if(lazySingleton == null){
            //同步块，线程安全的创建实例
            synchronized (Singleton.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if(lazySingleton == null){
                    lazySingleton = new Singleton();
                }
            }
        }
        return lazySingleton;
    }


    /**
     * 2.Lazy initialization holder class模式
     */
    private static class SingletonHolder{
        static Singleton singleton = new Singleton();
    }

    public static Singleton getHolderInstance(){
        return SingletonHolder.singleton;
    }

    /**
     * enum实现
     */

}
