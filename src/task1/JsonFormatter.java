package task1;

public class JsonFormatter implements ReportFormatter {
    @Override
    public String format(String title, String[] headers, String[][] rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"report\": \"").append(escape(title)).append("\",\n  \"records\": [\n");
        for (int i = 0; i < rows.length; i++) {
            sb.append("    {");
            for (int j = 0; j < headers.length; j++) {
                sb.append("\"").append(escape(headers[j])).append("\": \"").append(escape(rows[i][j])).append("\"");
                if (j < headers.length - 1) sb.append(", ");
            }
            sb.append("}").append(i < rows.length - 1 ? ",\n" : "\n");
        }
        sb.append("  ]\n}");
        return sb.toString();
    }

    private static String escape(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                default:
                    if (c < 0x20) escaped.append(String.format("\\u%04x", (int) c));
                    else escaped.append(c);
            }
        }
        return escaped.toString();
    }
}
