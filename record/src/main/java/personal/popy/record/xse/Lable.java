package personal.popy.record.xse;

public class Lable implements Value {
    String name;
    private static int cnt;
    private static StringBuilder sb = new StringBuilder();


    public Lable() {
        String lable_ = sb.append("lable_").append(cnt++).toString();
        sb.setLength(0);
        this.name = lable_;
    }

    public Lable(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void appendTo(ArrayBuffer sb) {
        sb.append(name);
    }
}
