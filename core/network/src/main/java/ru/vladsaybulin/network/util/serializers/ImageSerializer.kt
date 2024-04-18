package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import ru.vladsaybulin.network.common.BuildConfig
import ru.vladsaybulin.network.models.common.NetworkImage

/**
 * Rest api returns the path to the image.
 * This serializer turns the path into a link by adding the base url before the path
 */
class ImageSerializer : KSerializer<NetworkImage> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("image") {
        val stringDescriptor = String.serializer().descriptor
        element("preview", stringDescriptor)
        element("original", stringDescriptor)
    }

    override fun deserialize(decoder: Decoder): NetworkImage = decoder.decodeStructure(descriptor) {
        var previewPath: String? = null
        var originalPath: String? = null
        while (true) {
            when (decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> {
                    previewPath = decodeStringElement(descriptor, index = 0)
                }
                1 -> {
                    originalPath = decodeStringElement(descriptor, index = 1)
                }
                else -> continue //Skip other fields
            }
        }

        return@decodeStructure NetworkImage(
            previewUrl = BASE_URL + checkNotNull(previewPath),
            originalUrl = BASE_URL + checkNotNull(originalPath)
        )
    }

    override fun serialize(encoder: Encoder, value: NetworkImage) {
        throw UnsupportedOperationException()
    }

}

private const val BASE_URL = BuildConfig.BASE_URL