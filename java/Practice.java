import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Java の「よく使う」ところだけ (Java 21)
 *
 *   javac Practice.java && java Practice
 *
 * 前提ルール:
 *   - 1ファイルに public クラスは1つだけ、ファイル名と一致させる (Practice.java → public class Practice)
 *   - コードはすべてクラスの中。トップレベルの関数・変数は書けない。
 *   - エントリポイントは public static void main(String[] args)
 */
public class Practice {

    public static void main(String[] args) {
        types();
        strings();
        collections();
        classes();
        inheritance();
        exceptions();
        lambdas();
    }

    // ------------------------------------------------------------------
    // 1. 型: プリミティブ型と参照型が別物、が Java 最大の特徴 
    // https://qiita.com/hiroki-harada/items/cb6fa4affffab2019cde
    // ------------------------------------------------------------------
    static void types() {
        header("1. 型");

        // プリミティブ型 (int, long, double, boolean, char ...) は値そのもの。null になれない。
        int i = 42;
        double d = 3.14;
        boolean b = true;      // if (1) は書けない。条件式には boolean しか置けない。
        char c = 'A';          // char はシングルクォート、String はダブルクォート。別の型。

        // 参照型 = オブジェクト。null になれる。
        // プリミティブには対応するラッパークラスがある (int→Integer, double→Double)
        // コレクションにはプリミティブを入れられないので Integer などを使う。
        Integer boxed = i;     // オートボクシング (自動変換)

        // var は型推論。動的型ではなく、右辺から型が決まってそれ以降変わらない。
        var list = new ArrayList<String>();   // → ArrayList<String> で確定

        System.out.println(i + " " + d + " " + b + " " + c + " " + boxed + " " + list);

        // 整数同士の割り算は切り捨て
        System.out.println("7 / 2 = " + (7 / 2));          // 3
        System.out.println("7 / 2.0 = " + (7 / 2.0));      // 3.5
    }

    // ------------------------------------------------------------------
    // 2. String: 一番よくハマるところ
    // ------------------------------------------------------------------
    static void strings() {
        header("2. String");

        // ★ オブジェクトの比較に == を使ってはいけない。== は「同じインスタンスか」。
        //    中身の比較は必ず equals()。
        String x = "abc";
        String y = new String("abc");
        System.out.println("x == y      -> " + (x == y));        // false
        System.out.println("x.equals(y) -> " + x.equals(y));     // true

        // String は不変(immutable)。メソッドは元を書き換えず新しい String を返す。
        String s = "hello";
        s.toUpperCase();                       // ← 戻り値を捨てているので意味がない
        System.out.println("s = " + s);        // hello のまま
        System.out.println("s.toUpperCase() = " + s.toUpperCase());

        // ループで文字列を組み立てるときは StringBuilder (+ だと毎回オブジェクトが増える)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(i).append(",");
        }
        System.out.println("StringBuilder: " + sb);

        // フォーマット (printf と同じ書式)
        System.out.println(String.format("%s は %d 歳 (%.2f)", "Bob", 30, 1.5));

