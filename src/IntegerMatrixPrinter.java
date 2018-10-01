public final class IntegerMatrixPrinter {
    public static String writeToString(Integer[][] array) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            result.append('|').append("\t");
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] != null) {
                    result.append(array[i][j]).append("\t");
                } else {
                    result.append("_\t");
                }
            }
            result.append('|').append("\n");
        }
        return result.toString();
    }
}