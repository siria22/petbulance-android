package com.petbulance.presentation.utils

import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import org.xml.sax.XMLReader

@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val h3Style = typography.titleSmall
    val h4Style = typography.titleSmall
    val pStyle = typography.bodyMedium

    val h3Color = colorScheme.text.primary
    val h4Color = colorScheme.text.primary
    val pColor = colorScheme.text.primary

    val h3SizePx = remember(density, h3Style) { with(density) { h3Style.fontSize.toPx().toInt() } }
    val h4SizePx = remember(density, h4Style) { with(density) { h4Style.fontSize.toPx().toInt() } }
    val pSizePx = remember(density, pStyle) { with(density) { pStyle.fontSize.toPx().toInt() } }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextIsSelectable(false)
            }
        },
        update = { textView ->
            val processedHtml = html
                .replace("\n", "<br/>")
                .replace("<br>", "<br/>", ignoreCase = true)
                .replace("<h3>", "<h3-c>", ignoreCase = true)
                .replace("</h3>", "</h3-c><br/>", ignoreCase = true)
                .replace("<h4>", "<h4-c>", ignoreCase = true)
                .replace("</h4>", "</h4-c><br/>", ignoreCase = true)
                .replace("<p>", "<p-c>", ignoreCase = true)
                .replace("</p>", "</p-c>", ignoreCase = true)

            val tagHandler = object : Html.TagHandler {
                var pStart = 0
                var h3Start = 0
                var h4Start = 0


                override fun handleTag(
                    opening: Boolean,
                    tag: String?,
                    output: Editable?,
                    xmlReader: XMLReader?
                ) {
                    if (output == null) return
                    when (tag?.lowercase()) {
                        "p-c" -> {
                            if (opening) pStart = output.length
                            else {
                                output.setSpan(
                                    ForegroundColorSpan(pColor.toArgb()),
                                    pStart,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                output.setSpan(
                                    AbsoluteSizeSpan(pSizePx),
                                    pStart,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                pStyle.fontWeight?.let {
                                    output.setSpan(
                                        StyleSpan(Typeface.NORMAL),
                                        pStart,
                                        output.length,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                output.append("\n")
                            }
                        }

                        "h3-c" -> {
                            if (opening) h3Start = output.length
                            else {
                                output.setSpan(
                                    ForegroundColorSpan(h3Color.toArgb()),
                                    h3Start,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                output.setSpan(
                                    AbsoluteSizeSpan(h3SizePx),
                                    h3Start,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                h3Style.fontWeight?.let {
                                    output.setSpan(
                                        StyleSpan(Typeface.BOLD),
                                        h3Start,
                                        output.length,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                output.append("\n\n")
                            }
                        }

                        "h4-c" -> {
                            if (opening) h4Start = output.length
                            else {
                                output.setSpan(
                                    ForegroundColorSpan(h4Color.toArgb()),
                                    h4Start,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                output.setSpan(
                                    AbsoluteSizeSpan(h4SizePx),
                                    h4Start,
                                    output.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                h4Style.fontWeight?.let {
                                    output.setSpan(
                                        StyleSpan(Typeface.BOLD),
                                        h4Start,
                                        output.length,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                output.append("\n")
                            }
                        }
                    }
                }
            }

            textView.text = processedHtml.parseAsHtml(
                HtmlCompat.FROM_HTML_MODE_COMPACT,
                null,
                tagHandler
            )
        }
    )
}

@Preview(apiLevel = 34)
@Composable
private fun HtmlViewerPreview() {
    PetbulanceTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            HtmlText(
                html = """
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>서비스 이용약관</title>
    <style>
        /* 기본 스타일 초기화 및 폰트 설정 */
        body {
            font-family: 'Pretendard', 'Noto Sans KR', -apple-system, BlinkMacSystemFont, sans-serif;
            line-height: 1.6;
            color: #333;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }

        /* 약관 전체 컨테이너 */
        .terms-container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 40px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            border-radius: 8px;
            border: 1px solid #e0e0e0;
        }

        /* 메인 타이틀 */
        h1 {
            font-size: 24px;
            text-align: center;
            margin-bottom: 40px;
            padding-bottom: 20px;
            border-bottom: 2px solid #333;
            color: #111;
        }

        /* 조항 타이틀 (제1조 등) */
        h2 {
            font-size: 18px;
            margin-top: 30px;
            margin-bottom: 12px;
            color: #2c3e50;
            border-bottom: 1px solid #eee;
            padding-bottom: 8px;
        }

        /* 본문 텍스트 */
        p {
            font-size: 14px;
            margin-bottom: 10px;
            word-break: keep-all; /* 한글 줄바꿈 최적화 */
            color: #555;
        }

        /* 리스트 스타일 */
        ul, ol {
            margin: 10px 0;
            padding-left: 25px;
            font-size: 14px;
            color: #555;
        }

        li {
            margin-bottom: 5px;
        }

        /* 강조 텍스트 */
        strong {
            color: #000;
            font-weight: 600;
        }

        /* 부칙 (하단) */
        .addendum {
            margin-top: 50px;
            padding-top: 20px;
            border-top: 1px solid #ccc;
            text-align: right;
            font-size: 13px;
            color: #777;
        }

        /* 모바일 대응 */
        @media (max-width: 600px) {
            .terms-container {
                padding: 20px;
            }
            h1 {
                font-size: 20px;
            }
            h2 {
                font-size: 16px;
            }
        }
    </style>
</head>
<body>

    <div class="terms-container">
        <h1>서비스 이용약관</h1>

        <h2>제1조 (목적)</h2>
        <p>이 약관은 <strong>주식회사 펫뷸런스</strong>(이하 “회사”)가 제공하는 특수동물 병원 정보 검색, 후기 등록, 비대면 상담, 실시간 진료 여부 확인 등 서비스(이하 “서비스”)의 이용조건 및 절차, 회사와 이용자(회원 및 비회원) 간의 권리·의무 및 책임사항을 규정함을 목적으로 합니다.</p>

        <h2>제2조 (정의)</h2>
        <ul>
            <li><strong>“이용자”</strong>란 본 약관에 동의하고 서비스를 이용하는 회원 및 비회원을 말합니다.</li>
            <li><strong>“회원”</strong>이란 회사와 이용계약을 체결하고 서비스를 이용하는 자를 말합니다.</li>
            <li><strong>“비회원”</strong>이란 회원가입 없이 회사가 제공하는 일부 서비스를 이용하는 자를 말합니다.</li>
            <li><strong>“콘텐츠”</strong>란 이용자가 서비스 내에서 등록하거나 열람할 수 있는 후기, 사진, 병원 정보, 상담기록 등을 의미합니다.</li>
            <li><strong>“병원정보제공자”</strong>란 회사와 제휴 또는 등록절차에 따라 병원 정보를 제공하는 특수동물 병원을 말합니다.</li>
        </ul>

        <h2>제3조 (약관의 게시 및 변경)</h2>
        <ol>
            <li>회사는 이 약관의 내용을 이용자가 쉽게 알 수 있도록 서비스 초기 화면 또는 웹사이트에 게시합니다.</li>
            <li>회사는 관련 법령을 위배하지 않는 범위 내에서 약관을 변경할 수 있으며, 변경 시 변경사유 및 적용일자를 명시해 최소 7일 이상 공지하고, 이용자가 거부의사를 표시하지 않으면 승인된 것으로 봅니다.</li>
            <li>변경된 약관은 공지된 적용일자 이후부터 효력이 발생합니다.</li>
        </ol>

        <h2>제4조 (이용계약의 체결 및 해지)</h2>
        <p>이용자는 본 약관에 동의하고 회원가입 절차를 완료함으로써 이용계약이 성립됩니다. 회사는 다음 각 호에 해당하는 신청에 대해 승낙을 하지 않을 수 있습니다:</p>
        <ul>
            <li>허위 정보를 등록한 경우</li>
            <li>이용신청 요건을 충족하지 않은 경우</li>
        </ul>
        <p>회원은 언제든지 서비스 내 계정 삭제 또는 탈퇴 신청을 할 수 있으며, 회사는 관련 절차를 이용자가 탈퇴 요청한 날 또는 회원이 탈퇴 기준을 충족한 날로부터 처리합니다.</p>

        <h2>제5조 (서비스의 제공 및 변경)</h2>
        <p>회사는 다음과 같은 서비스를 제공합니다:</p>
        <ul>
            <li>특수동물 병원 정보 검색 및 필터링</li>
            <li>병원 후기 등록 및 열람</li>
            <li>커뮤니티 글 등록 및 작성</li>
            <li>기타 회사가 추가개발 또는 제휴를 통해 제공하는 서비스</li>
        </ul>
        <p>회사는 서비스의 내용 및 제공 시간을 변경할 수 있으며, 변경이 필요한 경우 이용자에게 사전 공지합니다. 회사는 천재지변, 설비점검, 장애 등으로 서비스 제공이 지연되거나 중단될 수 있으며, 이에 대해 책임을 지지 않을 수 있습니다.</p>

        <h2>제6조 (이용자의 의무)</h2>
        <p>이용자는 관련 법령, 본 약관, 운영정책 및 공지사항을 준수해야 합니다. 이용자는 서비스를 이용함에 있어 다음 행위를 하여서는 안 됩니다:</p>
        <ul>
            <li>허위 정보 등록 또는 타인의 정보를 도용하는 행위</li>
            <li>서비스의 안정적 운영을 방해하는 행위</li>
            <li>타인의 권리를 침해하거나 명예를 훼손하는 행위</li>
        </ul>
        <p>이용자는 회사가 요청하는 경우 이용신청 사항을 진실하게 제공해야 하며, 변경사항이 있는 경우 즉시 통지해야 합니다.</p>

        <h2>제7조 (콘텐츠의 저작권 및 이용제한)</h2>
        <p>서비스 내 등록된 콘텐츠(후기, 사진 등)의 저작권은 원칙적으로 해당 이용자에게 귀속됩니다. 이용자는 자신이 등록한 콘텐츠가 제3자의 권리를 침해하지 않음을 보장해야 합니다. 회사는 이용자가 등록한 콘텐츠를 서비스 운영·홍보 목적으로 사용할 수 있으며, 이용자는 이를 위해 별도 동의합니다.</p>

        <h2>제8조 (회사의 면책 및 책임제한)</h2>
        <p>회사는 병원정보, 이용후기, 상담 결과 등 제3자가 제공한 정보의 정확성, 완전성, 적합성에 대해 보증하지 않습니다. 이용자 간의 상담, 진료예약, 병원 방문 등과 관련한 분쟁에 대하여 회사는 책임을 지지 않습니다. 회사는 무료로 제공되는 서비스 이용으로 인해 이용자에게 발생한 손해에 대해서도, 회사가 고의 또는 중과실로 입증되지 않는 한 책임을 지지 않습니다.</p>

        <h2>제9조 (서비스 이용의 제한 및 중지)</h2>
        <p>회사는 다음 각 호의 사유가 있을 경우 이용자의 서비스 이용을 제한하거나 중지할 수 있습니다:</p>
        <ul>
            <li>이용자가 약관 또는 운영정책을 위반한 경우</li>
            <li>타인의 명의를 도용하거나 허위로 계정을 생성한 경우</li>
            <li>기타 회사가 서비스 운영상 필요하다고 인정한 경우</li>
        </ul>
        <p>이 경우 회사는 사전 통지하거나 사후 통지할 수 있으며, 중지 기간 및 조건 등을 이용자에게 안내합니다.</p>

        <h2>제10조 (약관의 해석 및 준거법·분쟁해결)</h2>
        <p>본 약관에 명시되지 않은 사항은 관계법령 및 상관례에 따릅니다. 본 약관과 이용계약의 해석 및 이행에 관하여 분쟁이 발생할 경우 대한민국 법률을 준거법으로 하며, 관할 법원은 회사의 본사 소재지를 관할하는 법원으로 합니다.</p>

        <div class="addendum">
            <strong>부칙</strong><br>
            이 약관은 2025년 00월 00일부터 시행됩니다.
        </div>
    </div>

</body>
</html>
            """.trimIndent()
            )
        }
    }
}