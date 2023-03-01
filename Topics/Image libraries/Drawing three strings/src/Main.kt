import java.awt.Color
import java.awt.image.BufferedImage

fun drawStrings(): BufferedImage {
    val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()
    val text = "Hello, images!"
    graphics.color = Color.RED
    graphics.drawString(text, 50, 50)
    graphics.color = Color.GREEN
    graphics.drawString(text, 51, 51)
    graphics.color = Color.BLUE
    graphics.drawString(text, 52, 52)
    return image
}