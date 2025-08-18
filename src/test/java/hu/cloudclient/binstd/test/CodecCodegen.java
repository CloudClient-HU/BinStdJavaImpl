package hu.cloudclient.binstd.test;

public class CodecCodegen {

    public static void main(String[] args) {
        int n = 20;

        System.out.println(getImports(n));
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.println(getMethodDef(i + 1) + "\n");
        }
    }

    public static String getImports(int n) {
        return "import java.util.function.Function;\nimport java.util.function.BiFunction;\n" + getRepeated(3, n, "\n", "import hu.cloudclient.binstd.function.Function%d;");
    }

    public static String getMethodDef(int n) {
        return "// This is an automatically generated method, you should probably not modify this\nstatic <CT, " + getRepeated(1, n, ", ", "T%d") +
            "> Codec<CT> composite("+
            switch (n) {
                case 1 -> "Function";
                case 2 -> "BiFunction";
                default -> "Function" + n;
            } +
            "<" +
            getRepeated(1, n, ", ", "T%d") +
            ", CT> factory, " +
            getRepeated(1, n, ", ", "Codec<T%1$d> c%1$d, Function<CT, T%1$d> g%1$d") +
            ") {\n    return new Codec<>() {\n\n        @Override\n        public CT decode(DataInputWrapper in) throws IOException {\n            " +
            getRepeated(1, n, "\n            ", "T%1$d v%1$d = c%1$d.decode(in);") +
            "\n            return factory.apply(" +
            getRepeated(1, n, ", ", "v%1$d") +
            ");\n        }\n\n        @Override\n        public void encode(DataOutputWrapper out, CT value) throws IOException {\n            " +
            getRepeated(1, n, "\n            ", "c%1$d.encode(out, g%1$d.apply(value));") +
            "\n        }\n    };\n}";
    }

    public static String getRepeated(int from, int to, String separator, String string) {
        StringBuilder sb = new StringBuilder();

        for (int i = from - 1; i < to; i++) {
            boolean isLast = i == to - 1;

            sb.append(string.formatted(i + 1));

            if (isLast) {
                return sb.toString();
            }

            sb.append(separator);
        }

        return "";
    }

}
