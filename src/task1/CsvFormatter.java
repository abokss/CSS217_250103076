package task1;

public class CsvFormatter implements ReportFormatter {
    @Override
    public String format(String title, String[] headers, String[][] rows) {
        StringBuilder out = new StringBuilder("# TITLE: ").append(quote(title)).append("\n");
        appendRow(out, headers);
        for (String[] row : rows) appendRow(out, row);
        return out.toString();
    }
    private static String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    private static void appendRow(StringBuilder out, String[] row) {
        for (int i = 0; i < row.length; i++) {
            if (i > 0) out.append(',');
            out.append(quote(row[i]));
        }
        out.append('\n');
    }
}
