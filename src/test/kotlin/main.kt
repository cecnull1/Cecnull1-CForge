import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.addComponent
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.createComponentMap
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process

fun main() = process {
    createComponentMap() process {
        val dummy = Any()
        dummy.addComponent(this, rl("test", "data"), Health(20))
    }
}