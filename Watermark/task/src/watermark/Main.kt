package watermark

import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess
import java.awt.Color

fun fileCreator(fileName: String): File {
    val separator = File.separator
    val workingDirectory = System.getProperty ("user.dir")
    val filePath = "${workingDirectory}${separator}" +
            if ("/" in fileName) fileName.replace("/", separator) else fileName
    return File(filePath)
}

class InputImage(type: String) {
    private val fileName: String
    private val imageFile: File
    val image: BufferedImage
    init {
        println("Input the ${if (type == "watermark") "watermark image" else type} filename:")
        fileName = readln()
        imageFile = fileCreator(fileName)
        if (imageFile.exists()) {
            image = ImageIO.read(imageFile)
            if (image.colorModel.numColorComponents != 3) {
                print("The number of $type color components isn't 3.")
                exitProcess(0)
            }
            if (image.colorModel.pixelSize != 24 && image.colorModel.pixelSize != 32) {
                print("The $type isn't 24 or 32-bit.")
                exitProcess(0)
            }
        } else {
            print("The file $fileName doesn't exist.")
            exitProcess(0)
        }
    }
}

object OutputImage {
    private val outputFileName: String
    private val outputFileExtension: String
    init {
        println("Input the output image filename (jpg or png extension):")
        outputFileName = readln()
        outputFileExtension = outputFileName.substringAfter(".")
        if (outputFileExtension != "jpg" && outputFileExtension != "png") {
            print("The output file extension isn't \"jpg\" or \"png\".")
            exitProcess(0)
        }
    }

    fun createOutput(image: BufferedImage, watermark: BufferedImage) {
        val outputImage = Blender.blendImage(image, watermark)
        val outputFile = fileCreator(outputFileName)
        ImageIO.write(outputImage, outputFileExtension, outputFile)
        print("The watermarked image $outputFileName has been created.")
    }
}

object Blender {
    var useAlpha = false
    var useTransparencyColor = false
    private var transparencyColor = Color(0,0,0)
    private var transparencyPercentage = 0
    private var positionMethod = ""
    private var positionX = 0
    private var positionY = 0

    fun setTransparencyColor() {
        println("Input a transparency color ([Red] [Green] [Blue]):")
        val input = readln()
        if (Regex("\\d{1,3} \\d{1,3} \\d{1,3}").matches(input)) {
            val (red, green, blue) = input.split(" ").map { it.toInt() }
            if (red !in 0..255 || green !in 0..255 || blue !in 0..255) {
                print("The transparency color input is invalid.")
                exitProcess(0)
            } else {
                transparencyColor = Color(red, green, blue)
            }
        } else {
            print("The transparency color input is invalid.")
            exitProcess(0)
        }
    }

    fun setTransparencyPercentage() {
        println("Input the watermark transparency percentage (Integer 0-100):")
        val input = readln()
        if (Regex("\\d{1,3}").matches(input)) {
            transparencyPercentage = input.toInt()
            if (transparencyPercentage !in 0..100) {
                print("The transparency percentage is out of range.")
                exitProcess(0)
            }
        } else {
            print("The transparency percentage isn't an integer number.")
            exitProcess(0)
        }
    }

    fun choosePositionMethod(image: BufferedImage, watermark: BufferedImage) {
        println("Choose the position method (single, grid):")
        val input = readln()
        if (input != "single" && input != "grid") {
            println("The position method input is invalid.")
            exitProcess(0)
        } else {
            positionMethod = input
            if (positionMethod == "single") {
                val diffX = image.width - watermark.width
                val diffY = image.height - watermark.height
                println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
                val position = readln()
                if (Regex("-?\\d+ -?\\d+").matches(position)) {
                    positionX = position.split(" ")[0].toInt()
                    positionY = position.split(" ")[1].toInt()
                    if (positionX !in 0..diffX || positionY !in 0..diffY) {
                        println("The position input is out of range.")
                        exitProcess(0)
                    }
                } else {
                    println("The position input is invalid.")
                    exitProcess(0)
                }
            }
        }
    }

    fun blendImage(image: BufferedImage, watermark: BufferedImage): BufferedImage {
        val outputImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val i = Color(image.getRGB(x, y))
                val w = if (positionMethod == "single" &&
                        x in positionX until watermark.width + positionX &&
                        y in positionY until watermark.height + positionY) {
                    Color(watermark.getRGB(x - positionX, y - positionY), true)
                } else {
                    Color(watermark.getRGB(x % watermark.width, y % watermark.height), true)
                }

                if (positionMethod == "single" &&
                    !(x in positionX until watermark.width + positionX &&
                        y in positionY until watermark.height + positionY)) {
                    val color = Color(i.red, i.green, i.blue)
                    outputImage.setRGB(x, y, color.rgb)
                } else {
                    if (useAlpha && w.alpha == 0) {
                        val color = Color(i.red, i.green, i.blue)
                        outputImage.setRGB(x, y, color.rgb)
                    } else if (useTransparencyColor &&
                        w.red == transparencyColor.red &&
                        w.green == transparencyColor.green &&
                        w.blue == transparencyColor.blue) {
                        val color = Color(i.red, i.green, i.blue)
                        outputImage.setRGB(x, y, color.rgb)
                    } else {
                        val color = Color(
                            (transparencyPercentage * w.red + (100 - transparencyPercentage) * i.red) / 100,
                            (transparencyPercentage * w.green + (100 - transparencyPercentage) * i.green) / 100,
                            (transparencyPercentage * w.blue + (100 - transparencyPercentage) * i.blue) / 100)
                        outputImage.setRGB(x, y, color.rgb)
                    }
                }
            }
        }

        return outputImage
    }
}

fun main() {
    val input = InputImage("image")
    val watermark = InputImage("watermark")

    if (watermark.image.width > input.image.width || watermark.image.height > input.image.height) {
        print("The watermark's dimensions are larger.")
        exitProcess(0)
    }

    if (watermark.image.transparency == 3) {
        println("Do you want to use the watermark's Alpha channel?")
        Blender.useAlpha = readln().lowercase() == "yes"
    } else {
        println("Do you want to set a transparency color?")
        Blender.useTransparencyColor = readln().lowercase() == "yes"
        if (Blender.useTransparencyColor) Blender.setTransparencyColor()
    }

    Blender.setTransparencyPercentage()
    Blender.choosePositionMethod(input.image, watermark.image)

    val output = OutputImage
    output.createOutput(input.image, watermark.image)
}