        // よく使う操作
        System.out.println("length=" + s.length() + " charAt(0)=" + s.charAt(0)
                + " contains(ell)=" + s.contains("ell") + " split=" + String.join("|", "a,b,c".split(",")));
    }

    // ------------------------------------------------------------------
    // 3. 配列とコレクション
    // ------------------------------------------------------------------
    static void collections() {
        header("3. 配列 / コレクション");

        // 配列は固定長。長さは .length (フィールド。メソッドではない)
        int[] arr = {3, 1, 2};
        for (int v : arr) {                    // 拡張 for (foreach)
            System.out.print(v + " ");
        }
        System.out.println("(length=" + arr.length + ")");

        // List / Map はインタフェース。「左辺はインタフェース、右辺は実装クラス」が定石。
        // <> はダイヤモンド演算子で、右辺の型引数を省略できる。
        List<String> names = new ArrayList<>();
        names.add("alice");
        names.add("bob");
        System.out.println(names + " size=" + names.size() + " get(0)=" + names.get(0));

        // ジェネリクスにプリミティブは書けない → List<int> は不可、List<Integer>
        List<Integer> nums = List.of(1, 2, 3);   // List.of は不変 (add すると例外)
        System.out.println("nums = " + nums);

        Map<String, Integer> ages = new HashMap<>();
        ages.put("alice", 20);
        ages.put("bob", 30);
        for (Map.Entry<String, Integer> e : ages.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }
        System.out.println("get(なし)=" + ages.get("none")               // null が返る
                + " getOrDefault=" + ages.getOrDefault("none", -1));
    }

    // ------------------------------------------------------------------
    // 4. クラスの基本形
    // ------------------------------------------------------------------
    static void classes() {
        header("4. クラス");

        User u = new User("alice", 20);
        System.out.println(u.getName() + " / " + u.getAge());
        System.out.println("toString: " + u);          // toString を override してあるので中身が出る

        // static メンバはインスタンスではなくクラスに属する
        System.out.println("作成数 = " + User.getCount());
    }

    // ------------------------------------------------------------------
    // 5. 継承とインタフェース
    // ------------------------------------------------------------------
    static void inheritance() {
        header("5. 継承 / インタフェース");

        // extends は1つだけ (単一継承)、implements は複数可
        Animal a = new Dog("ポチ");   // 変数の型は親、実体は子 = ポリモーフィズム
        a.speak();                    // 実体側の Dog#speak が呼ばれる
        a.introduce();                // 親で定義した共通処理

        Animal cat = new Cat("ミケ");
        cat.speak();

        // インタフェース型でまとめて扱う
        for (Animal animal : List.of(a, cat)) {
            animal.speak();
        }
    }

    // ------------------------------------------------------------------
    // 6. 例外
    // ------------------------------------------------------------------
    static void exceptions() {
        header("6. 例外");

        // 非検査例外 (RuntimeException 系) は catch しなくてもコンパイルは通る
        try {
            check(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("catch: " + e.getMessage());
        } finally {
            System.out.println("finally は必ず実行される");
        }

        // 検査例外 (Exception のサブクラス) は catch するか throws しないとコンパイルエラー
        try {
            Thread.sleep(1);     // InterruptedException = 検査例外
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // null を触ると NullPointerException。Java では例外は「投げる」もので戻り値ではない。
        String s = null;
        try {
            System.out.println(s.length());
        } catch (NullPointerException e) {
            System.out.println("NPE: null のメソッドは呼べない");
        }
    }

    static void check(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("負の値: " + n);
        }
    }

    // ------------------------------------------------------------------
    // 7. ラムダとストリーム (現代の Java では必須)
    // ------------------------------------------------------------------
    static void lambdas() {
        header("7. ラムダ / ストリーム");

        List<String> words = List.of("apple", "banana", "kiwi", "cherry");

        // filter / map / collect のパイプライン。元のリストは変更されない。
        List<String> result = words.stream()
                .filter(w -> w.length() > 4)
                .map(String::toUpperCase)          // メソッド参照 (w -> w.toUpperCase() と同じ)
                .sorted()
                .collect(Collectors.toList());
        System.out.println(result);

        // forEach
        words.forEach(w -> System.out.print(w + " "));
        System.out.println();

        // 集計
        int totalLength = words.stream().mapToInt(String::length).sum();
        System.out.println("合計文字数 = " + totalLength);
    }

    static void header(String title) {
        System.out.println("\n===== " + title + " =====");
    }
}

// ======================================================================
// 同じファイル内のクラスは public を付けられない (package-private)
// ======================================================================

class User {
    // private フィールド + getter、が Java の基本スタイル
    private final String name;    // final = 代入は一度だけ
    private final int age;

    private static int count;     // static = クラスで1つ共有

    User(String name, int age) {  // コンストラクタ: クラス名と同じ、戻り値の型を書かない
        this.name = name;         // this は自分のインスタンス。引数と名前が被るときに必要。
        this.age = age;
        count++;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    static int getCount() {       // static メソッドからは this / インスタンスフィールドを触れない
        return count;
    }

    @Override                     // 付けると綴り間違いをコンパイラが検出してくれる
    public String toString() {
        return "User{name=" + name + ", age=" + age + "}";
    }
}

/** abstract クラス: 単体では new できない。共通実装 + 未実装メソッドを持つ。 */
abstract class Animal {
    protected final String name;  // protected = サブクラスから見える

    Animal(String name) {
        this.name = name;
    }

    abstract void speak();        // 本体なし → サブクラスが必ず実装する

    void introduce() {
        System.out.println("私は " + name + " です");
    }
}

class Dog extends Animal {
    Dog(String name) {
        super(name);              // 親のコンストラクタ呼び出し (必ず先頭行)
    }

    @Override
    void speak() {
        System.out.println(name + ": ワン");
    }
}

class Cat extends Animal {
    Cat(String name) {
        super(name);
    }

    @Override
    void speak() {
        System.out.println(name + ": ニャー");
    }
}
