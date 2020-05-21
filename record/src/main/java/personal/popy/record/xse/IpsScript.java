package personal.popy.record.xse;

import java.io.*;

public class IpsScript extends AsmGenerator {
    private final String rom;

    public IpsScript(String rom, String outputPath) {
        this.rom = rom;
        try {
            setOut(new PrintStream(new FileOutputStream(outputPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public WalkLable applymovement(Object index, byte... movement) {
        WalkLable movement1 = new WalkLable(movement);
        addStringLable(movement1);
        super.applymovement(index, movement1);
        return movement1;
    }

    public WalkLable applymovement(Object index, String movement) {

        return applymovement(index, WalkLable.fromString(movement));
    }

    @Override
    protected void before() {
        super.before();
        append0(".gba");
        append0(".thumb");
        append0(".include", stringify("macros.s"));
        append0(".open", stringify(rom), 0x8000000);
        append0(".align 1");
    }

    private String stringify(String name) {
        sb.append('"').append(name).append('"');
        return sb.getStrAndReset();
    }

    @Override
    protected void after() {
        super.after();
        append0(".close");
        out.close();
    }

    //armips不支持可选参数
    @Override
    public void trainerbattle(int type, Object trainer, Object localId, Object p1, Object... p2_p4) {
        Object[] array = {0, 0, 0};
        System.arraycopy(p2_p4, 0, array, 0, p2_p4.length);
        for (Object o : array) {
            if (o instanceof StringLable) {
                addStringLable((StringLable) o);
            }
        }
        super.trainerbattle(type, trainer, localId, p1, array);
    }

    @Override
    protected void scripts() {
        org(0x9FF0000);
        Lable dong = new Lable();
        call(dong);
        call(dong);
        call(dong);
        applymovement(MOVE_PLAYER, "!");
        waitmovement(0);
        msgbox(MSG_KEEPOPEN).text("好奇怪的敲门声").line("到楼下看看吧。");
        addvar(0x4092, 1);
        waitmessage();
        closeonkeypress();
        end();
        mark(dong);
        special(0x13D);
        msgbox(MSG_NORMAL).text("咚！");
        return0();

    }


    public void runArmips() throws IOException {
        if (!new File(rom).exists()) {
            return;
        }
        String path = getClass().getClassLoader().getResource("").getPath();
        String cmd = path + "armips.exe script.txt -temp tmp.txt";
        InputStream in = Runtime.getRuntime().exec(cmd, null, new File(path))
                .getInputStream();

        byte[] data = new byte[1024];

        while (true) {
            int read = in.read(data);
            if (read == -1) break;
            System.out.println(new String(data, 0, read, "gbk"));
        }


    }

    public void runGba() throws Exception {
        if (!new File(rom).exists()) {
            return;
        }
        String s = "E:\\GAME\\GBA\\VisualBoyAdvance.exe";
        new ProcessBuilder().command(s, rom).start();
    }


}
