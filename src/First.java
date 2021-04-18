import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class First {
    HashMap<Character, Set<String>> setMap;

    public String getInfo(ArrayList<String> ls, char vn) {
        for (String tmp : ls) {
            char cc = tmp.charAt(0);
            if (cc == vn) {
                int index = tmp.indexOf("->");
                String tt;
                tt = tmp.substring(index + 2);
                return tt;
            }
        }
        return "";
    }

    public boolean hasEmpty(char aa, ArrayList<String> ls) {
        String ss = null;
        for (String l : ls) {
            ss = l;
            char tmp = ss.charAt(0);
            if (tmp == aa) {
                break;
            }
        }
        assert ss != null;
        return ss.contains("ε");
    }

    public void findFirst(Set<String> set, ArrayList<String> ls, char start, int index, String info) {
        if (index >= info.length()) {
            return;
        }
        //先在每个产生式中找该非终结符号
        char tmp = info.charAt(index);
        //是个非终结符号
        if (tmp != '|' && Judge.isVn(tmp)) {
            info = getInfo(ls, tmp);
            //判断是否含有空串
            findFirst(set, ls, start, index, info);
            if (index < info.length()) {
                char yy = info.charAt(index + 1);
                //是当前非终结符号包含e，则需要向下再推倒
                if (Judge.isVn(yy) && hasEmpty(tmp, ls) && start != yy) {
                    findFirst(set, ls, start, index + 1, info);
                }
            }
        } else if (tmp != 'ε') {
            if (tmp == '|') {
                return;
            }
            int i;
            for (i = index; i < info.length(); i++) {
                char cc = info.charAt(i);
                if (!Judge.isVn(cc) && cc != '|') {
                    set.add(cc + "");
                } else {
                    break;
                }
            }
            int j = info.indexOf("|");
            if (j != -1) {
                set.add(info.charAt(j + 1) + "");
            }
            if (i >= info.length() || info.charAt(i) == start) {
                return;
            }
            //继续访问下一个是否为终结符号
            char tt = info.charAt(i);
            if (Judge.isVn(tt) && hasEmpty(tt, ls))
                findFirst(set, ls, start, index + 1, info);
        } else {
            set.add(tmp + "");
        }
    }

    //找终结符号开头的文法
    public void findVtFirst(ArrayList<String> ls) {
        setMap = new HashMap<>();
        if (!Judge.systaxRight(ls)) {
            return;
        }
        //替换/////////////////////////////////////
        for (int i = 0; i < ls.size(); i++) {
            String str = ls.get(i);
            Set<String> set = new HashSet<>();
            char aa = str.charAt(0);
            int index = str.indexOf("->");
            str = str.substring(index + 2);
            findFirst(set, ls, aa, 0, str);
            setMap.put(aa, set);
        }
    }

    public HashMap<Character, Set<String>> getFirstMap() {
        return setMap;
    }
}
