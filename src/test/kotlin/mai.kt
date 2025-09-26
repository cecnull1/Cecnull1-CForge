import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.addComponent
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.createComponentMap
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.getComponent
import com.github.cecnull1.cecnull1_cforge.core.IComponent
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_cforge.core.ResourceLocation
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

typealias rl = ResourceLocation

class Test {
    @Test
    fun test() = Unit process {
// 2. 测试环境 = 纯 Kotlin 对象
        val map = createComponentMap()
        val dummy = Any()
        dummy.addComponent(map, rl("test", "data"), Health(20))
        assertEquals(20, dummy.getComponent<Health>(map, rl("test", "data"))?.current)
    }

}

data class Health(var current: Int) : IComponent