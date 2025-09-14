import com.github.cecnull1.cecnull1_cforge.core.PipeCore.calc
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.invoke
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.orElse
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.pipeIf
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then

fun main() = process {
    val println: (Any?) -> Unit =:: println
    val number = 64
    val number2 = 128
    number2 {
        pipeIf(true) then println orElse { number process println }
        pipeIf(false) then println orElse { number process println }
    } then {
        // 此块总被执行
    }
}

fun dslTest(block: TestBuilder.() -> Unit): TestBuilder {
    val e = TestBuilder()
    e.block()
    return e
}

class TestBuilder {
    var hi: String = "Default"
    var hi2: String = "Default"
    var hi3: Byte = 0
    fun build(): E {
        return E(hi, hi2, hi3)
    }
}

data class E(val hi: String, val hi2: String, val hi3: Byte) {
    fun toByteArray(): ByteArray = calc {
        byteArrayOf(
            *hi.toByteArray(Charsets.UTF_8),
            (-1).toByte(), (-1).toByte(),
            *hi2.toByteArray(Charsets.UTF_8),
            (-1).toByte(), (-1).toByte(),
            hi3,
            (-1).toByte(), (-1).toByte()
        )
    }
}
