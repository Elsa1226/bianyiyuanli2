import java.util.ArrayList;

public class Judge {//检查是否为值接左递归
    public static int[] vt=new int[]{1,0,0,0,0,0,1,1,1,0,1,0,0,1,0,0,1,1,0,0,0,1,1,0,0,0};
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
        char x = 'q';
        ArrayList<String> tmp = new ArrayList<>();
        System.out.println("原来的文法产生式：");
        for (String s : ls) {
            System.out.println(s);
        }
        for (String s : ls) {
            int index = s.indexOf("->");
            if (s.charAt(0) == s.charAt(index + 2)) {
                //P->Pa|b    消除左递归的规则  P->bX, X->aX|e
                String a = getA(s);
                int index1 = s.indexOf('|');
                if (index1 == -1) {
                    tmp.add(s);
                    continue;
                }
                String b = s.substring(index1 + 1);
                tmp.add(s.charAt(0) + "->" + b + x);
                tmp.add(x + "->" + a + x + "|" + 'ε');
                x += 1;
            } else {
                tmp.add(s);
            }
        }
        System.out.println("消除直接左递归后的结果：");
        for (String s : tmp) {
            System.out.println(s);
        }
        return tmp;
    }

    //是否为终结符号
    public static boolean isVn(char c) {
        //代表非终结符号

        if(c >= 'A' && c <= 'Z'){
            return vt[c - 65] == 0;
        }
        else return c >= 'q' && c <= 'y';
        //return c >= 'A' && c <= 'Z';
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
