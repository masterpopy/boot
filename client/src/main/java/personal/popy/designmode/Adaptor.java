package personal.popy.designmode;

/**
 * 对象适配器,也有通过继承进行类适配
 * 　　系统需要使用现有的类，而此类的接口不符合系统的需要。那么通过适配器模式就可以让这些功能得到更好的复用。
 * 　　在实现适配器功能的时候，可以调用自己开发的功能，从而自然地扩展系统的功能。
 * 缺点：
 * 过多的使用适配器，会让系统非常零乱，不易整体进行把握。比如，明明看到调用的是A接口，其实内部被适配成了B接口的实现，
 * 一个系统如果太多出现这种情况，无异于一场灾难。因此如果不是很有必要，可以不使用适配器，而是直接对系统进行重构。
 */
public class Adaptor {

    private Target target;

    public Adaptor(Target target){
        this.target = target;
    }

    public void method3(Object obj){
        target.method1(obj);
    }
}
