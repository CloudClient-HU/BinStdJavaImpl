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
		return "// This is an automatically generated method, you should probably not modify this\nstatic <T, " + getRepeated(1, n, ", ", "F%d") +
			"> Codec<T> rec("+
			switch (n) {
				case 1 -> "Function";
				case 2 -> "BiFunction";
				default -> "Function" + n;
			} +
			"<" +
			getRepeated(1, n, ", ", "F%d") +
			", T> factory, " +
			getRepeated(1, n, ", ", "Codec<F%1$d> c%1$d, Function<T, F%1$d> g%1$d") +
			") {\n\treturn new Codec<>() {\n\n\t\t@Override\n\t\tpublic T decode(DataInputWrapper in) throws IOException {\n\t\t\t" +
			getRepeated(1, n, "\n\t\t\t", "F%1$d v%1$d = c%1$d.decode(in);") +
			"\n\t\t\treturn factory.apply(" +
			getRepeated(1, n, ", ", "v%1$d") +
			");\n\t\t}\n\n\t\t@Override\n\t\tpublic void encode(DataOutputWrapper out, T value) throws IOException {\n\t\t\t" +
			getRepeated(1, n, "\n\t\t\t", "c%1$d.encode(out, g%1$d.apply(value));") +
			"\n\t\t}\n\t};\n}";
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
