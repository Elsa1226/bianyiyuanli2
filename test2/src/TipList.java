import java.util.*;

public class TipList {
    Map<String, String> PredictList;
    Map<Character, Set<String>> follMap;
    Map<Character, Set<String>> firstMap;
    Set<Character> ss;
    String start;

    ArrayList<String> list;

    public TipList(Map<Character, Set<String>> firMap, Map<Character,
            Set<String>> follMap, Set<Character> ss, String start, ArrayList<String> ls) {
        PredictList = new HashMap<>();
        this.follMap = follMap;
        this.firstMap = firMap;
        this.ss = ss;
        this.start = start;
        list = ls;
    }

    public void process() {
        ArrayList<Character> arr = new ArrayList<>();
        ss.add('#');
        System.out.printf("\n%15s", " ");
        for (char a : ss) {
            arr.add(a);
            System.out.printf("%15s", a);
        }
        //构造预测分析表
        for (String sss : list) {
            char start = sss.charAt(0);
            System.out.printf("\n%15s", start + "");
            String value;
            int flag = 0;
            for (char s : arr) {
                //在first集合中找到了
                if (sss.contains("ε")) flag = 1;
                String tmp = sss;
                if (find(start, s, 1)) {
                    //查看当前的字符串是否含有空集合
                    int index = sss.indexOf('|');
                    if (index != -1 && (!sss.contains("i"))) sss = sss.substring(0, index);
                    if (isChar(s) && sss.indexOf(s) != -1) {
                        value = start + "->" + s;
                    } else {
                        value = sss;
                        index = sss.indexOf('i');
                        if (index != -1)
                            value = value.substring(0, index - 1);
                    }
                    System.out.printf("%15s", value);
                    PredictList.put(start + "+" + s, value);
                } else if (flag == 1 && find(start, s, 0)) {
                    value = sss.charAt(0) + "->" + "ε";
                    System.out.printf("%15s", value);
                    PredictList.put(start + "+" + s, value);
                } else {//预测分析表的error
                    value = "Error";
                    System.out.printf("%15s", " ");
                    PredictList.put(start + "+" + s, value);
                }
                flag = 0;
                sss = tmp;
            }
            System.out.println();
        }
    }

    public String reverse(String ss) {
        StringBuilder aa = new StringBuilder();
        for (int i = ss.length() - 1; i >= 0; i--) {
            aa.append(ss.charAt(i));
        }
        return aa.toString();
    }

    public void Analysis(String ss) {
        ss = reverse(ss) + "#";
        Stack<String> Grammars = new Stack<>();
        //将起始非终结符号要入栈中
        Grammars.push("#");
        System.out.println(start);
        Grammars.push(start);
        Stack<String> shi = new Stack<>();
        //将起始非终结符号ya入栈中
        shi.push("#");
        for (int i = 0; i < ss.length(); i++) {
            shi.push("" + ss.charAt(i));
        }
        System.out.printf("%15s%15s%15s\n", "分析栈", "剩余输入串", "推倒式");
        while (!ss.isEmpty()) {
            printStack(Grammars);
            String top = Grammars.pop();
            //获取产生式的每一个字符
            char a = ss.charAt(0);

            //是操作数
            while (isChar(a) && !top.equals("i")) {
                //获取相应的文法产生式
                String str = PredictList.get(top + "+i");
                //将相应的文法产生式加到栈中
                pushToStack(Grammars, str);
                System.out.printf("%20s%20s\n", ss, str);
                printStack(Grammars);
                top = Grammars.pop();
                if (top.equals("i")) {
                    System.out.printf("%20s%20s\n", ss, "i");
                }
            }
            if (top.equals("i")) {
                ss = ss.substring(1);
                continue;
            }
            //是运算符号
            if (!isChar(a)) {
                //当前弹出栈顶的元素是非终结符号
                while (Judge.isVn(top.charAt(0)) && !top.equals("" + a)) {
                    //获取当前非终结符号和运算符号映射的文法表达式
                    String strs = PredictList.get(top + "+" + a);
                    System.out.printf("%20s%20s\n", ss, strs);
                    //打印当前栈中的非终结符号
                    pushToStack(Grammars, strs);
                    printStack(Grammars);
                    top = Grammars.pop();
                    if (top.equals("" + a)) {
                        System.out.printf("%20s%20s\n", ss, strs);
                    }
                }
                ss = ss.substring(1);
            }
        }
        if (Grammars.size() == 0) {
            System.out.println("文法分析成功!合法语句");
            return;
        }
        System.out.println("文法分析失败~~~不合法语句");
    }

    public void printStack(Stack<String> s) {
        System.out.println();
        StringBuilder ss = new StringBuilder();
        for (String c : s) {
            ss.append(c);
        }
        System.out.printf("%15s", ss.toString());
    }

    public void pushToStack(Stack<String> s, String str) {
        int index = str.indexOf("->");
        str = str.substring(index + 2);
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == 'ε') continue;
            s.push(str.charAt(i) + "");
        }
    }


    public boolean isChar(char c) {

        return c >= 'a' && c <= 'z';
    }

    public boolean find(char key, char a, int flag) {
        Set<String> ss;
        if (flag == 1) {
            ss = firstMap.get(key);
        } else {
            ss = follMap.get(key);
        }
        for (String c : ss) {
            if (c.charAt(0) == a) {
                return true;
            }
        }
        return false;
    }
}
