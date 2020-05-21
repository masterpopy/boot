package personal.popy.record.structure;

import java.util.concurrent.RecursiveTask;

public class ForkJoin extends RecursiveTask<Long> {

    //临界值
    public static final long CRITICAL = 100000L;

    //起始值
    private long start;
    //结束值
    private long end;

    public ForkJoin(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        //判断是否是拆分完毕
        if (end - start <= CRITICAL) {
            //如果拆分完毕就相加
            long sum = 0L;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            //没有拆分完毕就开始拆分
            //计算的两个值的中间值
            long middle = (end + start) / 2;
            ForkJoin right = new ForkJoin(start, middle);
            right.fork();//拆分，并压入线程队列
            ForkJoin left = new ForkJoin(middle + 1, end);
            left.fork();//拆分，并压入线程队列

            //合并
            return right.join() + left.join();
        }
    }
}
