import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
E->E+T|T
T->F*T|F
F->(E)|i
#
 */
public class Grammars {
    static Set<Character> ss = null;
    static String start;

    public static void getSuanFu(String a) {
        for (int i = 0; i < a.length(); i++) {
            char cc = a.charAt(i);
            if (cc != 'ε' && cc != '-' && cc != '>' && cc != '|' && (!Judge.isVn(cc))) {
                ss.add(cc);
            }
        }
    }

    public static void main(String[] args) {
        start = "";
        ss = new HashSet<>();
        Scanner input = new Scanner(System.in);
        ArrayList<String> ls = new ArrayList<>();
        while (true) {
            String tmp = input.nextLine();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|&nbsp;");
            Matcher m = p.matcher(tmp);
            tmp = m.replaceAll("");
            if (tmp.equals("#")) break;
            getSuanFu(tmp);
            ls.add(tmp);
        }
        ls = Judge.removeDirectLeftRecur(ls);
        ArrayList<String> tmp = new ArrayList<>(ls);
        First fir = new First();
        fir.findVtFirst(ls);
        ls = tmp;
        start = "" + ls.get(0).charAt(0);
        Follow foll = new Follow();
        foll.findFollow(ls, fir.getFirstMap());

        TipList tls = new TipList(fir.getFirstMap(), foll.getFollowMap(), ss, start, ls);
        System.out.println("follow集合:");
        print(foll.getFollowMap());
        System.out.println("first集合:");
        print(fir.getFirstMap());
        tls.process();
        System.out.println("请输入测试表达式：");
        String sss = input.next();
        tls.analy(sss);
    }

    public static void print(Map<Character, Set<String>> setMap) {
        for (Map.Entry<Character, Set<String>> oo : setMap.entrySet()) {
            Set<String> set = oo.getValue();
            System.out.println("非终结符号:" + oo.getKey());
            System.out.print("集合:");
            for (String jj : set) {
                System.out.print(jj + "   ");
            }
            System.out.println();
        }
    }
}
