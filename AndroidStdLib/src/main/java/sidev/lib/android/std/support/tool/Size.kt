package sidev.lib.android.std.support.tool



/**
 * Untuk mengakomodasi penggunaan [Size] untuk API level di bawah 21.
 */
data class Size(val width: Int, val height: Int){
    companion object{

        /**
         * Parses the specified string as a size value.
         *
         *
         * The ASCII characters `\``u002a` ('*') and
         * `\``u0078` ('x') are recognized as separators between
         * the width and height.
         *
         *
         * For any `Size s`: `Size.parseSize(s.toString()).equals(s)`.
         * However, the method also handles sizes expressed in the
         * following forms:
         *
         *
         * "*width*`x`*height*" or
         * "*width*`*`*height*" `=> new Size(width, height)`,
         * where *width* and *height* are string integers potentially
         * containing a sign, such as "-10", "+7" or "5".
         *
         * <pre>`Size.parseSize("3*+6").equals(new Size(3, 6)) == true
         * Size.parseSize("-3x-6").equals(new Size(-3, -6)) == true
         * Size.parseSize("4 by 3") => throws NumberFormatException
        `</pre> *
         *
         * @param string the string representation of a size value.
         * @return the size value represented by `string`.
         *
         * @throws NumberFormatException if `string` cannot be parsed
         * as a size value.
         * @throws NullPointerException if `string` was `null`
         */
        @Throws(NumberFormatException::class)
        fun parseSize(string: String): Size {
            var sep_ix = string.indexOf('*')
            if (sep_ix < 0) {
                sep_ix = string.indexOf('x')
            }
            if (sep_ix < 0) {
                throw NumberFormatException("Invalid Size: \"$string\"")
            }
            return try {
                Size(string.substring(0, sep_ix).toInt(), string.substring(sep_ix + 1).toInt())
            } catch (e: NumberFormatException) {
                throw NumberFormatException("Invalid Size: \"$string\"")
            }
        }
    }
}