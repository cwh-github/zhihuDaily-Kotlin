package com.example.cwh_pc.dailynewsstudy.extension

/**
 * Created by JokAr on 2017/6/30.
 */
object HtmlUtil {

    //css样式,隐藏header
    private const val HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>"

    //css style tag,需要格式化
    private const val NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>"

    // js script tag,需要格式化
    private const val NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>"

    val MIME_TYPE = "text/html; charset=utf-8"
    val ENCODING = "utf-8"

    /**
     * 根据css链接生成Link标签

     * @param url String
     * *
     * @return String
     */
    private fun createCssTag(url: String): String {
        return String.format(NEEDED_FORMAT_CSS_TAG, url)
    }

    /**
     * 根据多个css链接生成Link标签

     * @param urls List<String>
     * *
     * @return String
    </String> */
    private fun createCssTag(urls: List<String>?): String {
        val sb = StringBuilder()
        if (urls != null)
            for (url in urls) {
                sb.append(createCssTag(url))
            }
        return sb.toString()
    }

    /**
     * 根据js链接生成Script标签

     * @param url String
     * *
     * @return String
     */
    private fun createJsTag(url: String): String {
        return String.format(NEEDED_FORMAT_JS_TAG, url)
    }

    /**
     * 根据多个js链接生成Script标签

     * @param urls List<String>
     * *
     * @return String
    </String> */
    private fun createJsTag(urls: List<String>?): String {
        val sb = StringBuilder()
        if (urls != null)
            for (url in urls) {
                sb.append(createJsTag(url))
            }
        return sb.toString()
    }

    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档

     * @param html string
     * *
     * @param css  string
     * *
     * @param js   string
     * *
     * @return string
     */
    private fun createHtmlData(html: String, css: String, js: String): String {
        return css + HIDE_HEADER_STYLE + html + js
    }

    private fun createHtmlDataWithHead(html: String, css: String, js: String):String{
        return css +html + js
    }

    /**
     * 根据News
     * 生成完整的HTML文档
     * *
     * @return String
     */
    fun createHtmlData(cssArray: List<String>?,
                       jsArray: List<String>?,
                       body: String): String {
        val css = HtmlUtil.createCssTag(cssArray)
        val js = HtmlUtil.createJsTag(jsArray)
        return createHtmlData(body, css, js)
    }

    fun createHtmlWithHead(cssArray: List<String>?,
                           jsArray: List<String>?,
                           body: String): String {
        val css = HtmlUtil.createCssTag(cssArray)
        val js = HtmlUtil.createJsTag(jsArray)
        return createHtmlDataWithHead(body, css, js)
    }


}