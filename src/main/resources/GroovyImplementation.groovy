import com.example.demo.script.ScriptExample

class GroovyImplementation implements ScriptExample {
    @Override
    void doSomething() {
        println "${this.getClass()}!"
    }
}