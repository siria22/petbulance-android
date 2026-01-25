// 🚨 IDE에서는 이 import 구문들이 오류로 표시될 수 있으나,
//    Gradle 태스크 실행 시 -classpath 인수로 주입되므로 실제 빌드는 성공합니다.
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.CodeBlock

// --- 데이터 클래스 정의 ---
@Serializable
data class ColorToken(val value: String, val type: String)

@Serializable
data class ColorPrimitiveTokens(
    val base: Map<String, ColorToken>? = null,
    val gray: Map<String, ColorToken>? = null,
    val primary: Map<String, ColorToken>? = null,
    val secondary: Map<String, ColorToken>? = null,
    val tertiary: Map<String, ColorToken>? = null,
    val quaternary: Map<String, ColorToken>? = null,
    val positive: Map<String, ColorToken>? = null,
    val negative: Map<String, ColorToken>? = null,
    val warning: Map<String, ColorToken>? = null,
    val info: Map<String, ColorToken>? = null
)

@Serializable
data class RootTokens(
    val `color-primitive`: ColorPrimitiveTokens
)


// --- 헬퍼 함수: KotlinPoet 프로퍼티 생성 ---
fun generateCategoryObjects(primitives: ColorPrimitiveTokens, colorClass: ClassName): List<TypeSpec> {
    val categoryObjects = mutableListOf<TypeSpec>()

    val tokenMap = mapOf(
        "base" to primitives.base,
        "gray" to primitives.gray,
        "primary" to primitives.primary,
        "secondary" to primitives.secondary,
        "tertiary" to primitives.tertiary,
        "quaternary" to primitives.quaternary,
        "positive" to primitives.positive,
        "negative" to primitives.negative,
        "warning" to primitives.warning,
        "info" to primitives.info
    )

    tokenMap.forEach { (categoryName, tokenSet) ->
        if (tokenSet.isNullOrEmpty()) return@forEach // 토큰이 없으면 스킵

        // 1. 중첩 객체 빌더 생성 (예: "gray" -> "Gray")
        val nestedObjectName = categoryName.replaceFirstChar { it.uppercase() }
        val nestedObjectBuilder = TypeSpec.objectBuilder(nestedObjectName)

        val properties = mutableListOf<PropertySpec>()
        tokenSet.forEach { (tokenName, tokenData) ->
            if (tokenData.type == "color") {

                // 2. 🚨 (수정) 프로퍼티 이름 생성 로직
                //    "50" -> "p50", "white" -> "white"
                val propName = if (tokenName.toIntOrNull() != null) {
                    "p${tokenName}"
                } else {
                    tokenName
                }

                // Hex(#ffffff)를 Compose Color(0xFFffffff) 코드로 변환
                val colorHex = tokenData.value.replace("#", "0xFF").uppercase()

                properties.add(
                    PropertySpec.builder(propName, colorClass)
                        .initializer(CodeBlock.of("%T(%L)", colorClass, colorHex))
                        .build()
                )
            }
        }

        // 3. 중첩 객체에 프로퍼티 추가
        nestedObjectBuilder.addProperties(properties)

        // 4. 완성된 중첩 객체를 리스트에 추가
        categoryObjects.add(nestedObjectBuilder.build())
    }
    return categoryObjects
}

// --- 메인 로직 ---
// 1. Gradle 태스크의 'args'에서 출력 디렉터리 경로를 받습니다.
val outputDirPath = args.getOrNull(0) ?: throw IllegalArgumentException("Output directory argument (args[0]) is missing")
val tokenFilePath = args.getOrNull(1) ?: throw IllegalArgumentException("Token file path argument (args[1]) is missing")

// 🚨 (수정) 출력 파일명 변경
val outputFileName = "PetbulancePrimitives.kt"
val outputFile = File(outputDirPath, outputFileName) // 생성될 파일
val tokenFile = File(tokenFilePath) // tokens.json

println("--- Design Token Generation ---")
println("Input (tokens.json): ${tokenFile.path}")
println("Output (Primitives.kt): ${outputFile.path}")

if (!tokenFile.exists()) {
    println("❌ Error: tokens.json file not found.")
    System.exit(1)
}

try {
    // 2. JSON 파싱
    println("✅ tokens.json found. Starting parsing...")
    val json = Json { ignoreUnknownKeys = true }
    val jsonText = tokenFile.readText()
    val root = json.decodeFromString<RootTokens>(jsonText)
    val colorPrimitives = root.`color-primitive`
    println("✅ JSON parsing successful!")

// 3. 🚨 (수정) KotlinPoet으로 *중첩 구조* 파일 생성
    val colorClass = ClassName("androidx.compose.ui.graphics", "Color")
    // 헬퍼 함수 호출 (List<TypeSpec> 반환)
    val categoryObjects = generateCategoryObjects(colorPrimitives, colorClass)

    val fileSpec = FileSpec.builder("com.example.presentation.component.theme.color", "Primitives")
        .addType(
            TypeSpec.objectBuilder("PetbulancePrimitives")
                .addTypes(categoryObjects)
                .build()
        )
        .addImport("androidx.compose.ui.graphics", "Color")
        .build()

    // 4. 실제 파일 작성
    outputFile.writeText(fileSpec.toString())

    println("✅ Primitives.kt file generated successfully with ${categoryObjects.size} nested color objects.")

} catch (e: Exception) {
    println("❌ Error during script execution: ${e.message}")
    e.printStackTrace()
    System.exit(1)
}