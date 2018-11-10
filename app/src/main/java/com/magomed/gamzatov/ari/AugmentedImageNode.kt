package com.magomed.gamzatov.ari

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Button
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import java.util.concurrent.CompletableFuture

private const val TAG = "AugmentedImageNode"

class AugmentedImageNode(private val context: Context, layout: Int) : AnchorNode() {

    // The augmented image represented by this node.
    private var text: CompletableFuture<ViewRenderable>? = null

    init {
        if (text == null) {
            text = ViewRenderable.builder()
                .setView(context, layout)
                .setSizer { Vector3(0.07f, 0.06f, 0.0f) }
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
            val pose = Pose.makeTranslation(0.0f, 0.0f, 0.5f)

            node.setParent(this)
            node.localPosition = Vector3(pose.tx(), pose.ty(), pose.tz() * image!!.extentZ)
            node.localRotation = Quaternion(pose.qx() - 1f, pose.qy(), pose.qz(), pose.qw())
            val renderable = text?.getNow(null)
                renderable?.setSizer {
                    Vector3(image.extentX, image.extentZ / 2.2f, 0f)
            }
            node.renderable = renderable

            val view = renderable?.view
            val moreButton = view?.findViewById<Button>(R.id.more_button)
            moreButton?.setOnClickListener {
                Log.e(TAG, "Click!!!!!!!!!!!!!!!!!!!!!")
                val intent = Intent(context, MoreActivity::class.java)
                intent.putExtra("who", "Чайковский")
                startActivity(context, intent, null)
            }
       }
}