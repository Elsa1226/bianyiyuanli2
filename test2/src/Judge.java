import java.util.ArrayList;

public class Judge {//检查是否为值接左递归

    public static String getA(String ss) {
        int s_index = ss.indexOf("->");
        StringBuilder str = new StringBuilder();
        for (int i = s_index + 3; i < ss.length(); i++) {
            if (ss.charAt(i) == '|') break;
            str.append(ss.charAt(i));
        }
        return str.toString();
    }

    public static ArrayList<String> removeDirectLeftRecur(ArrayList<String> ls) {
        char x = 'X';
        ArrayList<String> tmp = new ArrayList<>();
        System.out.println("原来的文法产生式：");
        for (String s : ls) {
            char start = s.charAt(0);
            System.out.println(start + "      " + s);
            int index = s.indexOf("->");
            if (index == -1) {
                continue;
            }
            String cur = s;
            s = s.substring(index);
            index = s.indexOf(start);
            if (index == -1) {
                tmp.add(cur);
                continue;
            }
            //P->Pa|b    消除左递归的规则  P->bX, X->aX|e
            String a = getA(s);
            int index1 = s.indexOf('|');
            if (index1 == -1) {
                tmp.add(s);
                continue;
            }
            String b = s.substring(index1 + 1);
            tmp.add(start + "->" + b + x);
            tmp.add(x + "->" + a + x + "|" + 'ε');
            x += 1;
        }
        //消除左递归后
        System.out.println("消除直接左递归后的结果：");
        for (String s : tmp) {
            System.out.println(s);
        }
        return tmp;
    }

    //是否为终结符号
    public static boolean isVn(char c) {
        //代表非终结符号
        return c >= 'A' && c <= 'Z';
    }

    public static boolean systaxRight(ArrayList<String> ls) {
        for (String ss : ls) {
            if (!ss.contains("->")) {
                return false;
            }
        }
        return true;
    }
}
