package com.magomed.gamzatov.ari

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.core.Pose
import com.google.ar.sceneform.rendering.ViewRenderable

private const val TAG = "AugmentedImageNode"

class AugmentedImageNode(context: Context, layout: Int) : AnchorNode() {

    // The augmented image represented by this node.
//    private var wolf: CompletableFuture<ModelRenderable>? = null
    private var text: CompletableFuture<ViewRenderable>? = null

    init {
//        if (wolf == null) {
//            wolf = ModelRenderable.builder()
//                .setSource(context, Uri.parse("Wolf.sfb"))
//                .build()
//        }
        if (text == null) {
            text = ViewRenderable.builder()
                .setView(context, layout)
                .setSizer { Vector3(0.1f, 0.05f, 0.0f) }
                .build()
        }
    }

    var image: AugmentedImage? = null
        set(image) {
            field = image
            // If any of the models are not loaded, then recurse when all are loaded.
            if (text?.isDone == false) {
                CompletableFuture.allOf(text)
                    .thenAccept { this.image = image }
                    .exceptionally { throwable ->
                        Log.e(TAG, "Exception loading", throwable)
                        null
                    }
            }

            // Set the anchor based on the center of the image.
            anchor = image?.createAnchor(image.centerPose)

            // Make the 4 corner nodes.
            val node = Node()
            val pose = Pose.makeTranslation(0.0f, 0.0f, 0.0f)

            node.setParent(this)
            node.localPosition = Vector3(pose.tx(), pose.ty(), pose.tz() + 0.05f)
            node.localRotation = Quaternion(pose.qx() - 1f, pose.qy(), pose.qz(), pose.qw())
            node.renderable = text?.getNow(null)
       }
}