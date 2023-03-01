class Vehicle {
    // add name
    val name: String
    // create constructor
    constructor(name: String) {
        this.name = name
    }
    // create inner class Body
    inner class Body {
        private val color: String
        constructor(color: String) {
            this.color = color
        }

        fun printColor() {
            println("The $name vehicle has a $color body.")
        }
    }
}