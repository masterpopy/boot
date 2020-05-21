package personal.popy.record.xse;


import java.io.PrintStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ScriptDelegate implements OverWorldScripts, InvocationHandler {

    private OverWorldScripts xse;

    protected PrintStream out;
    protected ArrayBuffer sb = new ArrayBuffer(256 * 2, 256 * 2);
    protected String argSep = ", ";

    public ScriptDelegate() {
        xse = (OverWorldScripts) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{OverWorldScripts.class}, this);
    }

    public void setArgSep(String argSep) {
        this.argSep = argSep;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);
        }

        append(getName(method), args);
        return null;
    }


    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        Class<?> declaringClass = method.getDeclaringClass();
        return constructor.newInstance(declaringClass, 15).unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & 1033) == 1 && method.getDeclaringClass().isInterface();
    }

    protected String getName(Method method) {
        return method.getName();
    }

    protected void append0(String methodName, Object... args) {
        append(methodName, args);
    }

    protected void append(String methodName, Object[] args) {
        sb.append(methodName);
        if (!(args == null || args.length == 0)) {
            sb.append(" ");
            appendArgs(args);
        }

        String x = sb.getStrAndReset();
        if (out != null) {
            out.println(x);
        }
        System.out.println(x);
    }

    private void appendArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return;
        }
        for (Object arg : args) {
            if (arg == null) {
                throw new NullPointerException();
            }
            if (arg instanceof Integer) {
                sb.append((Integer) arg);
            } else if (arg instanceof String) {
                sb.append((String) arg);
            } else if (arg instanceof Object[]) {
                appendArgs((Object[]) arg);
                continue;
            } else if (arg instanceof Value) {
                ((Value) arg).appendTo(sb);
            }
            sb.append(argSep);
        }
        sb.setCharIndex(sb.charIndex - argSep.length());
    }

    @Override
    public void lock() {
        xse.lock();
    }

    @Override
    public void message(Object text) {
        xse.message(text);
    }

    @Override
    public void random(Object limit) {
        xse.random(limit);
    }

    @Override
    public void release() {
        xse.release();
    }

    @Override
    public void end() {
        xse.end();
    }

    @Override
    public void goto_if(int condition, Object destination) {
        xse.goto_if(condition, destination);
    }

    @Override
    public void callstd(Object function) {
        xse.callstd(function);
    }

    @Override
    public void nop1() {
        xse.nop1();
    }

    @Override
    public void return0() {
        xse.return0();
    }

    @Override
    public void call(Object destination) {
        xse.call(destination);
    }

    @Override
    public void returnram() {
        xse.returnram();
    }

    @Override
    public void killscript() {
        xse.killscript();
    }

    @Override
    public void nop() {
        xse.nop();
    }

    @Override
    public void goto0(Object destination) {
        xse.goto0(destination);
    }

    @Override
    public void gotostd(Object function) {
        xse.gotostd(function);
    }

    @Override
    public void call_if(int condition, Object destination) {
        xse.call_if(condition, destination);
    }

    @Override
    public void gotostd_if(Object condition, Object function) {
        xse.gotostd_if(condition, function);
    }

    @Override
    public void callstd_if(Object condition, Object function) {
        xse.callstd_if(condition, function);
    }

    @Override
    public void setptrbyte(Object source, Object destination) {
        xse.setptrbyte(source, destination);
    }

    @Override
    public void setflag(Object a) {
        xse.setflag(a);
    }

    @Override
    public void writebytetoaddr(Object value, Object offset) {
        xse.writebytetoaddr(value, offset);
    }

    @Override
    public void waitstate() {
        xse.waitstate();
    }

    @Override
    public void loadbytefromaddr(Object destination, Object source) {
        xse.loadbytefromaddr(destination, source);
    }

    @Override
    public void copybyte(Object destination, Object source) {
        xse.copybyte(destination, source);
    }

    @Override
    public void addvar(Object destination, Object value) {
        xse.addvar(destination, value);
    }

    @Override
    public void setorcopyvar(Object destination, Object source) {
        xse.setorcopyvar(destination, source);
    }

    @Override
    public void copylocal(Object destination, Object source) {
        xse.copylocal(destination, source);
    }

    @Override
    public void subvar(Object destination, Object value) {
        xse.subvar(destination, value);
    }

    @Override
    public void callasm(Object func) {
        xse.callasm(func);
    }

    @Override
    public void special(Object function) {
        xse.special(function);
    }

    @Override
    public void specialvar(Object output, Object function) {
        xse.specialvar(output, function);
    }

    @Override
    public void loadbyte(Object destination, Object value) {
        xse.loadbyte(destination, value);
    }

    @Override
    public void setvar(Object destination, Object value) {
        xse.setvar(destination, value);
    }

    @Override
    public void copyvar(Object destination, Object source) {
        xse.copyvar(destination, source);
    }

    @Override
    public void gotonative(Object func) {
        xse.gotonative(func);
    }

    @Override
    public void specialvar_(Object output, Object functionId) {
        xse.specialvar_(output, functionId);
    }

    @Override
    public void delay(Object time) {
        xse.delay(time);
    }

    @Override
    public void waitse() {
        xse.waitse();
    }

    @Override
    public void warpdoor(Object b, Object warp, Object X, Object Y) {
        xse.warpdoor(b, warp, X, Y);
    }

    @Override
    public void warpteleport(Object map, Object warp, Object X, Object Y) {
        xse.warpteleport(map, warp, X, Y);
    }

    @Override
    public void setwarp(Object map, Object warp, Object X, Object Y) {
        xse.setwarp(map, warp, X, Y);
    }

    @Override
    public void fadedefaultbgm() {
        xse.fadedefaultbgm();
    }

    @Override
    public void fadenewbgm(Object song_number) {
        xse.fadenewbgm(song_number);
    }

    @Override
    public void savebgm(Object song_number) {
        xse.savebgm(song_number);
    }

    @Override
    public void initclock(Object hour, Object minute) {
        xse.initclock(hour, minute);
    }

    @Override
    public void clearflag(Object a) {
        xse.clearflag(a);
    }

    @Override
    public void playbgm(Object song_number, Object unknown) {
        xse.playbgm(song_number, unknown);
    }

    @Override
    public void fadeinbgm(Object speed) {
        xse.fadeinbgm(speed);
    }

    @Override
    public void warp(Object b, Object warp, Object X, Object Y) {
        xse.warp(b, warp, X, Y);
    }

    @Override
    public void playfanfare(Object fanfare_number) {
        xse.playfanfare(fanfare_number);
    }

    @Override
    public void waitfanfare() {
        xse.waitfanfare();
    }

    @Override
    public void playse(Object sound_number) {
        xse.playse(sound_number);
    }

    @Override
    public void fadeoutbgm(Object speed) {
        xse.fadeoutbgm(speed);
    }

    @Override
    public void checkflag(Object a) {
        xse.checkflag(a);
    }

    @Override
    public void warpsilent(Object b, Object warp, Object X, Object Y) {
        xse.warpsilent(b, warp, X, Y);
    }

    @Override
    public void warphole(Object b) {
        xse.warphole(b);
    }

    @Override
    public void gettime() {
        xse.gettime();
    }

    @Override
    public void setobjectmovementtype(Object word, Object b) {
        xse.setobjectmovementtype(word, b);
    }

    @Override
    public void setmysteryeventstatus(Object value) {
        xse.setmysteryeventstatus(value);
    }

    @Override
    public void gotopostbattlescript() {
        xse.gotopostbattlescript();
    }

    @Override
    public void copyobjectxytoperm(Object index) {
        xse.copyobjectxytoperm(index);
    }

    @Override
    public void compare_addr_to_local(Object a, Object b) {
        xse.compare_addr_to_local(a, b);
    }

    @Override
    public void compare_var_to_var(Object var1, Object var2) {
        xse.compare_var_to_var(var1, var2);
    }

    @Override
    public void compare_local_to_local(Object byte1, Object byte2) {
        xse.compare_local_to_local(byte1, byte2);
    }

    @Override
    public void compare_local_to_value(Object a, Object b) {
        xse.compare_local_to_value(a, b);
    }

    @Override
    public void compare_local_to_addr(Object a, Object b) {
        xse.compare_local_to_addr(a, b);
    }

    @Override
    public void compare_addr_to_value(Object a, Object b) {
        xse.compare_addr_to_value(a, b);
    }

    @Override
    public void compare_addr_to_addr(Object a, Object b) {
        xse.compare_addr_to_addr(a, b);
    }

    @Override
    public void compare_var_to_value(Object var, Object value) {
        xse.compare_var_to_value(var, value);
    }

    @Override
    public void dotimebasedevents() {
        xse.dotimebasedevents();
    }

    @Override
    public void trainerbattlebegin() {
        xse.trainerbattlebegin();
    }

    @Override
    public void buffercontesttypestring(Object out, Object word) {
        xse.buffercontesttypestring(out, word);
    }

    @Override
    public void drawcontestwinner(Object a) {
        xse.drawcontestwinner(a);
    }

    @Override
    public void bufferspeciesname(Object out, Object species) {
        xse.bufferspeciesname(out, species);
    }

    @Override
    public void pokemartdecoration2(Object products) {
        xse.pokemartdecoration2(products);
    }

    @Override
    public void buffertrainerclassname(Object out, Object c) {
        xse.buffertrainerclassname(out, c);
    }

    @Override
    public void bufferitemnameplural(Object out, Object item, Object quantity) {
        xse.bufferitemnameplural(out, item, quantity);
    }

    @Override
    public void incrementgamestat(Object stat) {
        xse.incrementgamestat(stat);
    }

    @Override
    public void setfieldeffectargument(Object argument, Object param) {
        xse.setfieldeffectargument(argument, param);
    }

    @Override
    public void multichoicedefault(Object x, Object y, Object list, Object d, Object b) {
        xse.multichoicedefault(x, y, list, d, b);
    }

    @Override
    public void showcontestresults() {
        xse.showcontestresults();
    }

    @Override
    public void contestlinktransfer() {
        xse.contestlinktransfer();
    }

    @Override
    public void messageautoscroll(Object pointer) {
        xse.messageautoscroll(pointer);
    }

    @Override
    public void checkmonobedience(Object slot) {
        xse.checkmonobedience(slot);
    }

    @Override
    public void bufferpartymonnick(Object out, Object slot) {
        xse.bufferpartymonnick(out, slot);
    }

    @Override
    public void getpricereduction(Object index) {
        xse.getpricereduction(index);
    }

    @Override
    public void setmonmetlocation(Object slot, Object location) {
        xse.setmonmetlocation(slot, location);
    }

    @Override
    public void checkplayergender() {
        xse.checkplayergender();
    }

    @Override
    public void buffernumberstring(Object out, Object input) {
        xse.buffernumberstring(out, input);
    }

    @Override
    public void pokemartdecoration(Object products) {
        xse.pokemartdecoration(products);
    }

    @Override
    public void resetobjectpriority(Object index, Object b) {
        xse.resetobjectpriority(index, b);
    }

    @Override
    public void fadescreenswapbuffers(Object b) {
        xse.fadescreenswapbuffers(b);
    }

    @Override
    public void buffertrainername(Object out, Object trainer) {
        xse.buffertrainername(out, trainer);
    }

    @Override
    public void setobjectpriority(Object index, Object b, Object priority) {
        xse.setobjectpriority(index, b, priority);
    }

    @Override
    public void setmaplayoutindex(Object index) {
        xse.setmaplayoutindex(index);
    }

    @Override
    public void bufferdecorationname(Object out, Object decoration) {
        xse.bufferdecorationname(out, decoration);
    }

    @Override
    public void bufferleadmonspeciesname(Object out) {
        xse.bufferleadmonspeciesname(out);
    }

    @Override
    public void givemon(Object species, Object level, Object item, Object unknown1, Object unknown2, Object unknown3) {
        xse.givemon(species, level, item, unknown1, unknown2, unknown3);
    }

    @Override
    public void waitbuttonpress() {
        xse.waitbuttonpress();
    }

    @Override
    public void drawboxtext(Object byte1, Object byte2, Object byte3, Object byte4) {
        xse.drawboxtext(byte1, byte2, byte3, byte4);
    }

    @Override
    public void givedecoration(Object decoration) {
        xse.givedecoration(decoration);
    }

    @Override
    public void faceplayer() {
        xse.faceplayer();
    }

    @Override
    public void closeonkeypress() {
        xse.closeonkeypress();
    }

    @Override
    public void multichoice(Object x, Object y, Object list, Object b) {
        xse.multichoice(x, y, list, b);
    }

    @Override
    public void checkitem(Object index, Object quantity) {
        xse.checkitem(index, quantity);
    }

    @Override
    public void getplayerxy(Object X, Object Y) {
        xse.getplayerxy(X, Y);
    }

    @Override
    public void givepcitem(Object index, Object quantity) {
        xse.givepcitem(index, quantity);
    }

    @Override
    public void checkpcitem(Object index, Object quantity) {
        xse.checkpcitem(index, quantity);
    }

    @Override
    public void checktrainerflag(Object trainer) {
        xse.checktrainerflag(trainer);
    }

    @Override
    public void settrainerflag(Object trainer) {
        xse.settrainerflag(trainer);
    }

    @Override
    public void setmonmove(Object index, Object slot, Object move) {
        xse.setmonmove(index, slot, move);
    }

    @Override
    public void checkpartymove(Object index) {
        xse.checkpartymove(index);
    }

    @Override
    public void gotobeatenscript() {
        xse.gotobeatenscript();
    }

    @Override
    public void releaseall() {
        xse.releaseall();
    }

    @Override
    public void checkdecor(Object decoration) {
        xse.checkdecor(decoration);
    }

    @Override
    public void giveegg(Object species) {
        xse.giveegg(species);
    }

    @Override
    public void setholewarp(Object map, Object warp, Object X, Object Y) {
        xse.setholewarp(map, warp, X, Y);
    }

    @Override
    public void setdynamicwarp(Object map, Object warp, Object X, Object Y) {
        xse.setdynamicwarp(map, warp, X, Y);
    }

    @Override
    public void checkitemtype(Object index) {
        xse.checkitemtype(index);
    }

    @Override
    public void setobjectxyperm(Object index, Object x, Object y) {
        xse.setobjectxyperm(index, x, y);
    }

    @Override
    public void takedecoration(Object decoration) {
        xse.takedecoration(decoration);
    }

    @Override
    public void checkdecorspace(Object decoration) {
        xse.checkdecorspace(decoration);
    }

    @Override
    public void showobjectat(Object index, Object b) {
        xse.showobjectat(index, b);
    }

    @Override
    public void checkitemspace(Object index, Object quantity) {
        xse.checkitemspace(index, quantity);
    }

    @Override
    public void cleartrainerflag(Object trainer) {
        xse.cleartrainerflag(trainer);
    }

    @Override
    public void yesnobox(Object x, Object y) {
        xse.yesnobox(x, y);
    }

    @Override
    public void giveitem(Object index, Object quantity) {
        xse.giveitem(index, quantity);
    }

    @Override
    public void hideobjectat(Object index, Object b) {
        xse.hideobjectat(index, b);
    }

    @Override
    public void setobjectxy(Object index, Object x, Object y) {
        xse.setobjectxy(index, x, y);
    }

    @Override
    public void multichoicegrid(Object x, Object y, Object list, Object per_row, Object B) {
        xse.multichoicegrid(x, y, list, per_row, B);
    }

    @Override
    public void drawbox() {
        xse.drawbox();
    }

    @Override
    public void waitmessage() {
        xse.waitmessage();
    }

    @Override
    public void getpartysize() {
        xse.getpartysize();
    }

    @Override
    public void turnobject(Object index, Object direction) {
        xse.turnobject(index, direction);
    }

    @Override
    public void takeitem(Object index, Object quantity) {
        xse.takeitem(index, quantity);
    }

    @Override
    public void lockall() {
        xse.lockall();
    }

    @Override
    public void erasebox(Object byte1, Object byte2, Object byte3, Object byte4) {
        xse.erasebox(byte1, byte2, byte3, byte4);
    }

    @Override
    public void drawmonpic(Object species, Object x, Object y) {
        xse.drawmonpic(species, x, y);
    }

    @Override
    public void erasemonpic() {
        xse.erasemonpic();
    }

    @Override
    public void setdivewarp(Object map, Object warp, Object X, Object Y) {
        xse.setdivewarp(map, warp, X, Y);
    }

    @Override
    public void braillemessage(Object text) {
        xse.braillemessage(text);
    }

    @Override
    public void doweather() {
        xse.doweather();
    }

    @Override
    public void setdoorclosed(Object x, Object y) {
        xse.setdoorclosed(x, y);
    }

    @Override
    public void closedoor(Object x, Object y) {
        xse.closedoor(x, y);
    }

    @Override
    public void fadescreenspeed(Object effect, Object speed) {
        xse.fadescreenspeed(effect, speed);
    }

    @Override
    public void playslotmachine(Object word) {
        xse.playslotmachine(word);
    }

    @Override
    public void playmoncry(Object species, Object effect) {
        xse.playmoncry(species, effect);
    }

    @Override
    public void waitdooranim() {
        xse.waitdooranim();
    }

    @Override
    public void showelevmenu() {
        xse.showelevmenu();
    }

    @Override
    public void setvaddress(Object pointer) {
        xse.setvaddress(pointer);
    }

    @Override
    public void vcall_if(Object b, Object pointer) {
        xse.vcall_if(b, pointer);
    }

    @Override
    public void bufferstring(Object out, Object offset) {
        xse.bufferstring(out, offset);
    }

    @Override
    public void updatemoneybox(Object x, Object y, Object i) {
        xse.updatemoneybox(x, y, i);
    }

    @Override
    public void createvobject(Object sprite, Object byte2, Object x, Object y, Object elevation, Object direction) {
        xse.createvobject(sprite, byte2, x, y, elevation, direction);
    }

    @Override
    public void givemoney(Object value, Object check) {
        xse.givemoney(value, check);
    }

    @Override
    public void takemoney(Object value, Object check) {
        xse.takemoney(value, check);
    }

    @Override
    public void setdooropen(Object x, Object y) {
        xse.setdooropen(x, y);
    }

    @Override
    public void setstepcallback(Object subroutine) {
        xse.setstepcallback(subroutine);
    }

    @Override
    public void setflashradius(Object word) {
        xse.setflashradius(word);
    }

    @Override
    public void addelevmenuitem(Object a, Object b, Object c, Object d) {
        xse.addelevmenuitem(a, b, c, d);
    }

    @Override
    public void showmoneybox(Object x, Object y, Object check) {
        xse.showmoneybox(x, y, check);
    }

    @Override
    public void setberrytree(Object tree_id, Object berry, Object growth_stage) {
        xse.setberrytree(tree_id, berry, growth_stage);
    }

    @Override
    public void checkmoney(Object value, Object check) {
        xse.checkmoney(value, check);
    }

    @Override
    public void setmetatile(Object x, Object y, Object metatile_number, Object has_collision) {
        xse.setmetatile(x, y, metatile_number, has_collision);
    }

    @Override
    public void fadescreen(Object effect) {
        xse.fadescreen(effect);
    }

    @Override
    public void hidemoneybox() {
        xse.hidemoneybox();
    }

    @Override
    public void setrespawn(Object heallocation) {
        xse.setrespawn(heallocation);
    }

    @Override
    public void animateflash(Object b) {
        xse.animateflash(b);
    }

    @Override
    public void resetweather() {
        xse.resetweather();
    }

    @Override
    public void bufferstdstring(Object out, Object index) {
        xse.bufferstdstring(out, index);
    }

    @Override
    public void takecoins(Object count) {
        xse.takecoins(count);
    }

    @Override
    public void dowildbattle() {
        xse.dowildbattle();
    }

    @Override
    public void pokemart(Object products) {
        xse.pokemart(products);
    }

    @Override
    public void bufferitemname(Object out, Object item) {
        xse.bufferitemname(out, item);
    }

    @Override
    public void waitfieldeffect(Object animation) {
        xse.waitfieldeffect(animation);
    }

    @Override
    public void setweather(Object type) {
        xse.setweather(type);
    }

    @Override
    public void startcontest() {
        xse.startcontest();
    }

    @Override
    public void choosecontestmon() {
        xse.choosecontestmon();
    }

    @Override
    public void givecoins(Object count) {
        xse.givecoins(count);
    }

    @Override
    public void vgoto(Object pointer) {
        xse.vgoto(pointer);
    }

    @Override
    public void vcall(Object pointer) {
        xse.vcall(pointer);
    }

    @Override
    public void vgoto_if(Object b, Object pointer) {
        xse.vgoto_if(b, pointer);
    }

    @Override
    public void buffermovename(Object out, Object move) {
        xse.buffermovename(out, move);
    }

    @Override
    public void turnvobject(Object index, Object direction) {
        xse.turnvobject(index, direction);
    }

    @Override
    public void opendoor(Object x, Object y) {
        xse.opendoor(x, y);
    }

    @Override
    public void checkcoins(Object out) {
        xse.checkcoins(out);
    }

    @Override
    public void dofieldeffect(Object animation) {
        xse.dofieldeffect(animation);
    }

    @Override
    public void setwildbattle(Object species, Object level, Object item) {
        xse.setwildbattle(species, level, item);
    }

    @Override
    public void hidecoinsbox(Object x, Object y) {
        xse.hidecoinsbox(x, y);
    }

    @Override
    public void updatecoinsbox(Object x, Object y) {
        xse.updatecoinsbox(x, y);
    }

    @Override
    public void unloadhelp() {
        xse.unloadhelp();
    }

    @Override
    public void warp7(Object b, Object b1, Object word1, Object word2) {
        xse.warp7(b, b1, word1, word2);
    }

    @Override
    public void setmonobedient(Object slot) {
        xse.setmonobedient(slot);
    }

    @Override
    public void cmdD9() {
        xse.cmdD9();
    }

    @Override
    public void setworldmapflag(Object worldmapflag) {
        xse.setworldmapflag(worldmapflag);
    }

    @Override
    public void mossdeepgym1(Object unknown) {
        xse.mossdeepgym1(unknown);
    }

    @Override
    public void vbufferstring(Object b, Object pointer) {
        xse.vbufferstring(b, pointer);
    }

    @Override
    public void gotoram() {
        xse.gotoram();
    }

    @Override
    public void normalmsg() {
        xse.normalmsg();
    }

    @Override
    public void hidebox2() {
        xse.hidebox2();
    }

    @Override
    public void setescapewarp(Object b, Object warp, Object x, Object y) {
        xse.setescapewarp(b, warp, x, y);
    }

    @Override
    public void warp8(Object b, Object b1, Object word1, Object word2) {
        xse.warp8(b, b1, word1, word2);
    }

    @Override
    public void showcoinsbox(Object x, Object y) {
        xse.showcoinsbox(x, y);
    }

    @Override
    public void waitmoncry() {
        xse.waitmoncry();
    }

    @Override
    public void textcolor(Object color) {
        xse.textcolor(color);
    }

    @Override
    public void warpteleport2(Object b, Object warp, Object x, Object y) {
        xse.warpteleport2(b, warp, x, y);
    }

    @Override
    public void cmdD8() {
        xse.cmdD8();
    }

    @Override
    public void message3(Object pointer) {
        xse.message3(pointer);
    }

    @Override
    public void pokenavcall(Object pointer) {
        xse.pokenavcall(pointer);
    }

    @Override
    public void loadhelp(Object pointer) {
        xse.loadhelp(pointer);
    }

    @Override
    public void bufferboxname(Object out, Object box) {
        xse.bufferboxname(out, box);
    }

    @Override
    public void vloadptr(Object pointer) {
        xse.vloadptr(pointer);
    }

    @Override
    public void mossdeepgym4() {
        xse.mossdeepgym4();
    }

    @Override
    public void signmsg() {
        xse.signmsg();
    }

    @Override
    public void comparehiddenvar(Object a, Object value) {
        xse.comparehiddenvar(a, value);
    }

    @Override
    public void mossdeepgym3(Object var) {
        xse.mossdeepgym3(var);
    }

    @Override
    public void mossdeepgym2() {
        xse.mossdeepgym2();
    }

    @Override
    public void vmessage(Object pointer) {
        xse.vmessage(pointer);
    }

    @Override
    public void loadword(Object destination, Object value) {
        xse.loadword(destination, value);
    }

    public void applymovement(@DataSize(2) Object index, WalkLable movement) {
        xse.applymovement(index, movement);
    }

    public void applymovement(@DataSize(2) Object index, Object movement, byte map) {
        xse.applymovement(index, movement, map);
    }


    public void waitmovement(@DataSize(2) Object index) {
        xse.waitmovement(index);
    }


    public void waitmovement(@DataSize(2) Object index, byte map) {
        xse.waitmovement(index, map);
    }


    public void showsprite(@DataSize(2) Object index) {
        xse.showsprite(index);
    }


    public void removeobject(@DataSize(2) Object index) {
        xse.removeobject(index);
    }

    @Override
    public void trainerbattle(int type, Object trainer, Object localId, Object p1, Object... p2_p4) {
        xse.trainerbattle(type, trainer, localId, p1, p2_p4);
    }

}
