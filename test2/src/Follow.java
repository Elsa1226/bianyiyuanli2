import java.util.*;

public class Follow {
    Map<Character, Set<String>> setMap;
    Map<Character, Set<String>> firstMap;
    String start;
    int status;
    ArrayList<String> list;

    public Map<Character, Set<String>> getFollowMap() {
        return setMap;
    }

    public void findFollow(ArrayList<String> ls, Set<String> set, char start, int index) {
        int len = ls.size();
        if (index >= len) {
            return;
        }
        String tmp = ls.get(index);
        int tmpLen = tmp.length();
        char cur = tmp.charAt(0);
        //在集合中找start
        int tipIndex = tmp.indexOf(start);
        if (tipIndex == -1 || tipIndex == 0) {
            findFollow(ls, set, start, index + 1);
        }
        //在文法中找到了相应的非终结符号
        else {
            int i = tipIndex + 1;
            //A->aB这种情况
            if (i >= tmpLen) {
                //将当前这个文法开始符号加到list中，退出递归后将
                list.add(cur + "+" + start);
                return;
            }
            for (i = tipIndex + 1; i < tmpLen; i++) {
                char next = tmp.charAt(i);
                //是非终结符号,考虑第二种情况
                if (next == '|') break;
                //是终结符号直接加到follow表中
                if (!Judge.isVn(next)) {
                    set.add(next + "");
                }
                if (Judge.isVn(next)) {
                    //获取当前非终结符号的first集合
                    Set<String> t = firstMap.get(next);
                    if (!t.isEmpty()) {
                        addFirToFollow(t, set);
                        //第三种情况第二种A->aBc
                        if (firHasEmpty(t)) {
                            list.add(next + "+" + start);
                        }
                    }
                }
            }
        }
    }

    public void addFirToFollow(Set<String> setSrc, Set<String> setDst) {
        for (String s : setSrc) {
            if (!s.equals("ε"))
                setDst.add(s);
        }
    }

    public boolean firHasEmpty(Set<String> ls) {
        for (String s : ls) {
            if (s.equals("ε")) {
                return true;
            }
        }
        return false;
    }

    public void findFollow(ArrayList<String> ls, Map<Character, Set<String>> firstMap) {
        list = new ArrayList<>();
        setMap = new HashMap<>();
        Set<String> set;
        this.firstMap = firstMap;
        //先确定开始符号
        for (int i = 0; i < ls.size(); i++) {
            status = 0;
            set = new HashSet<>();
            String ss = ls.get(i);
            //获取非终结符号
            char tmp = ss.charAt(0);
            if (i == 0) {
                start = tmp + "";
                set.add("#");
            }
            findFollow(ls, set, tmp, 0);
            setMap.put(tmp, set);

            if (!list.isEmpty()) {
                for (String s : list) {
                    char src = s.charAt(0);
                    char dst = s.charAt(s.length() - 1);
                    if(setMap.get(src)!=null){
                        setMap.get(src).forEach(item -> {
                            setMap.get(dst).add(item);
                        });
                    }
                    //setMap.get(dst).addAll(setMap.get(src));
                }
            }
        }
    }
}
