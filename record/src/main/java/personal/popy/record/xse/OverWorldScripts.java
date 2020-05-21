package personal.popy.record.xse;

@SuppressWarnings("unused")
public interface OverWorldScripts {

    @Index(0x00)
    void nop();

    @Index(0x01)
    void nop1();

    @Index(0x02)
    void end();

    @Index(0x03)
    void return0();

    @Index(0x04)
    void call(@DataSize(4) Object destination);

    @Index(0x05)
    void goto0(@DataSize(4) Object destination);

    @Index(0x06)
    void goto_if(@DataSize(1) int condition, @DataSize(4) Object destination);

    @Index(0x07)
    void call_if(@DataSize(1) int condition, @DataSize(4) Object destination);

    @Index(0x08)
    void gotostd(@DataSize(1) Object function);

    @Index(0x09)
    void callstd(@DataSize(1) Object function);

    @Index(0x0a)
    void gotostd_if(@DataSize(1) Object condition, @DataSize(1) Object function);

    @Index(0x0b)
    void callstd_if(@DataSize(1) Object condition, @DataSize(1) Object function);

    @Index(0x0c)
    void returnram();

    @Index(0x0d)
    void killscript();

    @Index(0x0e)
    void setmysteryeventstatus(@DataSize(1) Object value);

    @Index(0x0f)
    void loadword(@DataSize(1) Object destination, @DataSize(4) Object value);

    @Index(0x10)
    void loadbyte(@DataSize(1) Object destination, @DataSize(1) Object value);

    @Index(0x11)
    void writebytetoaddr(@DataSize(1) Object value, @DataSize(4) Object offset);

    @Index(0x12)
    void loadbytefromaddr(@DataSize(1) Object destination, @DataSize(4) Object source);

    @Index(0x13)
    void setptrbyte(@DataSize(1) Object source, @DataSize(4) Object destination);

    @Index(0x14)
    void copylocal(@DataSize(1) Object destination, @DataSize(1) Object source);

    @Index(0x15)
    void copybyte(@DataSize(4) Object destination, @DataSize(4) Object source);

    @Index(0x16)
    void setvar(@DataSize(2) Object destination, @DataSize(2) Object value);

    @Index(0x17)
    void addvar(@DataSize(2) Object destination, @DataSize(2) Object value);

    @Index(0x18)
    void subvar(@DataSize(2) Object destination, @DataSize(2) Object value);

    @Index(0x19)
    void copyvar(@DataSize(2) Object destination, @DataSize(2) Object source);

    @Index(0x1a)
    void setorcopyvar(@DataSize(2) Object destination, @DataSize(2) Object source);

    @Index(0x1b)
    void compare_local_to_local(@DataSize(1) Object byte1, @DataSize(1) Object byte2);

    @Index(0x1c)
    void compare_local_to_value(@DataSize(1) Object a, @DataSize(1) Object b);

    @Index(0x1d)
    void compare_local_to_addr(@DataSize(1) Object a, @DataSize(4) Object b);

    @Index(0x1e)
    void compare_addr_to_local(@DataSize(4) Object a, @DataSize(1) Object b);

    @Index(0x1f)
    void compare_addr_to_value(@DataSize(4) Object a, @DataSize(1) Object b);

    @Index(0x20)
    void compare_addr_to_addr(@DataSize(4) Object a, @DataSize(4) Object b);

    @Index(0x21)
    void compare_var_to_value(@DataSize(2) Object var, @DataSize(2) Object value);

    @Index(0x22)
    void compare_var_to_var(@DataSize(2) Object var1, @DataSize(2) Object var2);

    //compare
    @Index(0x23)
    void callasm(@DataSize(4) Object func);

    @Index(0x24)
    void gotonative(@DataSize(4) Object func);

    @Index(0x25)
    void special(@DataSize(2) Object function);

    @Index(0x26)
    void specialvar(@DataSize(2) Object output, @DataSize(2) Object function);

    @Index(0x26)
    void specialvar_(@DataSize(2) Object output, @DataSize(2) Object functionId);

    @Index(0x27)
    void waitstate();

    @Index(0x28)
    void delay(@DataSize(2) Object time);

    @Index(0x29)
    void setflag(@DataSize(2) Object a);

