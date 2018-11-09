package com.magomed.gamzatov.ari

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private lateinit var fitToScanView: ImageView

    private val augmentedImageMap = HashMap<AugmentedImage, AugmentedImageNode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        fitToScanView = findViewById(R.id.image_view_fit_to_scan)

        arFragment.arSceneView.scene.addOnUpdateListener(this::onUpdateFrame)
    }

    override fun onResume() {
        super.onResume()
        if (augmentedImageMap.isEmpty()) {
            fitToScanView.visibility = View.VISIBLE
        }
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = arFragment.arSceneView.arFrame

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
        for (augmentedImage in updatedAugmentedImages) {
            when (augmentedImage.trackingState) {
                TrackingState.PAUSED -> {
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    val text = "Detected Image " + augmentedImage.name
                    SnackbarHelper.getInstance().showMessage(this, text)
                }

                TrackingState.TRACKING -> {
                    // Have to switch to UI Thread to update View.
                    fitToScanView.visibility = View.GONE

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        when (augmentedImage.name) {
                            DEFAULT_IMAGE_NAME -> {
                                val node = AugmentedImageNode(this, R.layout.text_test)
                                node.image = augmentedImage
                                augmentedImageMap[augmentedImage] = node
                                arFragment.arSceneView.scene.addChild(node)
                            }
                            DEFAULT_IMAGE_NAME2 -> {
                                val node = AugmentedImageNode(this, R.layout.text_chaykovsky)
                                node.image = augmentedImage
                                augmentedImageMap[augmentedImage] = node
                                arFragment.arSceneView.scene.addChild(node)
                            }
                        }
                    }
                }

                TrackingState.STOPPED -> augmentedImageMap.remove(augmentedImage)
            }
        }
    }
}
