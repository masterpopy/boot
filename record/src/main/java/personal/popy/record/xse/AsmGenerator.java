package personal.popy.record.xse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AsmGenerator extends ScriptDelegate {
    public static final byte EXCLAM = 0x56; //感叹号
    //public static final byte TERM       = -2;
    public static final byte DOWN_FAST = 0x2D;
    public static final byte UP_FAST = 0x2e;
    public static final byte LEFT_FAST = 0x2f;
    public static final byte RIGHT_FAST = 0x30;
    public static final byte FACE_DOWN = 0;
    public static final byte FACE_UP = 1;
    public static final byte FACE_LEFT = 2;
    public static final byte FACE_RIGHT = 3;

    public static final int TRAINER_BATTLE_SINGLE = 0x0;
    public static final int TRAINER_BATTLE_CONTINUE_SCRIPT_NO_MUSIC = 0x1;
    public static final int TRAINER_BATTLE_CONTINUE_SCRIPT = 0x2;
    public static final int TRAINER_BATTLE_SINGLE_NO_INTRO_TEXT = 0x3;
    public static final int TRAINER_BATTLE_DOUBLE = 0x4;
    public static final int TRAINER_BATTLE_REMATCH = 0x5;
    public static final int TRAINER_BATTLE_CONTINUE_SCRIPT_DOUBLE = 0x6;
    public static final int TRAINER_BATTLE_REMATCH_DOUBLE = 0x7;
    public static final int TRAINER_BATTLE_CONTINUE_SCRIPT_DOUBLE_NO_MUSIC = 0x8;
    public static final int TRAINER_BATTLE_PYRAMID = 0x9;
    public static final int TRAINER_BATTLE_SET_TRAINER_A = 0xa;
    public static final int TRAINER_BATTLE_SET_TRAINER_B = 0xb;
    public static final int TRAINER_BATTLE_12 = 0xc;
    public static final int MSG_OBTAIN = 0x0;
    public static final int MSG_FIND = 0x1;
    public static final int MSG_FACE = 0x2;
    public static final int MSG_SIGN = 0x3;
    public static final int MSG_KEEPOPEN = 0x4;
    public static final int MSG_YESNO = 0x5;
    public static final int MSG_NORMAL = 0x6;
    public static final int MSG_POKENAV = 0xa;
    public static final int LT = 0x0;
    public static final int EQ = 0x1;
    public static final int GT = 0x2;
    public static final int LE = 0x3;
    public static final int GE = 0x4;
    public static final int NE = 0x5;
    public static final int MOVE_PLAYER = 0xff;
    public static final int MOVE_CAMERA = 0x7f;


    List<StringLable> lableList;

    public AsmGenerator() {

    }

    @Override
    protected String getName(Method method) {
        Index annotation = method.getAnnotation(Index.class);
        return macros[annotation.value()];
    }


    static String[] macros = {
            "nop",//0x0
            "nop1",//0x1
            "end",//0x2
            "return",//0x3
            "call",//0x4
            "goto",//0x5
            "if1",//0x6
            "if2",//0x7
            "gotostd",//0x8
            "callstd",//0x9
            "gotostdif",//0xA
            "callstdif",//0xB
            "jumpram",//0xC
            "killscript",//0xD
            "setbyte",//0xE
            "loadpointer",//0xF
            "setbyte2",//0x10
            "writebytetooffset",//0x11
            "loadbytefrompointer",//0x12
            "setfarbyte",//0x13
            "copyscriptbanks",//0x14
            "copybyte",//0x15
            "setvar",//0x16
            "addvar",//0x17
            "subvar",//0x18
            "copyvar",//0x19
            "copyvarifnotzero",//0x1A
            "comparebanks",//0x1B
            "comparebanktobyte",//0x1C
            "comparebanktofarbyte",//0x1D
            "comparefarbytetobank",//0x1E
            "comparefarbytetobyte",//0x1F
            "comparefarbytes",//0x20
            "compare",//0x21
            "comparevars",//0x22
            "callasm",//0x23
            "cmd24",//0x24
            "special",//0x25
            "special2",//0x26
            "waitstate",//0x27
            "pause",//0x28
            "setflag",//0x29
            "clearflag",//0x2A
            "checkflag",//0x2B
            "cmd2C",//0x2C
            "checkdailyflags",//0x2D
            "resetvars",//0x2E
            "sound",//0x2F
            "checksound",//0x30
            "fanfare",//0x31
            "waitfanfare",//0x32
            "playsong",//0x33
            "playsong2",//0x34
            "fadedefault",//0x35
            "fadesong",//0x36
            "fadeout",//0x37
            "fadein",//0x38
            "warp",//0x39
            "warpmuted",//0x3A
            "warpwalk",//0x3B
            "warphole",//0x3C
            "warpteleport",//0x3D
            "warp3",//0x3E
            "setwarpplace",//0x3F
            "warp4",//0x40
            "warp5",//0x41
            "getplayerpos",//0x42
            "countpokemon",//0x43
            "additem",//0x44
            "removeitem",//0x45
            "checkitemroom",//0x46
            "checkitem",//0x47
            "checkitemtype",//0x48
            "addpcitem",//0x49
            "checkpcitem",//0x4A
            "adddecoration",//0x4B
            "removedecoration",//0x4C
            "testdecoration",//0x4D
            "checkdecoration",//0x4E
            "applymovement",//0x4F
            "applymovementpos",//0x50
            "waitmovement",//0x51
            "waitmovementpos",//0x52
            "hidesprite",//0x53
            "hidespritepos",//0x54
            "showsprite",//0x55
            "showspritepos",
            "movesprite",//0x57
            "spritevisible",
            "spriteinvisible",
            "faceplayer",//0x5A
            "spriteface",
            "trainerbattle",//0x5C
            "repeattrainerbattle",//0x5D
            "endtrainerbattle",//0x5E
            "endtrainerbattle2",//0x5F
            "checktrainerflag",//0x60
            "settrainerflag",//0x61
            "cleartrainerflag",//0x62
            "movesprite2",//0x63
            "moveoffscreen",//0x64
            "spritebehave",//0x65
            "waitmsg",//0x66
            "preparemsg",//0x67
            "closeonkeypress",//0x68
            "lockall",//0x69
            "lock",//0x6A
            "releaseall",//0x6B
            "release",//0x6C
            "waitkeypress",//0x6D
            "yesnobox",//0x6E
            "multichoice",//0x6F
            "multichoice2",//0x70
            "multichoice3",//0x71
            "showbox",//0x72
            "hidebox",//0x73
            "clearbox",//0x74
            "showpokepic",//0x75
            "hidepokepic",//0x76
            "showcontestwinner",//0x77
            "braille",//0x78
            "givepokemon",//0x79
            "giveegg",//0x7A
            "setpkmnpp",//0x7B
            "checkattack",//0x7C
            "bufferpokemon",//0x7D
            "bufferfirstpokemon",//0x7E
            "bufferpartypokemon",//0x7F
            "bufferitem",//0x80
            "bufferdecoration",//0x81
            "bufferattack",//0x82
            "buffernumber",//0x83
            "bufferstd",//0x84
            "bufferstring",//0x85
            "pokemart",//0x86
            "pokemart2",//0x87
            "pokemart3",//0x88
            "pokecasino",//0x89
            "cmd8A",//0x8A
            "choosecontestpokemon",//0x8B
            "startcontest",//0x8C
            "showcontestresults",//0x8D
            "contestlinktransfer",//0x8E
            "random",//0x8F
            "givemoney",//0x90
            "paymoney",//0x91
            "checkmoney",//0x92
            "showmoney",//0x93
            "hidemoney",//0x94
            "updatemoney",//0x95
            "cmd96",//0x96
            "fadescreen",//0x97
            "fadescreendelay",//0x98
            "darken",//0x99
            "lighten",//0x9A
            "preparmsg2",//0x9B
            "doanimation",//0x9C
            "setanimation",//0x9D
            "checkanimation",//0x9E
            "sethealingplace",//0x9F
            "checkgender",//0xA0
            "cry",//0xA1
            "setmaptile",//0xA2
            "resetweather",//0xA3
            "setweather",//0xA4
            "doweather",//0xA5
            "cmdA6",//0xA6
            "setmapfooter",//0xA7
            "spritelevelup",//0xA8
            "restorespritelevel",//0xA9
            "createsprite",//0xAA
            "spriteface2",//0xAB
            "setdooropened",//0xAC
            "setdoorclosed",//0xAD
            "doorchange",//0xAE
            "setdooropened2",//0xAF
            "setdoorclosed2",//0xB0
            "cmdB1",//0xB1
            "cmdB2",//0xB2
            "checkcoins",//0xB3
            "givecoins",//0xB4
            "removecoins",//0xB5
            "setwildbattle",//0xB6
            "dowildbattle",//0xB7
            "setvirtualaddress",//0xB8
            "virtualgoto",//0xB9
            "virtualcall",//0xBA
            "virtualgotoif",//0xBB
            "virtualcallif",//0xBC
            "virtualmsgbox",//0xBD
            "virtualloadpointer",//0xBE
            "virtualbuffer",//0xBF
            "showcoins",//0xC0
            "hidecoins",//0xC1
            "updatecoins",//0xC2
            "cmdC3",//0xC3
            "warp6",//0xC4
            "waitcry",//0xC5
            "bufferboxname",//0xC6
            "textcolor",//0xC7
            "cmdC8",//0xC8
            "cmdC9",//0xC9
            "signmsg",//0xCA
            "normalmsg",//0xCB
            "comparehiddenvar",//0xCC
            "setobedience",//0xCD
            "checkobedience",//0xCE
            "executeram",//0xCF
            "setworldmapflag",//0xD0
            "warpteleport2",//0xD1
            "setcatchlocation",//0xD2
            "braille2",//0xD3
            "bufferitems",//0xD4
            "cmdD5",//0xD5
            "cmdD6",//0xD6
            "warp7",//0xD7
            "cmdD8",//0xD8
            "cmdD9",//0xD9
            "hidebox2",//0xDA
            "preparemsg3",//0xDB
            "fadescreen3",//0xDC
            "buffertrainerclass",//0xDD
            "buffertrainername",//0xDE
            "pokenavcall",//0xDF
            "warp8",//0xE0
            "buffercontesttype",//0xE1
            "bufferitems2"//0xE2
    };

    protected abstract void scripts();


    public void generate() {
        this.before();
        scripts();
        this.after();
    }

    public Lable lable() {
        return new Lable();
    }

    protected void before() {
    }

    protected void after() {
        if (lableList == null) {
            return;
        }
        for (StringLable lable : lableList) {
            mark(lable);
            lable.getBytes(sb);
            bytes(sb);
        }
    }

    public void mark(Lable lable) {
        out.println();
        append0(lable.name, ":");
    }

    public void msgbox(int text, int type) {
        append0("msgbox", text, type);
    }

    public StringLable msgbox(int type) {
        StringLable e = new StringLable();
        append0("msgbox", e, type);
        addStringLable(e);
        return e;
    }

    public StringLable msgbox(StringLable e, int type) {
        append0("msgbox", e, type);
        return e;
    }

    protected void addStringLable(StringLable e) {
        if (lableList == null) {
            lableList = new ArrayList<>();
        }

        lableList.add(e);
    }

    public void bytes(ArrayBuffer text) {
        String bytesHex = text.getBytesHex();
        append0(".byte", bytesHex);
    }

    public void giveitem(Object item, Object count, Object function) {
        append0("giveitem", count, function);
    }

    public void org(int offset) {
        append0(".org", offset);
    }

    public void trainerbattle_single(Object trainer, Object intro_text, Object lose_text) {
        trainerbattle(TRAINER_BATTLE_SINGLE, trainer, 0, intro_text, lose_text);
    }

    public void trainer_battle_continue_script(Object trainer, Object intro_text, Object lose_text, Object event_script) {
        trainerbattle(TRAINER_BATTLE_CONTINUE_SCRIPT, trainer, 0, intro_text, lose_text, event_script);
    }

    public void trainer_battle_continue_script_no_music(Object trainer, Object intro_text, Object lose_text, Object event_script) {
        trainerbattle(TRAINER_BATTLE_CONTINUE_SCRIPT_NO_MUSIC, trainer, 0, intro_text, lose_text, event_script);
    }

    public void trainer_battle_single_no_intro_text(Object trainer, Object lose_text) {
        trainerbattle(TRAINER_BATTLE_SINGLE_NO_INTRO_TEXT, trainer, 0, lose_text);
    }

    public void trainer_battle_double(Object trainer, Object intro_text, Object lose_text, Object not_enough_pkmn_text) {
        trainerbattle(TRAINER_BATTLE_DOUBLE, trainer, 0, intro_text, lose_text, not_enough_pkmn_text);
    }

    public void trainer_battle_continue_script_double(Object trainer, Object intro_text, Object lose_text,
                                                      Object not_enough_pkmn_text, Object event_script) {
        trainerbattle(TRAINER_BATTLE_CONTINUE_SCRIPT_DOUBLE, trainer, 0, intro_text,
                lose_text, not_enough_pkmn_text, event_script);
    }

    public void trainer_battle_continue_script_double_no_music(Object trainer, Object intro_text, Object lose_text,
                                                               Object not_enough_pkmn_text, Object event_script) {
        trainerbattle(TRAINER_BATTLE_CONTINUE_SCRIPT_DOUBLE_NO_MUSIC, trainer, 0, intro_text,
                lose_text, not_enough_pkmn_text, event_script);
    }

    public void trainerbattle_rematch(Object trainer, Object intro_text, Object lose_text) {
        trainerbattle(TRAINER_BATTLE_REMATCH, trainer, 0, intro_text, lose_text);
    }

    public void trainerbattle_rematch_double(Object trainer, Object intro_text, Object lose_text) {
        trainerbattle(TRAINER_BATTLE_REMATCH_DOUBLE, trainer, 0, intro_text, lose_text);
    }
}