    @Index(0x2a)
    void clearflag(@DataSize(2) Object a);

    @Index(0x2b)
    void checkflag(@DataSize(2) Object a);

    @Index(0x2c)
    void initclock(@DataSize(2) Object hour, @DataSize(2) Object minute);

    @Index(0x2d)
    void dotimebasedevents();

    @Index(0x2e)
    void gettime();

    @Index(0x2f)
    void playse(@DataSize(2) Object sound_number);

    @Index(0x30)
    void waitse();

    @Index(0x31)
    void playfanfare(@DataSize(2) Object fanfare_number);

    @Index(0x32)
    void waitfanfare();

    @Index(0x33)
    void playbgm(@DataSize(2) Object song_number, @DataSize(1) Object unknown);

    @Index(0x34)
    void savebgm(@DataSize(2) Object song_number);

    @Index(0x35)
    void fadedefaultbgm();

    @Index(0x36)
    void fadenewbgm(@DataSize(2) Object song_number);

    @Index(0x37)
    void fadeoutbgm(@DataSize(1) Object speed);

    @Index(0x38)
    void fadeinbgm(@DataSize(1) Object speed);

    @Index(0x39)
    void warp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x3a)
    void warpsilent(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x3b)
    void warpdoor(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x3c)
    void warphole(@DataSize(1) Object b);

    @Index(0x3d)
    void warpteleport(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x3e)
    void setwarp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x3f)
    void setdynamicwarp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x40)
    void setdivewarp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x41)
    void setholewarp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x42)
    void getplayerxy(@DataSize(2) Object X, @DataSize(2) Object Y);

    @Index(0x43)
    void getpartysize();

    @Index(0x44)
    void giveitem(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x45)
    void takeitem(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x46)
    void checkitemspace(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x47)
    void checkitem(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x48)
    void checkitemtype(@DataSize(2) Object index);

    @Index(0x49)
    void givepcitem(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x4a)
    void checkpcitem(@DataSize(2) Object index, @DataSize(2) Object quantity);

    @Index(0x4b)
    void givedecoration(@DataSize(2) Object decoration);

    @Index(0x4c)
    void takedecoration(@DataSize(2) Object decoration);

    @Index(0x4d)
    void checkdecor(@DataSize(2) Object decoration);

    @Index(0x4e)
    void checkdecorspace(@DataSize(2) Object decoration);

    @Index(0x4f)
    void applymovement(@DataSize(2) Object index, WalkLable movement);

    @Index(0x50)
    void applymovement(@DataSize(2) Object index, Object movement, byte map);

    @Index(0x51)
    void waitmovement(@DataSize(2) Object index);

    @Index(0x52)
    void waitmovement(@DataSize(2) Object index, byte map);

    @Index(0x53)
    void removeobject(@DataSize(2) Object index);

    @Index(0x55)
    void showsprite(@DataSize(2) Object index);

    @Index(0x57)
    void setobjectxy(@DataSize(2) Object index, @DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0x58)
    void showobjectat(@DataSize(2) Object index, @DataSize(1) Object b);

    @Index(0x59)
    void hideobjectat(@DataSize(2) Object index, @DataSize(1) Object b);

    @Index(0x5a)
    void faceplayer();

    @Index(0x5b)
    void turnobject(@DataSize(2) Object index, @DataSize(1) Object direction);

    @Index(0x5c)
    void trainerbattle(int type, @DataSize(2) Object trainer, @DataSize(2) Object localId, Object p1, Object... p2_p4);

    @Index(0x5d)
    void trainerbattlebegin();

    @Index(0x5e)
    void gotopostbattlescript();

    @Index(0x5f)
    void gotobeatenscript();

    @Index(0x60)
    void checktrainerflag(@DataSize(2) Object trainer);

    @Index(0x61)
    void settrainerflag(@DataSize(2) Object trainer);

    @Index(0x62)
    void cleartrainerflag(@DataSize(2) Object trainer);

    @Index(0x63)
    void setobjectxyperm(@DataSize(2) Object index, @DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0x64)
    void copyobjectxytoperm(@DataSize(2) Object index);

    @Index(0x65)
    void setobjectmovementtype(@DataSize(2) Object word, @DataSize(1) Object b);

    @Index(0x66)
    void waitmessage();

    @Index(0x67)
    void message(@DataSize(4) Object text);

    @Index(0x68)
    void closeonkeypress();

    @Index(0x69)
    void lockall();

    @Index(0x6a)
    void lock();

    @Index(0x6b)
    void releaseall();

    @Index(0x6c)
    void release();

    @Index(0x6d)
    void waitbuttonpress();

    @Index(0x6e)
    void yesnobox(@DataSize(1) Object x, @DataSize(1) Object y);

    @Index(0x6f)
    void multichoice(@DataSize(1) Object x, @DataSize(1) Object y, @DataSize(1) Object list, @DataSize(1) Object b);

    @Index(0x70)
    void multichoicedefault(@DataSize(1) Object x, @DataSize(1) Object y, @DataSize(1) Object list, @DataSize(1) Object d, @DataSize(1) Object b);

    @Index(0x71)
    void multichoicegrid(@DataSize(1) Object x, @DataSize(1) Object y, @DataSize(1) Object list, @DataSize(1) Object per_row, @DataSize(1) Object B);

    @Index(0x72)
    void drawbox();

    @Index(0x73)
    void erasebox(@DataSize(1) Object byte1, @DataSize(1) Object byte2, @DataSize(1) Object byte3, @DataSize(1) Object byte4);

    @Index(0x74)
    void drawboxtext(@DataSize(1) Object byte1, @DataSize(1) Object byte2, @DataSize(1) Object byte3, @DataSize(1) Object byte4);

    @Index(0x75)
    void drawmonpic(@DataSize(2) Object species, @DataSize(1) Object x, @DataSize(1) Object y);

    @Index(0x76)
    void erasemonpic();

    @Index(0x77)
    void drawcontestwinner(@DataSize(1) Object a);

    @Index(0x78)
    void braillemessage(@DataSize(4) Object text);

    @Index(0x79)
    void givemon(@DataSize(2) Object species, @DataSize(1) Object level, @DataSize(2) Object item, @DataSize(4) Object unknown1, @DataSize(4) Object unknown2, @DataSize(1) Object unknown3);

    @Index(0x7a)
    void giveegg(@DataSize(2) Object species);

    @Index(0x7b)
    void setmonmove(@DataSize(1) Object index, @DataSize(1) Object slot, @DataSize(2) Object move);

    @Index(0x7c)
    void checkpartymove(@DataSize(2) Object index);

    @Index(0x7d)
    void bufferspeciesname(@DataSize(1) Object out, @DataSize(2) Object species);

    @Index(0x7e)
    void bufferleadmonspeciesname(@DataSize(1) Object out);

    @Index(0x7f)
    void bufferpartymonnick(@DataSize(1) Object out, @DataSize(2) Object slot);

    @Index(0x80)
    void bufferitemname(@DataSize(1) Object out, @DataSize(2) Object item);

    @Index(0x81)
    void bufferdecorationname(@DataSize(1) Object out, @DataSize(2) Object decoration);

    @Index(0x82)
    void buffermovename(@DataSize(1) Object out, @DataSize(2) Object move);

    @Index(0x83)
    void buffernumberstring(@DataSize(1) Object out, @DataSize(2) Object input);

    @Index(0x84)
    void bufferstdstring(@DataSize(1) Object out, @DataSize(2) Object index);

    @Index(0x85)
    void bufferstring(@DataSize(1) Object out, @DataSize(4) Object offset);

    @Index(0x86)
    void pokemart(@DataSize(4) Object products);

    @Index(0x87)
    void pokemartdecoration(@DataSize(4) Object products);

    @Index(0x88)
    void pokemartdecoration2(@DataSize(4) Object products);

    @Index(0x89)
    void playslotmachine(@DataSize(2) Object word);

    @Index(0x8a)
    void setberrytree(@DataSize(1) Object tree_id, @DataSize(1) Object berry, @DataSize(1) Object growth_stage);

    @Index(0x8b)
    void choosecontestmon();

    @Index(0x8c)
    void startcontest();

    @Index(0x8d)
    void showcontestresults();

    @Index(0x8e)
    void contestlinktransfer();

    @Index(0x8f)
    void random(@DataSize(2) Object limit);

    @Index(0x90)
    void givemoney(@DataSize(4) Object value, @DataSize(1) Object check);

    @Index(0x91)
    void takemoney(@DataSize(4) Object value, @DataSize(1) Object check);

    @Index(0x92)
    void checkmoney(@DataSize(4) Object value, @DataSize(1) Object check);

    @Index(0x93)
    void showmoneybox(@DataSize(1) Object x, @DataSize(1) Object y, @DataSize(1) Object check);

    @Index(0x94)
    void hidemoneybox();

    @Index(0x95)
    void updatemoneybox(@DataSize(1) Object x, @DataSize(1) Object y, @DataSize(1) Object i);

    @Index(0x96)
    void getpricereduction(@DataSize(2) Object index);

    @Index(0x97)
    void fadescreen(@DataSize(1) Object effect);

    @Index(0x98)
    void fadescreenspeed(@DataSize(1) Object effect, @DataSize(1) Object speed);

    @Index(0x99)
    void setflashradius(@DataSize(2) Object word);

    @Index(0x9a)
    void animateflash(@DataSize(1) Object b);

    @Index(0x9b)
    void messageautoscroll(@DataSize(4) Object pointer);

    @Index(0x9c)
    void dofieldeffect(@DataSize(2) Object animation);

    @Index(0x9d)
    void setfieldeffectargument(@DataSize(1) Object argument, @DataSize(2) Object param);

    @Index(0x9e)
    void waitfieldeffect(@DataSize(2) Object animation);

    @Index(0x9f)
    void setrespawn(@DataSize(2) Object heallocation);

    @Index(0xa0)
    void checkplayergender();

    @Index(0xa1)
    void playmoncry(@DataSize(2) Object species, @DataSize(2) Object effect);

    @Index(0xa2)
    void setmetatile(@DataSize(2) Object x, @DataSize(2) Object y, @DataSize(2) Object metatile_number, @DataSize(2) Object has_collision);

    @Index(0xa3)
    void resetweather();

    @Index(0xa4)
    void setweather(@DataSize(2) Object type);

    @Index(0xa5)
    void doweather();

    @Index(0xa6)
    void setstepcallback(@DataSize(1) Object subroutine);

    @Index(0xa7)
    void setmaplayoutindex(@DataSize(2) Object index);

    @Index(0xa8)
    void setobjectpriority(@DataSize(2) Object index, @DataSize(1) Object b, @DataSize(1) Object priority);

    @Index(0xa9)
    void resetobjectpriority(@DataSize(2) Object index, @DataSize(1) Object b);

    @Index(0xaa)
    void createvobject(@DataSize(1) Object sprite, @DataSize(1) Object byte2, @DataSize(2) Object x, @DataSize(2) Object y, @DataSize(1) Object elevation, @DataSize(1) Object direction);

    @Index(0xab)
    void turnvobject(@DataSize(1) Object index, @DataSize(1) Object direction);

    @Index(0xac)
    void opendoor(@DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xad)
    void closedoor(@DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xae)
    void waitdooranim();

    @Index(0xaf)
    void setdooropen(@DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xb0)
    void setdoorclosed(@DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xb1)
    void addelevmenuitem(@DataSize(1) Object a, @DataSize(2) Object b, @DataSize(2) Object c, @DataSize(2) Object d);

    @Index(0xb2)
    void showelevmenu();

    @Index(0xb3)
    void checkcoins(@DataSize(2) Object out);

    @Index(0xb4)
    void givecoins(@DataSize(2) Object count);

    @Index(0xb5)
    void takecoins(@DataSize(2) Object count);

    @Index(0xb6)
    void setwildbattle(@DataSize(2) Object species, @DataSize(1) Object level, @DataSize(2) Object item);

    @Index(0xb7)
    void dowildbattle();

    @Index(0xb8)
    void setvaddress(@DataSize(4) Object pointer);

    @Index(0xb9)
    void vgoto(@DataSize(4) Object pointer);

    @Index(0xba)
    void vcall(@DataSize(4) Object pointer);

    @Index(0xbb)
    void vgoto_if(@DataSize(1) Object b, @DataSize(4) Object pointer);

    @Index(0xbc)
    void vcall_if(@DataSize(1) Object b, @DataSize(4) Object pointer);

    @Index(0xbd)
    void vmessage(@DataSize(4) Object pointer);

    @Index(0xbe)
    void vloadptr(@DataSize(4) Object pointer);

    @Index(0xbf)
    void vbufferstring(@DataSize(1) Object b, @DataSize(4) Object pointer);

    @Index(0xc0)
    void showcoinsbox(@DataSize(1) Object x, @DataSize(1) Object y);

    @Index(0xc1)
    void hidecoinsbox(@DataSize(1) Object x, @DataSize(1) Object y);

    @Index(0xc2)
    void updatecoinsbox(@DataSize(1) Object x, @DataSize(1) Object y);

    @Index(0xc3)
    void incrementgamestat(@DataSize(1) Object stat);

    @Index(0xc4)
    void setescapewarp(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xc5)
    void waitmoncry();

    @Index(0xc6)
    void bufferboxname(@DataSize(1) Object out, @DataSize(2) Object box);

    @Index(0xc7)
    void textcolor(@DataSize(1) Object color);

    @Index(0xc8)
    void loadhelp(@DataSize(4) Object pointer);

    @Index(0xc9)
    void unloadhelp();

    @Index(0xca)
    void signmsg();

    @Index(0xcb)
    void normalmsg();

    @Index(0xcc)
    void comparehiddenvar(@DataSize(1) Object a, @DataSize(4) Object value);

    @Index(0xcd)
    void setmonobedient(@DataSize(2) Object slot);

    @Index(0xce)
    void checkmonobedience(@DataSize(2) Object slot);

    @Index(0xcf)
    void gotoram();

    @Index(0xd0)
    void setworldmapflag(@DataSize(2) Object worldmapflag);

    @Index(0xd1)
    void warpteleport2(@DataSize(1) Object b, @DataSize(1) Object warp, @DataSize(2) Object x, @DataSize(2) Object y);

    @Index(0xd2)
    void setmonmetlocation(@DataSize(2) Object slot, @DataSize(1) Object location);

    @Index(0xd3)
    void mossdeepgym1(@DataSize(2) Object unknown);

    @Index(0xd4)
    void mossdeepgym2();

    @Index(0xd5)
    void mossdeepgym3(@DataSize(2) Object var);

    @Index(0xd6)
    void mossdeepgym4();

    @Index(0xd7)
    void warp7(@DataSize(1) Object b, @DataSize(1) Object b1, @DataSize(2) Object word1, @DataSize(2) Object word2);

    @Index(0xd8)
    void cmdD8();

    @Index(0xd9)
    void cmdD9();

    @Index(0xda)
    void hidebox2();

    @Index(0xdb)
    void message3(@DataSize(4) Object pointer);

    @Index(0xdc)
    void fadescreenswapbuffers(@DataSize(1) Object b);

    @Index(0xdd)
    void buffertrainerclassname(@DataSize(1) Object out, @DataSize(2) Object c);

    @Index(0xde)
    void buffertrainername(@DataSize(1) Object out, @DataSize(2) Object trainer);

    @Index(0xdf)
    void pokenavcall(@DataSize(4) Object pointer);

    @Index(0xe0)
    void warp8(@DataSize(1) Object b, @DataSize(1) Object b1, @DataSize(2) Object word1, @DataSize(2) Object word2);

    @Index(0xe1)
    void buffercontesttypestring(@DataSize(1) Object out, @DataSize(2) Object word);

    @Index(0xe2)
    void bufferitemnameplural(@DataSize(1) Object out, @DataSize(2) Object item, @DataSize(2) Object quantity);



    //goto_if_unset
    //goto_if_set
    //goto_if_lt
    //goto_if_eq
    //goto_if_gt
    //goto_if_le
    //goto_if_ge
    //goto_if_ne
    //call_if_unset
    //call_if_set
    //call_if_lt
    //call_if_eq
    //call_if_gt
    //call_if_le
    //call_if_ge
    //call_if_ne
    //switch
    //case
    //msgbox
    //giveitem_std
    //givedecoration_std
    //register_matchcall

}