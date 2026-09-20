package ru.practicum.android.diploma.ui

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextPaint
import android.text.style.LeadingMarginSpan
import android.text.style.LineHeightSpan
import android.text.style.MetricAffectingSpan
import android.util.Log
import org.xml.sax.XMLReader
import ru.practicum.android.diploma.R

class HtmlFormatter(private val context: Context) {
    fun format(htmlString: String): Spanned {
        var customTagString =
            htmlString.replace("<h2>", "<custom_h2>", true)
                .replace("</h2>", "</custom_h2><h2_margin></h2_margin>", true)
                .replace("<p>", "<custom_p>", true)
                .replace("</p>", "</custom_p><p_margin></p_margin>", true)
                .replace("<h3>", "<custom_h3>", true)
                .replace("</h3>", "</custom_h3><h3_margin></h3_margin>", true)
                .replace("<ul>", "<custom_ul>", true)
                .replace("</ul>", "</custom_ul><ul_margin></ul_margin>", true)
                .replace("<li>", "<custom_li>", true)
                .replace("</li>", "</custom_li><li_margin></li_margin>", true)
        customTagString = "<div>$customTagString</div>"
        Log.d("HTML", customTagString)
        val spanned = Html.fromHtml(customTagString, Html.FROM_HTML_MODE_COMPACT, null, CustomTagHandler(context))
        return spanned
    }
}

private class CustomTagHandler(private val context: Context) : Html.TagHandler {

    override fun handleTag(opening: Boolean, tag: String, output: Editable, p3: XMLReader) {
        Log.d("HTML", "opening=$opening tag=$tag length=${output.length}")

        when (tag) {
            "custom_h2" -> setHead2Span(opening, output)
            "custom_h3" -> setHead3Span(opening, output)
            "custom_p" -> setParagraphSpan(opening, output)
            "custom_ul" -> setUnorderedListSpan(opening, output)
            "custom_li" -> setListItemSpan(opening, output)
            "h2_margin" -> setMargin(
                opening,
                output,
                context.resources.getDimensionPixelSize(R.dimen.basic_padding)
            )

            "h3_margin" -> setMargin(opening, output, context.resources.getDimensionPixelSize(R.dimen.micro_padding))
            "p_margin" -> setMargin(opening, output, context.resources.getDimensionPixelSize(R.dimen.basic_padding))
            "ul_margin" -> setMargin(opening, output, context.resources.getDimensionPixelSize(R.dimen.basic_padding))
            "li_margin" -> setMargin(
                opening,
                output,
                context.resources.getDimensionPixelSize(R.dimen.list_items_spacing)
            )

            else -> return
        }
    }

    private fun setHead2Span(opening: Boolean, output: Editable) {
        if (opening) {
            setMarker(output)
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            val end = output.length
            output.removeSpan(marker)

            output.setSpan(
                CustomTypefaceSpan(
                    context.resources.getFont(R.font.ys_display_medium),
                    context.resources.getDimension(R.dimen.html_h2_text_size)
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            output.append("\n")
        }
    }

    private fun setHead3Span(opening: Boolean, output: Editable) {
        if (opening) {
            setMarker(output)
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            val end = output.length
            output.removeSpan(marker)

            output.setSpan(
                CustomTypefaceSpan(
                    context.resources.getFont(R.font.ys_display_medium),
                    context.resources.getDimension(R.dimen.html_h3_text_size)
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            output.append("\n")
        }
    }

    private fun setParagraphSpan(opening: Boolean, output: Editable) {
        if (opening) {
            setMarker(output)
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            val end = output.length
            output.removeSpan(marker)

            output.setSpan(
                CustomTypefaceSpan(
                    context.resources.getFont(R.font.ys_display_regular),
                    context.resources.getDimension(R.dimen.html_p_text_size)
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            output.append("\n")
        }
    }

    private fun setUnorderedListSpan(opening: Boolean, output: Editable) {
        if (opening) {
            setMarker(output)
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            val end = output.length
            output.removeSpan(marker)

            output.setSpan(
                CustomTypefaceSpan(
                    context.resources.getFont(R.font.ys_display_regular),
                    context.resources.getDimension(R.dimen.html_ul_text_size)
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun setListItemSpan(opening: Boolean, output: Editable) {
        if (opening) {
            setMarker(output)
            output.append(context.resources.getString(R.string.list_item_marker))
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            output.removeSpan(marker)

            output.append("\n")
            val end = output.length

            output.setSpan(
                LeadingMarginSpan.Standard(
                    0,
                    context.resources.getDimensionPixelSize(R.dimen.list_item_not_first_line_margin)
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun setMargin(opening: Boolean, output: Editable, marginSize: Int) {
        if (opening) {
            setMarker(output)
        } else {
            val markers = output.getSpans(0, output.length, Marker::class.java)
            val marker = markers.lastOrNull() ?: return
            val start = output.getSpanStart(marker)
            output.removeSpan(marker)
            output.append("\u200B\n")
            val end = output.length

            output.setSpan(
                LineHeightSpan { _, _, _, _, _, fm ->
                    val currentHeight = fm.descent - fm.ascent
                    val totalDelta = marginSize - currentHeight
                    fm.descent += totalDelta
                },
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun setMarker(output: Editable) {
        output.setSpan(
            Marker(),
            output.length,
            output.length,
            Spanned.SPAN_MARK_MARK
        )
    }
}

private class Marker

private class CustomTypefaceSpan(private val typeface: Typeface, private val textSize: Float) : MetricAffectingSpan() {
    override fun updateMeasureState(p: TextPaint) = update(p)
    override fun updateDrawState(tp: TextPaint) = update(tp)

    private fun update(paint: TextPaint) {
        paint.typeface = typeface
        paint.textSize = textSize
    }
}
